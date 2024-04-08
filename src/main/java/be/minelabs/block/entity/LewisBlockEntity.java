package be.minelabs.block.entity;

import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.advancement.criterion.LCTCriterion;
import be.minelabs.network.NetworkingConstants;
import be.minelabs.recipe.lewis.LewisCraftingGrid;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import be.minelabs.inventory.OrderedInventory;
import be.minelabs.recipe.molecules.MoleculeGraph;
import be.minelabs.screen.LewisBlockScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class LewisBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    // slots 0-24
    private final LewisCraftingGrid craftingGrid = new LewisCraftingGrid(5, 5);

    // slots 0-8: atoms, slot 9: erlenmeyer, slot 10: output
    // combined with craftingGrid this gives slots 25-33: atoms, slot 34: erlenmeyer, slot 35: output
    private final OrderedInventory ioInventory = new OrderedInventory(11);

    //PropertyDelegate is an interface which is implemented inline here.
    //It can normally contain multiple integers as data identified by the index.
    private final PropertyDelegate propertyDelegate;
    //List of needed input ingredients; Synced by LewisDataPacket
    private DefaultedList<Ingredient> ingredients = DefaultedList.of();
    //private final RecipeManager.MatchGetter<LewisCraftingGrid, MoleculeRecipe> matchGetter;
    //Progress of the recipe; -1 means not started; Synced by propertyDelegate
    public int progress = 0;
    public int maxProgress = 23;
    //Density (amount) of items needed for the recipe;  Also used to tell the client a recipe is found;Synced by propertyDelegate
    private int density;
    //Current recipe selected; NOT synced
    private MoleculeRecipe currentRecipe;

    public LewisBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.LEWIS_BLOCK_ENTITY, pos, state);

        propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> LewisBlockEntity.this.progress; //progress of the recipe
                    case 1 -> LewisBlockEntity.this.maxProgress;
                    case 2 -> LewisBlockEntity.this.density; //Density (amount) of items needed for the recipe
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> LewisBlockEntity.this.progress = value;
                    case 1 -> LewisBlockEntity.this.maxProgress = value;
                    case 2 -> LewisBlockEntity.this.density = value;
                }
            }

            @Override
            public int size() {
                return 3;
            }
        };
    }

    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new LewisBlockScreenHandler(syncId, playerInventory, craftingGrid, ioInventory, propertyDelegate, pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.progress = nbt.getInt("pr");
        this.craftingGrid.readNbtList(nbt.getList("grid", NbtElement.COMPOUND_TYPE));
        this.ioInventory.readNbtList(nbt.getList("io_inv", NbtElement.COMPOUND_TYPE));
        this.density = nbt.getInt("dens");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("pr", this.progress);
        nbt.put("grid", craftingGrid.toNbtList());
        nbt.put("io_inv", ioInventory.toNbtList());
        nbt.putInt("dens", this.density);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return super.createNbt();
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        DynamicRegistryManager manager = world.getRegistryManager();
        if (hasRecipe() && canExport(manager) && containerCheck() && hasEnoughItems()) {
            progress++;
            markDirty();

            if (progress >= maxProgress) {
                // Craft
                craft(manager);
                resetProgress();
            }
        } else {
            resetProgress();
            advancementCheck(world.getRecipeManager());
        }
    }

    /**
     * Callback used for advancement
     * Don't use currentRecipe, it might trigger the advancement
     */
    private void advancementCheck(RecipeManager manager) {
        Optional<MoleculeRecipe> recipe = manager.getFirstMatch(MoleculeRecipe.MoleculeRecipeType.INSTANCE, craftingGrid, getWorld());
        if (recipe.isEmpty() && getWorld() instanceof ServerWorld serverWorld) {
            MoleculeGraph structure = craftingGrid.getPartialMolecule().getStructure();
            if (!structure.getVertices().isEmpty() && structure.getTotalOpenConnections() == 0 && structure.isConnectedManagerFunctieOmdatJoeyZaagtZoalsVaak()) {
                Criteria.LCT_CRITERION.trigger(serverWorld, getPos(), 5, c -> c.test(LCTCriterion.Type.UNKNOWN_MOLECULE));
            }
        }
    }

    /**
     * Craft the item
     * Remove ingredients (and container)
     * Add/create output stack
     *
     * @param manager : DynamicRegistryManager, pass through
     */
    private void craft(DynamicRegistryManager manager) {
        // TODO REFINE, DONT want to disable old working only add new (else if needs to change)
        //Remove actual ingredients
        for (int i = 0; i < ingredients.size(); i++) {
            AtomicStorageBlockEntity atomic_storage = getAtomicStorage();
            int count = currentRecipe.getDensity();
            if (atomic_storage != null) {
                count = atomic_storage.getInventory().removeStack(ingredients.get(i), count);
            }
            ioInventory.getStack(i).decrement(count);
        }
        // Remove a container item if needed
        if (currentRecipe.needsContainer()) {
            ioInventory.removeStack(9, 1);
        }
        // Update output stack
        ioInventory.setStack(10, new ItemStack(currentRecipe.getOutput(manager).getItem(),
                ioInventory.getStack(10).getCount() + currentRecipe.getOutput(manager).getCount()));

    }

    /**
     * If the recipe requires an Erlenmeyer make sure there is one.
     * Otherwise, allow crafting.
     *
     * @return boolean
     */
    private boolean containerCheck() {
        return hasContainer() && currentRecipe.needsContainer() || !currentRecipe.needsContainer();
    }

    /**
     * Check if the inventory has enough items to craft the output
     *
     * @return boolean
     */
    private boolean hasEnoughItems() {
        AtomicStorageBlockEntity atomic_storage = getAtomicStorage();
        // TODO REFINE, DONT want to disable old working only add new (else if needs to change)
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (atomic_storage != null) {
                if (!atomic_storage.getInventory().contains(ingredient, currentRecipe.getRequiredAmount(ingredient))) {
                    return false;
                }
            } else if (!ingredient.test(ioInventory.getStack(i)) || ioInventory.getStack(i).getCount() < currentRecipe.getDensity()) {
                return false;
            }
        }
        return true;
    }

    public AtomicStorageBlockEntity getAtomicStorage() {
        Direction direction = this.getCachedState().get(Properties.HORIZONTAL_FACING);
        for (int j = 0; j < 3; j++) {
            direction = direction.rotateYClockwise();
            if (getWorld().getBlockEntity(getPos().offset(direction)) instanceof AtomicStorageBlockEntity storage) {
                return storage;
            }
        }
        return null;
    }

    /**
     * Does the LCT have at least 1 container in slot 9
     *
     * @return boolean
     */
    private boolean hasContainer() {
        return !ioInventory.getStack(9).isEmpty() && ioInventory.getStack(9).getCount() >= 1;
    }

    /**
     * Check if we can Export to the output Slot (10)
     * Empty slot? or reached max stack size ?
     *
     * @return boolean
     */
    private boolean canExport(DynamicRegistryManager manager) {
        return ioInventory.getStack(10).isEmpty()
                || ioInventory.getStack(10).getCount() + currentRecipe.getOutput(manager).getCount() <= ioInventory.getStack(10).getMaxCount()
                && ioInventory.getStack(10).isOf(currentRecipe.getOutput(manager).getItem());
    }

    private boolean hasRecipe() {
        return currentRecipe != null;
    }

    private void resetProgress() {
        progress = 0;
        markDirty();
    }

    public LewisCraftingGrid getLewisCraftingGrid() {
        return craftingGrid;
    }

    public Inventory getIoInventory() {
        return ioInventory;
    }

    /**
     * Provides a buffer to {@link LewisBlockScreenHandler}, to get the position of the blockentity.
     * The blockentity is used to get all needed values.
     *
     * @param player the player that is opening the screen
     * @param buf    the packet buffer
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    //Reset the recipe
    public void resetRecipe() {
        this.currentRecipe = null;
        this.progress = 0;
        this.ingredients = DefaultedList.ofSize(0);
        this.density = 0;
        this.maxProgress = 23;
        markDirty();
    }

    public void updateRecipe() {
        getWorld().getRecipeManager().getFirstMatch(MoleculeRecipe.MoleculeRecipeType.INSTANCE, craftingGrid, getWorld())
                .ifPresentOrElse(moleculeRecipe -> {
                    if (moleculeRecipe != currentRecipe) {
                        // Different recipe
                        this.currentRecipe = moleculeRecipe;
                        this.progress = 0;
                        this.ingredients = moleculeRecipe.getIngredients();
                        this.density = moleculeRecipe.getDensity();
                        this.maxProgress = moleculeRecipe.getTime();
                        markDirty();
                        sendDataPacket();
                    }
                }, this::resetRecipe);
    }

    private void sendDataPacket() {
        if (getWorld().isClient) {
            return;
        }
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.ingredients.size());
        for (Ingredient ingredient : this.ingredients) {
            ingredient.write(buf);
        }

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, pos)) {
            ServerPlayNetworking.send(player, NetworkingConstants.LEWISDATASYNC, buf);
        }
    }

    public void setIngredients(DefaultedList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public MoleculeRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }
}
