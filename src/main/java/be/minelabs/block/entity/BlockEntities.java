package be.minelabs.block.entity;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntities {
    public static final BlockEntityType<ErlenmeyerBlockEntity> ERLENMEYER_STAND_BLOCK_ENTITY;
    public static BlockEntityType<LewisBlockEntity> LEWIS_BLOCK_ENTITY;
    public static BlockEntityType<IonicBlockEntity> IONIC_BLOCK_ENTITY;
    public static BlockEntityType<ElectricFieldSensorBlockEntity> ELECTRIC_FIELD_SENSOR;
    public static BlockEntityType<QuantumFieldBlockEntity> QUANTUM_FIELD_BLOCK_ENTITY;

    public static BlockEntityType<LabChestBlockEntity> LAB_CHEST_BLOCK_ENTITY;

    public static BlockEntityType<MologramBlockEntity> MOLOGRAM_BLOCK_ENTITY;

    public static BlockEntityType<AtomicStorageBlockEntity> ATOMIC_STORAGE_BLOCK_ENTITY;


    static {
        IONIC_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Minelabs.MOD_ID, "ionic_block"),
                FabricBlockEntityTypeBuilder.create(IonicBlockEntity::new,
                        Blocks.IONIC_BLOCK).build(null));

        LEWIS_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create(LewisBlockEntity::new,
                        Blocks.LEWIS_BLOCK, Blocks.LAB_LEWIS).build(null), "lewis_block");

        ELECTRIC_FIELD_SENSOR = register(FabricBlockEntityTypeBuilder.create((p, s) ->
                new ElectricFieldSensorBlockEntity(ELECTRIC_FIELD_SENSOR, p, s),
                Blocks.ELECTRIC_FIELD_SENSOR_BLOCK).build(null), "electric_field_sensor");

        QUANTUM_FIELD_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create(QuantumFieldBlockEntity::new,
                        Blocks.ELECTRON_QUANTUMFIELD, Blocks.GLUON_QUANTUMFIELD,
                        Blocks.NEUTRINO_QUANTUMFIELD, Blocks.DOWNQUARK_QUANTUMFIELD,
                        Blocks.PHOTON_QUANTUMFIELD, Blocks.WEAK_BOSON_QUANTUMFIELD,
                        Blocks.UPQUARK_QUANTUMFIELD).build(null), "quantum_field_entity");

        LAB_CHEST_BLOCK_ENTITY = register(
                FabricBlockEntityTypeBuilder.create(LabChestBlockEntity::new,
                        Blocks.LAB_CABIN, Blocks.LAB_DRAWER).build(null), "lab_chest_block_entity");

        MOLOGRAM_BLOCK_ENTITY = register(FabricBlockEntityTypeBuilder.create(MologramBlockEntity::new,
                Blocks.MOLOGRAM_BLOCK).build(null), "mologram_block_entity");

        ERLENMEYER_STAND_BLOCK_ENTITY = register(FabricBlockEntityTypeBuilder.create(ErlenmeyerBlockEntity::new,
                Blocks.ERLENMEYER_STAND).build(), "erlenmeyer_stand_block_entity");

        ATOMIC_STORAGE_BLOCK_ENTITY = register(FabricBlockEntityTypeBuilder.create(AtomicStorageBlockEntity::new,
                Blocks.ATOMIC_STORAGE).build(null), "atomic_storage_block_entity");
    }

    /**
     * Register a BlockEntity
     * <p>
     *
     * @param blockEntityType : BlockEntityType Object to register
     * @param identifier      : String name of the Item
     * @return {@link Block}
     */
    private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType<T> blockEntityType, String identifier) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Minelabs.MOD_ID, identifier), blockEntityType);
    }

    /**
     * Main class method
     * Registers all BlocksEntities
     */
    public static void onInitialize() {
    }
}
