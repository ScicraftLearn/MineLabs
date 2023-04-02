package be.uantwerpen.minelabs.entity;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.item.Items;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.SpawnSettings;

import java.util.function.Predicate;

//import static be.uantwerpen.minelabs.block.Blocks.LEWIS_BLOCK;


public class Entities {
    // EntityTypes
    public static final EntityType<SubatomicParticleEntity> SUBATOMIC_PARTICLE_ENTITY_TYPE = register(FabricEntityTypeBuilder.<SubatomicParticleEntity>create(SpawnGroup.MISC, SubatomicParticleEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build(), "subatomic_particle_entity");

    public static final EntityType<BohrBlueprintEntity> BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE = register(FabricEntityTypeBuilder.<BohrBlueprintEntity>create(SpawnGroup.MISC, BohrBlueprintEntity::new)
            .dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeBlocks(10).disableSummon().fireImmune().build(), "bohr_blueprint_entity");

    public static final EntityType<EntropyCreeperEntity> ENTROPY_CREEPER = register(FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntropyCreeperEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.7f)).build(), "entropy_creeper");

    public static final EntityType<BalloonEntity> BALLOON = register(FabricEntityTypeBuilder.create(SpawnGroup.MISC, BalloonEntity::new)
            .dimensions(EntityDimensions.fixed(1.0f, 1.0f)).build(), "balloon");


    /**
     * Register a single entity
     * <p>
     *
     * @param entityType : EntityType to register
     * @param identifier : String name of the entity
     * @return registered EntityType
     */
    private static <T extends Entity> EntityType<T> register(EntityType<T> entityType, String identifier) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Minelabs.MOD_ID, identifier), entityType);
    }

    /**
     * Register a single block entity
     * <p>
     *
     * @param blockEntityType : BlockEntityType to register
     * @param identifier      : String name of the entity
     * @return registered BlockEntityType
     */
    private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType<T> blockEntityType, String identifier) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Minelabs.MOD_ID, identifier), blockEntityType);
    }

    /**
     * Modify the Entity spawns
     * <p>
     *
     * @param entityType  : EntityType to add Spawns for
     * @param selector:   Predicate BiomeSelection, what biome(s) the entity can spawn in
     * @param spawnEntry: no documentation found
     *                    <p>
     *                    While testing set Selector to BiomeSelectors.all(), this will spawn you Entity in "The End"/"Nether" when entering
     */
    private static void registerEntitySpawns(EntityType<?> entityType, Predicate<BiomeSelectionContext> selector, SpawnSettings.SpawnEntry spawnEntry) {
        BiomeModifications.create(Registry.ENTITY_TYPE.getId(entityType))
                .add(ModificationPhase.ADDITIONS, selector, context -> context.getSpawnSettings().addSpawn(entityType.getSpawnGroup(), spawnEntry));
    }

    /**
     * Main class method
     * Register All entities
     */
    public static void registerEntities() {
        Minelabs.LOGGER.info("registering entities");
        FabricDefaultAttributeRegistry.register(ENTROPY_CREEPER, EntropyCreeperEntity.createCreeperAttributes());
        registerEntitySpawns(ENTROPY_CREEPER, BiomeSelectors.foundInOverworld().or(BiomeSelectors.foundInTheNether()),
                new SpawnSettings.SpawnEntry(ENTROPY_CREEPER, 100, 0, 1));
        FabricDefaultAttributeRegistry.register(BALLOON, BalloonEntity.createMobAttributes());
    }
}
