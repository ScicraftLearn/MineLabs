package be.minelabs.block;

import be.minelabs.Minelabs;
import be.minelabs.block.blocks.*;
import be.minelabs.block.entity.BlockEntities;
import be.minelabs.block.entity.ChemicalFluidBlock;
import be.minelabs.fluid.Fluids;
import be.minelabs.science.Molecule;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class Blocks {

    //Atomic floor for atomic dimension
    public static final Block ATOM_FLOOR = register(new AtomicFloor(), "atomic_floor");

    //Portal block
    public static final Block PORTAL_BLOCK = register(new PortalBlock(FabricBlockSettings.of(Material.DECORATION)), "portal_block");

    public static final Block SALT_ORE = register(new ExperienceDroppingBlock(FabricBlockSettings.of(Material.STONE)
            .mapColor(MapColor.WHITE_GRAY).strength(3.0f, 3.0f).requiresTool(), UniformIntProvider.create(0, 3)), "salt/salt_ore");
    public static final Block DEEPSLATE_SALT_ORE = register(new ExperienceDroppingBlock(FabricBlockSettings.of(Material.STONE)
            .mapColor(MapColor.WHITE_GRAY).strength(4.5f, 3.0f).requiresTool(), UniformIntProvider.create(1, 4)), "salt/deepslate_salt_ore");
    public static final Block SALT_BLOCK = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE_GRAY).strength(2.0f)), "salt/salt_block");

    public static final Block SALT_WIRE = register(new SaltWireBlock(FabricBlockSettings.of(Material.DECORATION)
            .mapColor(MapColor.WHITE_GRAY).noCollision().breakInstantly().dynamicBounds()), "salt/salt_wire");

    public static final Block SALT_CRYSTAL = register(new AmethystClusterBlock(7, 3,
            FabricBlockSettings.of(Material.AMETHYST).nonOpaque().ticksRandomly()
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(1.5f).luminance((state) -> 5)), "salt/salt_crystal");
    public static final Block LARGE_SALT_CRYSTAL = register(new AmethystClusterBlock(5, 3,
            FabricBlockSettings.copy(SALT_CRYSTAL).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).luminance((state) -> 4)), "salt/large_salt_crystal");
    public static final Block MEDIUM_SALT_CRYSTAL = register(new AmethystClusterBlock(4, 3,
            FabricBlockSettings.copy(SALT_CRYSTAL).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).luminance((state) -> 2)), "salt/medium_salt_crystal");
    public static final Block SMALL_SALT_CRYSTAL = register(new AmethystClusterBlock(3, 4,
            FabricBlockSettings.copy(SALT_CRYSTAL).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).luminance((state) -> 1)), "salt/small_salt_crystal");

    public static final Block BUDDING_SALT_BLOCK = register(new BuddingSaltBlock(
            FabricBlockSettings.of(Material.AMETHYST).ticksRandomly().strength(1.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool()), "salt/budding_salt_block");

    public static final Block LAB_CABIN = register(new LabChestBlock(FabricBlockSettings
            .of(Material.STONE).mapColor(MapColor.GRAY).dynamicBounds().strength(2.0F).requiresTool()), "lab/lab_cabin");
    public static final Block LAB_DRAWER = register(new LabChestBlock(FabricBlockSettings
            .of(Material.STONE).mapColor(MapColor.GRAY).dynamicBounds().strength(2.0F).requiresTool()), "lab/lab_drawer");
    public static final Block LAB_SINK = register(new LabSinkBlock(FabricBlockSettings
            .of(Material.STONE).mapColor(MapColor.GRAY).dynamicBounds().strength(2.0F).requiresTool()), "lab/lab_sink");
    public static final Block LAB_CENTER = register(new LabCenterBlock(FabricBlockSettings
            .of(Material.STONE).mapColor(MapColor.GRAY).strength(2.0F).requiresTool()), "lab/lab_center");
    public static final Block LAB_CORNER = register(new LabCornerBlock(FabricBlockSettings
            .of(Material.STONE).mapColor(MapColor.GRAY).dynamicBounds().strength(2.0F).requiresTool()), "lab/lab_corner");

    public static final Block LAB_LEWIS = register(new LabLewisBlock(FabricBlockSettings
            .of(Material.METAL).mapColor(MapColor.GRAY).dynamicBounds().strength(2.0F).requiresTool()), "lab/lab_lewis");

    // Value of charge here will be used temporarily when the block is still 'fresh' at the server, before a reload
    public static final Block NEUTRINO = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "subatomic/neutrino");
    public static final Block ANTINEUTRINO = register(new Block(FabricBlockSettings.of(Material.WOOL)
            .mapColor(MapColor.WHITE).strength(2f).noCollision()), "subatomic/antineutrino");


    // Quantumfields
    public static final Block GLUON_QUANTUMFIELD = register(new PositionalQuantumFieldBlock(), "quantumfield/gluon_quantumfield");
    public static final Block UPQUARK_QUANTUMFIELD = register(new PositionalQuantumFieldBlock(), "quantumfield/upquark_quantumfield");
    public static final Block DOWNQUARK_QUANTUMFIELD = register(new PositionalQuantumFieldBlock(), "quantumfield/downquark_quantumfield");

    public static final Block PHOTON_QUANTUMFIELD = register(new QuantumfieldBlock(), "quantumfield/photon_quantumfield");
    public static final Block WEAK_BOSON_QUANTUMFIELD = register(new QuantumfieldBlock(), "quantumfield/weak_boson_quantumfield");
    public static final Block ELECTRON_QUANTUMFIELD = register(new QuantumfieldBlock(), "quantumfield/electron_quantumfield");
    public static final Block NEUTRINO_QUANTUMFIELD = register(new QuantumfieldBlock(), "quantumfield/neutrino_quantumfield");

    public static final Block TIME_FREEZE_BLOCK = register(new TimeFreezeBlock(FabricBlockSettings.of(Material.GLASS)
            .noCollision().strength(0.5f, 2.0f)), "time_freeze_block");
    public static final Block ELECTRIC_FIELD_SENSOR_BLOCK = register(new ElectricFieldSensorBlock(FabricBlockSettings.of(Material.METAL).noCollision().nonOpaque()), "electric_field_sensor");

    // Normal fire has luminance 15, soul fire 10 -> we choose 12 arbitrarily
    public static final Block GREEN_FIRE = register(new GreenFire(FabricBlockSettings.of(Material.FIRE, MapColor.EMERALD_GREEN).noCollision().breakInstantly().luminance(12).sounds(BlockSoundGroup.WOOL)), "fire/green_fire");

    public static final Block MOLOGRAM_BLOCK = register(new MologramBlock(FabricBlockSettings.of(Material.METAL).mapColor(MapColor.WHITE).strength(2f).nonOpaque().luminance(state -> state.get(MologramBlock.LIT) ? 8 : 0)), "mologram");
    public static final Block LEWIS_BLOCK = register(new LewisBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), "lewis_block");
    public static final Block IONIC_BLOCK = register(new IonicBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), "ionic_block");

    public static final Block ATOMIC_STORAGE = register(new AtomicStorageBlock(FabricBlockSettings.of(Material.STONE)
            .mapColor(MapColor.GRAY).dynamicBounds().strength(2.0F).requiresTool()), "atomic_storage");
    public static final Block HNO3 = register(new ChemicalFluidBlock(Fluids.STILL_HNO3, Molecule.HNO3), "chemical/hno3");
    public static final Block H2O = register(new ChemicalFluidBlock(Fluids.STILL_H2O, Molecule.H2O), "chemical/h2o");
    public static final Block CS2 = register(new ChemicalFluidBlock(Fluids.STILL_CS2, Molecule.CS2), "chemical/cs2");
    public static final Block CCL4 = register(new ChemicalFluidBlock(Fluids.STILL_CCl4, Molecule.CCL4), "chemical/ccl4");
    public static final Block PCL3 = register(new ChemicalFluidBlock(Fluids.STILL_PCl3, Molecule.PCL3), "chemical/pcl3");
    public static final Block SCL2 = register(new ChemicalFluidBlock(Fluids.STILL_SCl2, Molecule.SCL2), "chemical/scl2");
    public static final Block NCL3 = register(new ChemicalFluidBlock(Fluids.STILL_NCl3, Molecule.NCL3), "chemical/ncl3");
    public static final Block HCN = register(new ChemicalFluidBlock(Fluids.STILL_HCN, Molecule.HCN), "chemical/hcn");
    public static final Block CH4O = register(new ChemicalFluidBlock(Fluids.STILL_CH4O, Molecule.CH4O), "chemical/ch4o");
    public static final Block SICL4 = register(new ChemicalFluidBlock(Fluids.STILL_SiCl4, Molecule.SICL4), "chemical/sicl4");
    public static final BohrBlueprintBlock BOHR_BLUEPRINT = register(new BohrBlueprintBlock(), "bohr_block");

    public static final Block ERLENMEYER_STAND = register(new ErlenmeyerBlock(
            FabricBlockSettings.of(Material.GLASS).strength(0.75f)), "lab/erlenmeyer_stand");
    public static final Block MICROSCOPE = register(new MicroscopeBlock(
            FabricBlockSettings.of(Material.METAL).strength(1f).luminance(6)), "lab/microscope");
    public static final Block TUBERACK = register(new TubeRackBlock(
            FabricBlockSettings.of(Material.METAL).strength(1f)), "lab/tuberack");
    public static final Block BURNER = register(new BurnerBlock(FabricBlockSettings.of(Material.METAL)
            .strength(1f).luminance(state -> state.get(Properties.LIT) ? 8 : 0)), "lab/burner");

    /**
     * Register a Block
     * <p>
     *
     * @param block      : Block Object to register
     * @param identifier : String name of the Item
     * @return {@link Block}
     */
    private static <T extends Block> T register(T block, String identifier) {
        return Registry.register(Registries.BLOCK, new Identifier(Minelabs.MOD_ID, identifier), block);
    }

    /**
     * Main class method
     * Registers all Blocks
     */
    public static void onInitialize() {
        BlockEntities.onInitialize();
        ExtraDispenserBehavior.onInitialize();
    }
}