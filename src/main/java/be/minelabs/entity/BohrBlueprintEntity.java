package be.minelabs.entity;

import be.minelabs.Minelabs;
import be.minelabs.advancement.criterion.BohrCriterion;
import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.Blocks;
import be.minelabs.block.blocks.BohrBlueprintBlock;
import be.minelabs.entity.projectile.thrown.ChargedEntity;
import be.minelabs.entity.projectile.thrown.ParticleEntity;
import be.minelabs.item.items.AtomItem;
import be.minelabs.item.Items;
import be.minelabs.mixin.FishingBobberEntityAccessor;
import be.minelabs.particle.ParticleTypes;
import be.minelabs.util.AtomConfiguration;
import be.minelabs.world.MinelabsGameRules;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

public class BohrBlueprintEntity extends Entity {
    // Public constants
    public static final int MAX_PROTONS = 200;
    public static final int MAX_ELECTRONS = 126;    // for rendering purposes (8 above Og)
    public static final int MAX_NEUTRONS = 200;

    // Private constants
    // how much integrity decreases each tick
    private static final float INTEGRITY_DECAY_STEP = 1f / (10f * 20);
    // per tick this value times the amount of extra electrons is subtracted from the timer.
    private static final float electronEjectProgressPerTick = 1f / (2 * 20);

    // Transient data (not persisted or tracked)
    private int validityCheckCounter = 0;

    // server side information (only stored in nbt)
    // timer for when to eject an electron when too unstable. Starts from 1 and when 0 launches an electron.
    private float electronEjectProgress = 1f;

    // Ordered list of items added to the entity. Should only contain protons, electrons neutrons and atoms.
    // Anti particles are instead removed and can never be added if corresponding particle is not present.
    // Atom can only be added as first entry and cannot remove parts of it.
    private final Stack<ItemStack> inventory = new Stack<>();

    // tracked data is synced to the client automatically (still needs to be written to nbt if it needs to be persisted)
    protected static final TrackedData<AtomConfiguration> ATOM_CONFIGURATION = DataTracker.registerData(BohrBlueprintEntity.class, AtomConfiguration.DATA_HANDLER);

    // Integrity ranges from MAX_INTEGRITY to 0 where at 0 the configuration decomposes because it is too unstable.
    // Decreases by one per tick if unstable.
    protected static final TrackedData<Float> INTEGRITY = DataTracker.registerData(BohrBlueprintEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public BohrBlueprintEntity(EntityType<? extends BohrBlueprintEntity> entityType, World world) {
        super(entityType, world);
    }

    public BohrBlueprintEntity(World world, BlockPos pos) {
        this(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, world);
        // entity position is center of bottom, the -0.5 offset aligns it back to the block grid
        setPosition(Vec3d.ofCenter(pos).add(0, -0.5, 0));
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.5f * dimensions.height;
    }

    @Override
    public void tick() {
        if (this.isRemoved())
            return;

        if (this.world.isClient) {
            clientTick();
            return;
        }

        logicalTick();
    }

    /**
     * Only runs on version of entity used for rendering.
     */
    private void clientTick() {
        if (getProtons() == 0 && getNeutrons() == 0) {
            this.world.addParticle(ParticleTypes.BOHR_PLATE_PARTICLE, this.getX(), this.getY() + 0.5f * getHeight(), this.getZ(), 0, 0, 0);
        }
    }

    /**
     * Runs on logical server (can either be in physical client or in physical server).
     */
    private void logicalTick() {
        this.attemptTickInVoid();

        // check if still attached to bohr plate, otherwise clean up
        if (this.validityCheckCounter++ == 100) {
            this.validityCheckCounter = 0;
            // cleanup for if the entity unexpectedly got left behind after the block was removed
            if (!this.isAttachedToBlock()) {
                this.discard();
            }
        }

        if (getIntegrity() <= 0) {
            decomposeAtom();
        }

        if (getAtomConfig().isNucleusDecomposing()) {
            decrementIntegrity();
        }

        if (getAtomConfig().isElectronDecomposing()) {
            electronEjectProgress -= electronEjectProgressPerTick * getAtomConfig().getDecomposingElectronCount();

            if (electronEjectProgress <= 0f) {
                if (removeItem(Items.ELECTRON))
                    tryLaunchParticle(Items.ELECTRON);
                electronEjectProgress = 1f;
            }
        }
    }

    // Called by particle when it collides with this entity.
    public void onParticleCollision(ChargedEntity particle) {
        if (world.isClient)
            return;
        ItemStack stack = particle.getStack();
        Item item = stack.getItem();

        if (addItem(item, (ServerPlayerEntity) particle.getOwner())) {
            particle.discard();
        }
    }

    public BlockPos getBohrBlueprintPos() {
        return getBlockPos().down();
    }

    public boolean isAttachedToBlock() {
        return world.getBlockState(getBohrBlueprintPos()).isOf(Blocks.BOHR_BLUEPRINT);
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(INTEGRITY, 1f);
        dataTracker.startTracking(ATOM_CONFIGURATION, new AtomConfiguration(0, 0, 0));
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        // cleanup after entity is removed
        if (reason.shouldDestroy()) {
            dropContents();
            BlockState state = world.getBlockState(getBohrBlueprintPos());
            if (state.isOf(Blocks.BOHR_BLUEPRINT))
                world.removeBlock(getBohrBlueprintPos(), false);
        }
    }

    public boolean isEmpty() {
        // inventory is not synced to client, so need to check counts.
        return getProtons() == 0 && getNeutrons() == 0 && getElectrons() == 0;
    }

    public void dropContents() {
        for (ItemStack stack : inventory) {
            dropStack(stack);
        }
        clear();
    }

    private boolean canAcceptItem(Item item) {
        // Atom can only be first item inserted
        if (item instanceof AtomItem && inventory.isEmpty())
            return true;

        // Subatomic particles have max capacity
        if (item.equals(Items.PROTON))
            return getProtons() < MAX_PROTONS;
        if (item.equals(Items.NEUTRON))
            return getNeutrons() < MAX_NEUTRONS;
        if (item.equals(Items.ELECTRON))
            return getElectrons() < MAX_ELECTRONS;

        // Anti items have min capacity (unless we want to make anti-matter
        if (item.equals(Items.ANTI_PROTON))
            return getProtons() > 0;
        if (item.equals(Items.ANTI_NEUTRON))
            return getNeutrons() > 0;
        if (item.equals(Items.POSITRON))
            return getElectrons() > 0;

        return false;
    }

    @Nullable
    private static Item getAntiItem(Item item) {
        return Map.of(
                Items.ANTI_PROTON, Items.PROTON,
                Items.ANTI_NEUTRON, Items.NEUTRON,
                Items.POSITRON, Items.ELECTRON
        ).get(item);
    }

    public boolean addItem(Item item) {
        return addItem(item, null);
    }

    public boolean addItem(Item item, @Nullable ServerPlayerEntity source) {
        // we don't want the client to modify inventory. Always return false because we can't check inventory for remove.
        if (world.isClient)
            return false;

        if (!canAcceptItem(item))
            return false;

        ItemStack stack = new ItemStack(item, 1);
        inventory.add(stack);
        onItemAdded(stack, source);

        return true;
    }

    private boolean removeItem(Item item) {
        return removeItem(item, null);
    }

    private boolean removeItem(Item item, @Nullable ServerPlayerEntity source) {
        // we don't want the client to modify inventory
        if (world.isClient)
            return false;

        // iterate in reverse order
        for (ListIterator<ItemStack> iterator = inventory.listIterator(inventory.size()); iterator.hasPrevious(); ) {
            ItemStack stack = iterator.previous();
            if (stack.isOf(item)) {
                iterator.remove();
                onItemRemoved(stack, source);
                return true;
            }
        }
        return false;
    }

    public ItemStack removeLastItem(@Nullable ServerPlayerEntity source) {
        if (inventory.isEmpty())
            return ItemStack.EMPTY;

        ItemStack stack = inventory.pop();
        onItemRemoved(stack, source);
        return stack;
    }

    public ItemStack dropLastItem() {
        return dropLastItem(null);
    }

    public ItemStack dropLastItem(@Nullable ServerPlayerEntity source) {
        ItemStack stack = removeLastItem(source);
        dropStack(stack);
        return stack;
    }

    private void updateCountsItemUpdate(ItemStack stack, int amount) {
        if (stack.isOf(Items.PROTON)) incrementProtons(amount);
        else if (stack.isOf(Items.ELECTRON)) incrementElectrons(amount);
        else if (stack.isOf(Items.NEUTRON)) incrementNeutrons(amount);
        else if (stack.isOf(Items.ANTI_PROTON)) incrementProtons(-1 * amount);
        else if (stack.isOf(Items.POSITRON)) incrementElectrons(-1 * amount);
        else if (stack.isOf(Items.ANTI_NEUTRON)) incrementNeutrons(-1 * amount);
        else if (stack.getItem() instanceof AtomItem) updateCountsFromContent();
        else Minelabs.LOGGER.warn("Unkown ItemStack in Bohr Blueprint: " + stack);
    }

    private void onItemAdded(ItemStack stack, @Nullable ServerPlayerEntity source) {
        updateCountsItemUpdate(stack, 1);

        // advancements
        if (source != null) {
            if (stack.getItem() instanceof AtomItem)
                Criteria.BOHR_CRITERION.trigger(source, BohrCriterion.Type.ADD_ATOM);
            else
                Criteria.BOHR_CRITERION.trigger(source, BohrCriterion.Type.ADD_PARTICLE);
        }
    }

    private void onItemRemoved(ItemStack stack, @Nullable ServerPlayerEntity source) {
        updateCountsItemUpdate(stack, -1);

        // advancements
        if (source != null) {
            if (stack.getItem() instanceof AtomItem)
                Criteria.BOHR_CRITERION.trigger(source, BohrCriterion.Type.REMOVE_ATOM);
            else
                Criteria.BOHR_CRITERION.trigger(source, BohrCriterion.Type.REMOVE_PARTICLE);
        }
    }

    /**
     * Launches a subatomic particle in a random direction (as entity not as item).
     * Should only be used for subatomic particles, not atoms.
     */
    private void launchParticle(Item item) {
        // launch particle
        ParticleEntity entity = new ParticleEntity(world,
                BlockPos.ofFloored(getX(), getY() + getHeight() / 2f, getZ()), new ItemStack(item));
        // velocity chosen such that it launches up and around, but not too much at the ground
        Vec3d velocity = new Vec3d(0, 0.2, 0)
                .add(
                        this.random.nextTriangular(0, 1d) * 2,
                        this.random.nextTriangular(0, 1d) * 1,
                        this.random.nextTriangular(0, 1d) * 2
                ).normalize().multiply(ParticleEntity.DEFAULT_SPEED);
        entity.setVelocity(velocity);
        world.spawnEntity(entity);
    }

    /**
     * Try to Launch this particle
     * Will drop if GameRule doesn't allow projectile
     *
     * @param item : item to launch/drop
     */
    public void tryLaunchParticle(Item item) {
        if (world.getGameRules().getBoolean(MinelabsGameRules.BOHR_PROJECTILES)) {
            launchParticle(item);
        } else {
            dropStack(new ItemStack(item));
        }
    }

    private void decomposeAtom() {
        for (int p = 0; p < getProtons(); p++) tryLaunchParticle(Items.PROTON);
        for (int n = 0; n < getNeutrons(); n++) tryLaunchParticle(Items.NEUTRON);
        for (int e = 0; e < getElectrons(); e++) tryLaunchParticle(Items.ELECTRON);
        clear();
    }

    private void clear() {
        inventory.clear();
        // sets everything to zero
        updateCountsFromContent();
    }

    public ItemStack craftAtom() {
        return craftAtom(null);
    }

    /**
     * Tries to craft the atom and clear inventory on success. Otherwise, nothing changes and return empty stack.
     */
    public ItemStack craftAtom(@Nullable ServerPlayerEntity player) {
        ItemStack stack = getCraftableAtom();
        if (!stack.isEmpty()) {
            // Advancement: when atom actually crafted and not just inserted and extracted again
            if (player != null && inventory.size() > 1)
                Criteria.BOHR_CRITERION.trigger(player, BohrCriterion.Type.CRAFT_ATOM);

            clear();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    @Override
    public boolean canAvoidTraps() {
        // so it ignores tripwires and pressure plates.
        return true;
    }

    @Override
    public boolean canHit() {
        // on server this function is used for collision checks
        if (!world.isClient) return true;
        // on the client it is used for mining interaction
        return !isEmpty();
    }

    /**
     * Returning true prevents the attack from happening.
     */
    @Override
    public boolean handleAttack(Entity attacker) {
        return isEmpty();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.isRemoved() && !this.world.isClient) {
            if (source.getAttacker() instanceof ServerPlayerEntity player) {
                onHitByPlayer(player);
                return true;
            }
        }
        return false;
    }

    private void onHitByPlayer(ServerPlayerEntity player) {
        dropLastItem(player);
    }

    public ItemStack extractByRod(ServerPlayerEntity player, FishingBobberEntity bobber) {
        ItemStack stack = removeLastItem(player);

        if (stack.isEmpty() || world.isClient)
            return stack;

        // custom drop logic based on dropStack of entity.
        // We immediately add fishing hook pulling logic before entity is spawned so the info is synced with the client.
        ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), stack);
        ((FishingBobberEntityAccessor) bobber).invokePullHookedEntity(itemEntity);
        this.world.spawnEntity(itemEntity);

        return stack;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        // Remove other bohr blueprint entities already present.
        // We put this check here because the position needs to be known and it is loaded from nbt, for example when copied by structure block
        if (!world.isClient) {
            List<Entity> entities = world.getOtherEntities(this, getBoundingBox(), e -> e instanceof BohrBlueprintEntity);
            for (Entity entity : entities) {
                // move below map first so the bohr plate below won't be found and it doesn't destroy it.
                entity.setPosition(getX(), world.getBottomY() - 1, getZ());
                entity.discard();
            }
        }
    }

    /**
     * Inventory is saved on block entity. Less important (and more frequently changing info) is kept in the entity.
     * See `BohrBlueprintBlockEntity`
     */
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        // save inventory
        NbtList nbtList = new NbtList();
        for (ItemStack stack : inventory) {
            if (stack.isEmpty()) continue;
            nbtList.add(stack.writeNbt(new NbtCompound()));
        }
        nbt.put("Items", nbtList);

        nbt.putFloat("electronEjectProgress", electronEjectProgress);
        nbt.putFloat("integrity", getIntegrity());

        // The following information is not needed to encode the state and should not be read directly.
        // It is merely present to facilitate interactions with commands.
        getAtomConfig().getSymbol().ifPresent(symbol -> nbt.putString("atom", symbol));
        nbt.putInt("protons", getProtons());
        nbt.putInt("neutrons", getNeutrons());
        nbt.putInt("electrons", getElectrons());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        inventory.clear();
        if (nbt.contains("Items")) {
            // load inventory
            NbtList nbtList = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < nbtList.size(); i++) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                inventory.add(ItemStack.fromNbt(nbtCompound));
            }
            updateCountsFromContent();
        }

        if (nbt.contains("electronEjectProgress"))
            electronEjectProgress = nbt.getFloat("electronEjectProgress");

        if (nbt.contains("integrity"))
            setIntegrity(nbt.getFloat("integrity"));
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    /**
     * Sets the amount of protons, electrons and neutrons based on the inventory.
     */
    private void updateCountsFromContent() {
        int protons = 0, electrons = 0, neutrons = 0;
        for (ItemStack stack : inventory) {
            if (stack.isOf(Items.PROTON)) protons++;
            else if (stack.isOf(Items.ELECTRON)) electrons++;
            else if (stack.isOf(Items.NEUTRON)) neutrons++;
            else if (stack.isOf(Items.ANTI_PROTON)) protons--;
            else if (stack.isOf(Items.POSITRON)) electrons--;
            else if (stack.isOf(Items.ANTI_NEUTRON)) neutrons--;
            else if (stack.getItem() instanceof AtomItem atomItem) {
                protons += atomItem.getAtom().getAtomNumber();
                electrons += atomItem.getAtom().getAtomNumber();
                neutrons += atomItem.getAtom().getInitialNeutrons();
            } else {
                Minelabs.LOGGER.warn("Incompatible item found in bohr plate: " + stack);
            }
        }
        setAtomConfiguration(protons, neutrons, electrons);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);

        if (ATOM_CONFIGURATION.equals(data)) {
            compositionChanged();
        }
    }

    /**
     * Update atom and stability info only once.
     */
    private void compositionChanged() {
        // server only from here on
        if (world.isClient) return;

        if (!getAtomConfig().isNucleusDecomposing())
            setIntegrity(1f);

        // reset progress
        if (!getAtomConfig().isElectronDecomposing())
            electronEjectProgress = 1f;

        // set block state
        BohrBlueprintBlock.Status status = BohrBlueprintBlock.Status.EMPTY;
        if (!getCraftableAtom().isEmpty()) status = BohrBlueprintBlock.Status.CRAFTABLE;
        else if (!getAtomConfig().isStable()) status = BohrBlueprintBlock.Status.UNSTABLE;
        BohrBlueprintBlock.updateStatus(world, getBohrBlueprintPos(), status);
    }

    public AtomConfiguration getAtomConfig() {
        return dataTracker.get(ATOM_CONFIGURATION);
    }

    protected void setAtomConfiguration(int protons, int neutrons, int electrons) {
        setAtomConfiguration(new AtomConfiguration(protons, neutrons, electrons));
    }

    protected void setAtomConfiguration(AtomConfiguration atomConfig) {
        dataTracker.set(ATOM_CONFIGURATION, atomConfig);
    }

    public ItemStack getCraftableAtom() {
        return getAtomConfig().isStable() ? getAtomConfig().getAtomStack() : ItemStack.EMPTY;
    }

    public float getIntegrity() {
        return dataTracker.get(INTEGRITY);
    }

    protected int getProtons() {
        return getAtomConfig().getProtons();
    }

    protected int getElectrons() {
        return getAtomConfig().getElectrons();
    }

    protected int getNeutrons() {
        return getAtomConfig().getNeutrons();
    }

    private void setIntegrity(float value) {
        dataTracker.set(INTEGRITY, value);
    }

    private void decrementIntegrity() {
        decrementIntegrity(INTEGRITY_DECAY_STEP);
    }

    private void decrementIntegrity(float value) {
        setIntegrity(getIntegrity() - value);
    }

    private void setProtons(int value) {
        setAtomConfiguration(value, getNeutrons(), getElectrons());
    }

    private void setNeutrons(int value) {
        setAtomConfiguration(getProtons(), value, getElectrons());
    }

    private void setElectrons(int value) {
        setAtomConfiguration(getProtons(), getNeutrons(), value);
    }

    private void incrementProtons(int value) {
        setProtons(getProtons() + value);
    }

    private void incrementNeutrons(int value) {
        setNeutrons(getNeutrons() + value);
    }

    private void incrementElectrons(int value) {
        setElectrons(getElectrons() + value);
    }
}
