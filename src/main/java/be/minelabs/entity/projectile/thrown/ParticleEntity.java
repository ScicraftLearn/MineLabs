package be.minelabs.entity.projectile.thrown;

import be.minelabs.advancement.criterion.CoulombCriterion;
import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.entity.Entities;
import be.minelabs.item.Items;
import be.minelabs.science.coulomb.CoulombResource;
import be.minelabs.science.coulomb.CoulombData;
import be.minelabs.sound.SoundEvents;
import be.minelabs.world.MinelabsGameRules;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleEntity extends ChargedEntity {

    private CoulombData data;

    public ParticleEntity(EntityType<? extends ParticleEntity> entityType, World world) {
        super(entityType, world);
    }

    public ParticleEntity(World world, BlockPos pos, ItemStack stack) {
        super(Entities.PARTICLE_ENTITY, world, pos, stack);
    }

    public ParticleEntity(LivingEntity owner, World world, ItemStack stack) {
        super(Entities.PARTICLE_ENTITY, owner, world, stack);
    }

    private void setData(String name) {
        if (name.contains("air")) {
            name = Items.ELECTRON.getTranslationKey();
        }
        setCustomName(Text.translatable(name));
        if (!world.isClient) {
            // Don't want to load "server data" on the client
            String[] split = name.split("\\.");
            this.data = CoulombResource.INSTANCE.getCoulombData(split[split.length - 1]);
            setCharge(data.charge);
        }
    }

    @Override
    public void setItem(ItemStack stack) {
        super.setItem(stack);
        setData(stack.getItem().getTranslationKey());
    }

    // Must call super.tick(), for field update
    @Override
    public void tick() {
        if (world.isClient) {
            super.tick();
            return;
        }

        addVelocity(getField().multiply(1 / data.mass));
        // TODO rework after actual movement :
        Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5, (condition) -> condition.test(CoulombCriterion.Type.MOVE));

        super.tick();
        tryDecay();
    }

    /**
     * Try to decay the entity
     * ONLY if the entity isn't stable
     * <p>
     * Called 20 times a second !
     */
    private void tryDecay() {
        if (!data.stable && world.getGameRules().getBoolean(MinelabsGameRules.ALLOW_DECAY)) {
            if (world.getRandom().nextFloat() < data.decay_chance) {
                ItemEntity item = new ItemEntity(world, getX(), getY() + .5, getZ(), getDecayStack());
                item.setVelocity(getVelocity().multiply(2));
                world.spawnEntity(item);
                Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5, (condition) -> condition.test(CoulombCriterion.Type.DECAY));
                playSound(SoundEvents.COULOMB_DECAY, 1f, 1f);
                if (data.shouldReplace()) {
                    setItem(data.getDecayReplacement());
                    return;
                }

                this.discard();
            }
        }
    }

    @Override
    public void onChargedEntityHit(ChargedEntity entity) {
        if (data.getAntiItem() != null && entity.getItem().isOf(data.getAntiItem())) {
            ItemScatterer.spawn(getWorld(), getX(), getY(), getZ(), getAnnihilationStack());
            ((ServerWorld) world).spawnParticles(ParticleTypes.FLASH, getX(), getY()+0.4f, getZ(), 1, 0f, 0f,0f, 0f);
            Criteria.COULOMB_FORCE_CRITERION.trigger((ServerWorld) world, getBlockPos(), 5,
                    (condition) -> condition.test(CoulombCriterion.Type.ANNIHILATE));
            playSound(SoundEvents.COULOMB_ANNIHILATE, 1f, 1f);
            this.discard();
            entity.discard();
        } else {
            super.onChargedEntityHit(entity);
        }
    }

    /**
     * Get the Stack that drops when the entity annihilates
     *
     * @return ItemStack
     */
    private ItemStack getAnnihilationStack() {
        return data.getAnnihilationDrop();
    }

    /**
     * Get the Stack that drops when the entity decay's
     *
     * @return ItemStack
     */
    private ItemStack getDecayStack() {
        return data.getDecayDrop();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        setData(nbt.getString("data"));
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("data", getStack().getItem().getTranslationKey());
    }
}
