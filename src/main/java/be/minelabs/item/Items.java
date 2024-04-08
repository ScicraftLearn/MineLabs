package be.minelabs.item;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.entity.Entities;
import be.minelabs.fluid.Fluids;
import be.minelabs.item.items.*;
import be.minelabs.science.Atom;
import be.minelabs.science.Molecule;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Items {
    public static final Item ATOM_FLOOR = register(new BlockItem((Blocks.ATOM_FLOOR), new FabricItemSettings()), "atomic_floor");

    // Items
    public static final Item ENTROPY_CREEPER_SPAWN_EGG = register(new SpawnEggItem(Entities.ENTROPY_CREEPER,
            0xbb64e1, 0x5d0486, new FabricItemSettings()), "entropy_creeper_spawn_egg");

    public static final Item LASERTOOL_IRON = register(new LaserTool(2.5f, -2.4f, ToolMaterials.IRON, new Item.Settings()), "lasertool_iron");
    public static final Item LASERTOOL_GOLD = register(new LaserTool(2.5f, -2.4f, ToolMaterials.GOLD, new Item.Settings()), "lasertool_gold");
    public static final Item LASERTOOL_DIAMOND = register(new LaserTool(2.5f, -2.4f, ToolMaterials.DIAMOND, new Item.Settings()), "lasertool_diamond");

    public static final Item SALT = register(new FireReactionBlockItem(Blocks.SALT_WIRE,
            new FabricItemSettings().maxCount(64), 4, "NaCl"), "salt/salt");

    public static final Item SALT_SHARD = register(new Item(
            new FabricItemSettings().maxCount(64)), "salt/salt_shard");

    public static final Item SALT_ORE = register(new BlockItem(Blocks.SALT_ORE,
            new FabricItemSettings().maxCount(64)), "salt/salt_ore");

    public static final Item DEEPSLATE_SALT_ORE = register(new BlockItem(Blocks.DEEPSLATE_SALT_ORE,
            new FabricItemSettings().maxCount(64)), "salt/deepslate_salt_ore");

    public static final Item SALT_BLOCK = register(new BlockItem(Blocks.SALT_BLOCK,
            new FabricItemSettings().maxCount(64)), "salt/salt_block");

    public static final Item SALT_CRYSTAL = register(new BlockItem(Blocks.SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64)), "salt/salt_crystal");

    public static final Item SMALL_SALT_CRYSTAL = register(new BlockItem(Blocks.SMALL_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64)), "salt/small_salt_crystal");

    public static final Item MEDIUM_SALT_CRYSTAL = register(new BlockItem(Blocks.MEDIUM_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64)), "salt/medium_salt_crystal");

    public static final Item LARGE_SALT_CRYSTAL = register(new BlockItem(Blocks.LARGE_SALT_CRYSTAL,
            new FabricItemSettings().maxCount(64)), "salt/large_salt_crystal");

    public static final Item BUDDING_SALT_BLOCK = register(new BlockItem(Blocks.BUDDING_SALT_BLOCK,
            new FabricItemSettings().maxCount(64)), "salt/budding_salt_block");

    public static final Item SAFETY_GLASSES = register(new ArmorItem(ArmorMaterials.CLOTH, ArmorItem.Type.HELMET,
            new FabricItemSettings()), "lab/safety_glasses");
    // TODO CHANGE ARMOR MAT (different texture on player's head)
    public static final Item FORCE_GLASSES = register(new ArmorItem(ArmorMaterials.CLOTH, ArmorItem.Type.HELMET,
            new FabricItemSettings()), "lab/force_glasses");

   /* public static final Item LAB_COAT = register(new ArmorItem(ArmorMaterials.CLOTH, EquipmentSlot.CHEST,
            new FabricItemSettings()), "lab/lab_coat");*/

    public static final Item LAB_COAT = register(new LabCoatArmorItem(ArmorMaterials.CLOTH, ArmorItem.Type.CHESTPLATE,
            new FabricItemSettings()), "lab/lab_coat");

    //TODO ADD PANTS? AND BOOTS ?

    public static final Item LAB_CABIN = register(new BlockItem(Blocks.LAB_CABIN,
            new FabricItemSettings()), "lab/lab_cabin");

    public static final Item LAB_DRAWER = register(new BlockItem(Blocks.LAB_DRAWER,
            new FabricItemSettings()), "lab/lab_drawer");

    public static final Item LAB_SINK = register(new BlockItem(Blocks.LAB_SINK,
            new FabricItemSettings()), "lab/lab_sink");

    public static final Item LAB_CENTER = register(new BlockItem(Blocks.LAB_CENTER,
            new FabricItemSettings()), "lab/lab_center");

    public static final Item LAB_CORNER = register(new BlockItem(Blocks.LAB_CORNER,
            new FabricItemSettings()), "lab/lab_corner");

    public static final Item LAB_LEWIS = register(new BlockItem(Blocks.LAB_LEWIS,
            new FabricItemSettings()), "lab/lab_lewis");


    public static final Item MICROSCOPE = register(new BlockItem(Blocks.MICROSCOPE,
            new FabricItemSettings().maxCount(1)), "lab/microscope");

    public static final Item TUBERACK = register(new BlockItem(Blocks.TUBERACK,
            new FabricItemSettings().maxCount(1)), "lab/tuberack");
    public static final Item LENS = register(new Item(
            new FabricItemSettings().maxCount(1)), "lab/lens");

    public static final Item BIG_LENS = register(new Item(
            new FabricItemSettings().maxCount(1)), "lab/big_lens");

    public static final Item BURNER = register(new BlockItem(Blocks.BURNER,
            new FabricItemSettings()), "lab/burner");

    public static final Item MAGNET = register(new MagnetItem(
            new FabricItemSettings().maxCount(1)), "magnet");

    public static final Item POCKET_HOLE = register(new BlackHoleItem(
            new FabricItemSettings().maxCount(1)), "pocket_hole");

    public static final Item LITHIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 1, "LiCl"), "dust/lithium_chloride_dust");

    public static final Item STRONTIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 2, "SrCl2"), "dust/strontium_chloride_dust");

    public static final Item STRONTIUM_NITRATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 2, "SrN2O6"), "dust/strontium_nitrate_dust"); // Sr(NO3)2

    public static final Item CALCIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 3, "CaCl2"), "dust/calcium_chloride_dust");

    public static final Item SODIUM_CARBONATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 4, "Na2CO3"), "dust/sodium_carbonate_dust");

    public static final Item BORAX_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 5, "borax"), "dust/borax_dust");

    public static final Item COPPER_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 6, "CuSO4"), "dust/copper_sulfate_dust");

    public static final Item BORIC_ACID = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 6, "BH3O3"), "erlenmeyer/erlenmeyer_boric_acid");

    public static final Item COPPER_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 7, "CuCl2"), "dust/copper_chloride_dust");

    public static final Item POTASSIUM_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 8, "K2SO4"), "dust/potassium_sulfate_dust");

    public static final Item POTASSIUM_NITRATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 8, "KNO3"), "dust/potassium_nitrate_dust");

    public static final Item POTASSIUM_CHLORIDE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 9, "KCl"), "dust/potassium_chloride_dust");

    public static final Item MAGNESIUM_SULFATE_DUST = register(new FireReactionItem(new FabricItemSettings()
            .maxCount(64), 10, "MgSO4"), "dust/magnesium_sulfate_dust");

    public static final Item ASH_DUST = register(new Item(new FabricItemSettings()
            .maxCount(64)), "dust/ash_dust");

    public static final Item DUST_ALH3 = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "AlH3"), "dust/aluminium_hydride_dust");

    public static final Item DUST_SIO2 = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "SiO2"), "dust/silicon_dioxide_dust");

    public static final Item DUST_AlCl3 = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "AlCl3"), "dust/aluminium_trichloride_dust");

    public static final Item DUST_BN = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "BN"), "dust/boron_nitride_dust");

    public static final Item DUST_BEO = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "BeO"), "dust/beryllium_oxide_dust");

    public static final Item DUST_SIC = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "SiC"), "dust/silicon_carbide_dust");

    public static final Item DUST_ALN = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "AlN"), "dust/aluminium_nitride_dust");

    public static final Item DUST_MGO = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "MgO"), "dust/magnesium_oxide_dust");

    public static final Item DUST_SO3 = register(new MoleculeItem(new FabricItemSettings()
            .maxCount(64), "SO3"), "dust/sulfur_trioxide_dust");

    // Items > Atoms
    public static final List<AtomItem> ATOMS = Arrays.stream(Atom.values()).map(Items::registerAtom).toList();

    // Items > Bond to display in LCT (internal)
    public static final Item BOND = register(new Item(new Item.Settings()), "bond");
    public static final Item VALENCEE = register(new Item(new Item.Settings()), "valence_electrons");

    // Items > Electric field
    public static final Item TIME_FREEZE_BLOCK = register(new BlockItem(Blocks.TIME_FREEZE_BLOCK, new FabricItemSettings()), "time_freeze_block");

    // Items > Elementary particles

    public static final Item UPQUARK_RED = register(new QuarkItem(new FabricItemSettings()), "subatomic/upquark_red");
    public static final Item UPQUARK_GREEN = register(new QuarkItem(new FabricItemSettings()), "subatomic/upquark_green");
    public static final Item UPQUARK_BLUE = register(new QuarkItem(new FabricItemSettings()), "subatomic/upquark_blue");
    public static final Item ANTI_UPQUARK_RED = register(new QuarkItem(new FabricItemSettings()), "subatomic/anti_upquark_red");
    public static final Item ANTI_UPQUARK_GREEN = register(new QuarkItem(new FabricItemSettings()), "subatomic/anti_upquark_green");
    public static final Item ANTI_UPQUARK_BLUE = register(new QuarkItem(new FabricItemSettings()), "subatomic/anti_upquark_blue");
    public static final Item DOWNQUARK_RED = register(new QuarkItem(new FabricItemSettings()), "subatomic/downquark_red");
    public static final Item DOWNQUARK_GREEN = register(new QuarkItem(new FabricItemSettings()), "subatomic/downquark_green");
    public static final Item DOWNQUARK_BLUE = register(new QuarkItem(new FabricItemSettings()), "subatomic/downquark_blue");
    public static final Item ANTI_DOWNQUARK_RED = register(new QuarkItem(new FabricItemSettings()), "subatomic/anti_downquark_red");
    public static final Item ANTI_DOWNQUARK_GREEN = register(new QuarkItem(new FabricItemSettings()), "subatomic/anti_downquark_green");
    public static final Item ANTI_DOWNQUARK_BLUE = register(new QuarkItem(new FabricItemSettings()), "subatomic/anti_downquark_blue");

    public static final Item GLUON = register(new Item(new FabricItemSettings()), "subatomic/gluon");

    public static final Item ELECTRON = register(new ChargedItem(new FabricItemSettings()), "subatomic/electron");
    public static final Item POSITRON = register(new ChargedItem(new FabricItemSettings()), "subatomic/positron");
    public static final Item PHOTON = register(new Item(new FabricItemSettings()), "subatomic/photon");

    public static final Item NEUTRINO = register(new BlockItem(Blocks.NEUTRINO, new FabricItemSettings()), "subatomic/neutrino");
    public static final Item ANTINEUTRINO = register(new BlockItem(Blocks.ANTINEUTRINO, new FabricItemSettings()), "subatomic/antineutrino");
    public static final Item WEAK_BOSON = register(new ChargedItem(new FabricItemSettings()), "subatomic/weak_boson");

    public static final Item PROTON = register(new ChargedItem(new FabricItemSettings()), "subatomic/proton");
    public static final Item ANTI_PROTON = register(new ChargedItem(new FabricItemSettings()), "subatomic/anti_proton");
    public static final Item NEUTRON = register(new ChargedItem(new FabricItemSettings()), "subatomic/neutron");
    public static final Item ANTI_NEUTRON = register(new ChargedItem(new FabricItemSettings()), "subatomic/anti_neutron");
    public static final Item PION_NUL = register(new ChargedItem(new FabricItemSettings()), "subatomic/pion_nul");
    public static final Item PION_MINUS = register(new ChargedItem(new FabricItemSettings()), "subatomic/pion_minus");
    public static final Item PION_PLUS = register(new ChargedItem(new FabricItemSettings()), "subatomic/pion_plus");


    // Items > Quantum fields
    public static final Item UPQUARK_QUANTUMFIELD = register(new BlockItem(Blocks.UPQUARK_QUANTUMFIELD, new FabricItemSettings()), "quantumfield/upquark_quantumfield");
    public static final Item DOWNQUARK_QUANTUMFIELD = register(new BlockItem(Blocks.DOWNQUARK_QUANTUMFIELD, new FabricItemSettings()), "quantumfield/downquark_quantumfield");
    public static final Item GLUON_QUANTUMFIELD = register(new BlockItem(Blocks.GLUON_QUANTUMFIELD, new FabricItemSettings()), "quantumfield/gluon_quantumfield");
    public static final Item ELECTRON_QUANTUMFIELD = register(new BlockItem(Blocks.ELECTRON_QUANTUMFIELD, new FabricItemSettings()), "quantumfield/electron_quantumfield");
    public static final Item PHOTON_QUANTUMFIELD = register(new BlockItem(Blocks.PHOTON_QUANTUMFIELD, new FabricItemSettings()), "quantumfield/photon_quantumfield");
    public static final Item NEUTRINO_QUANTUMFIELD = register(new BlockItem(Blocks.NEUTRINO_QUANTUMFIELD, new FabricItemSettings()), "quantumfield/neutrino_quantumfield");
    public static final Item WEAK_BOSON_QUANTUMFIELD = register(new BlockItem(Blocks.WEAK_BOSON_QUANTUMFIELD, new FabricItemSettings()), "quantumfield/weak_boson_quantumfield");


    public static final Item CHARGED_POINT = register(new ChargedPointItem(new FabricItemSettings()), "charged_point");
    public static final Item ELECTRIC_FIELD_SENSOR = register(new BlockItem(Blocks.ELECTRIC_FIELD_SENSOR_BLOCK, new FabricItemSettings()), "electric_field_sensor");

    public static final Item FORCE_COMPASS = register(new ForceCompassItem(new FabricItemSettings().maxCount(1)),
            "force_compass");

    public static final Item MOLOGRAM = register(new BlockItem(Blocks.MOLOGRAM_BLOCK, new FabricItemSettings()), "mologram");

    public static final Item BALLOON = register(new BalloonItem(new Item.Settings().maxCount(1)), "balloon");

    public static final Item BOHR_BLUEPRINT = register(new BlockItem(Blocks.BOHR_BLUEPRINT, new Item.Settings()), "bohr_block");

    public static final Item LEWIS_BLOCK_ITEM = register(new BlockItem(Blocks.LEWIS_BLOCK, new Item.Settings()), "lewis_block");
    public static final Item IONIC_BLOCK_ITEM = register(new BlockItem(Blocks.IONIC_BLOCK, new Item.Settings()), "ionic_block");

    // Erlenmeyer
    public static final Item ERLENMEYER_STAND = register(new BlockItem(Blocks.ERLENMEYER_STAND,
            new FabricItemSettings()), "lab/erlenmeyer_stand");
    public static final Item ERLENMEYER = register(new ErlenmeyerItem(net.minecraft.fluid.Fluids.EMPTY,
            new Item.Settings(), ""), "erlenmeyer/erlenmeyer");

    public static final Item ERLENMEYER_O2 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.O2), "erlenmeyer/erlenmeyer_o2");
    public static final Item ERLENMEYER_N2 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.N2), "erlenmeyer/erlenmeyer_n2");
    public static final Item ERLENMEYER_CH4 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.CH4), "erlenmeyer/erlenmeyer_ch4");
    public static final Item ERLENMEYER_H2 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.H2), "erlenmeyer/erlenmeyer_h2");
    public static final Item ERLENMEYER_NO = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.NO), "erlenmeyer/erlenmeyer_no");
    public static final Item ERLENMEYER_NO2 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.NO2), "erlenmeyer/erlenmeyer_no2");
    public static final Item ERLENMEYER_Cl2 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.CL2), "erlenmeyer/erlenmeyer_cl2");
    public static final Item ERLENMEYER_CO2 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.CO2), "erlenmeyer/erlenmeyer_co2");
    public static final Item ERLENMEYER_CO = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.CO), "erlenmeyer/erlenmeyer_co");
    public static final Item ERLENMEYER_NH3 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.NH3), "erlenmeyer/erlenmeyer_nh3");
    public static final Item ERLENMEYER_N2O = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.N2O), "erlenmeyer/erlenmeyer_n2o");
    public static final Item ERLENMEYER_HCl = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.HCL), "erlenmeyer/erlenmeyer_hcl");
    public static final Item ERLENMEYER_He = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.HE), "erlenmeyer/erlenmeyer_he");
    public static final Item ERLENMEYER_Ne = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.NE), "erlenmeyer/erlenmeyer_ne");
    public static final Item ERLENMEYER_Ar = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.AR), "erlenmeyer/erlenmeyer_ar");
    public static final Item ERLENMEYER_CL2O = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.CL2O), "erlenmeyer/erlenmeyer_cl2o");
    public static final Item ERLENMEYER_CH4S = register(new GasPotion(
            new FabricItemSettings().recipeRemainder(ERLENMEYER), Molecule.CH4S), "erlenmeyer/erlenmeyer_methanethiol");
    public static final Item ERLENMEYER_CH2O = register(new GasPotion(
            new FabricItemSettings().recipeRemainder(ERLENMEYER), Molecule.CH2O), "erlenmeyer/erlenmeyer_formaldehyde");
    public static final Item ERLENMEYER_H2CO3 = register(new GasPotion(
            new FabricItemSettings().recipeRemainder(ERLENMEYER), Molecule.H2CO3), "erlenmeyer/erlenmeyer_carbonic_acid");
    public static final Item ERLENMEYER_BH3 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.BH3), "erlenmeyer/erlenmeyer_bh3");
    public static final Item ERLENMEYER_HF = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.HF), "erlenmeyer/erlenmeyer_hf");
    public static final Item ERLENMEYER_SIH4 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.SIH4), "erlenmeyer/erlenmeyer_sih4");
    public static final Item ERLENMEYER_PH3 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.PH3), "erlenmeyer/erlenmeyer_ph3");
    public static final Item ERLENMEYER_H2S = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.H2S), "erlenmeyer/erlenmeyer_h2s");
    public static final Item ERLENMEYER_CF4 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.CF4), "erlenmeyer/erlenmeyer_cf4");
    public static final Item ERLENMEYER_BF3 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.BF3), "erlenmeyer/erlenmeyer_bf3");
    public static final Item ERLENMEYER_BCL3 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.BCL3), "erlenmeyer/erlenmeyer_bcl3");
    public static final Item ERLENMEYER_SO2 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.SO2), "erlenmeyer/erlenmeyer_so2");
    public static final Item ERLENMEYER_CLF = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.CLF), "erlenmeyer/erlenmeyer_clf");
    public static final Item ERLENMEYER_F2 = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.F2), "erlenmeyer/erlenmeyer_f2");

    public static final Item ERLENMEYER_C2H6O = register(new GasPotion(
            new Item.Settings().recipeRemainder(ERLENMEYER), Molecule.C2H6O), "erlenmeyer/erlenmeyer_ethanol");

    public static final Item ERLENMEYER_HNO3 = register(new ErlenmeyerItem(
            Fluids.STILL_HNO3, new Item.Settings().recipeRemainder(ERLENMEYER), "HNO3"), "erlenmeyer/erlenmeyer_hno3");
    public static final Item ERLENMEYER_H2O = register(new ErlenmeyerItem(
            Fluids.STILL_H2O, new Item.Settings().recipeRemainder(ERLENMEYER), "H2O"), "erlenmeyer/erlenmeyer_h2o");
    public static final Item ERLENMEYER_NCL3 = register(new ErlenmeyerItem(
            Fluids.STILL_NCl3, new Item.Settings().recipeRemainder(ERLENMEYER), "NCl3"), "erlenmeyer/erlenmeyer_ncl3");
    public static final Item ERLENMEYER_CS2 = register(new ErlenmeyerItem(
            Fluids.STILL_CS2, new Item.Settings().recipeRemainder(ERLENMEYER), "CS2"), "erlenmeyer/erlenmeyer_cs2");
    public static final Item ERLENMEYER_CCL4 = register(new ErlenmeyerItem(
            Fluids.STILL_CCl4, new Item.Settings().recipeRemainder(ERLENMEYER), "CCl4"), "erlenmeyer/erlenmeyer_ccl4");
    public static final Item ERLENMEYER_PCl3 = register(new ErlenmeyerItem(
            Fluids.STILL_PCl3, new Item.Settings().recipeRemainder(ERLENMEYER), "PCl3"), "erlenmeyer/erlenmeyer_pcl3");
    public static final Item ERLENMEYER_SCl2 = register(new ErlenmeyerItem(
            Fluids.STILL_SCl2, new Item.Settings().recipeRemainder(ERLENMEYER), "SCl2"), "erlenmeyer/erlenmeyer_scl2");
    public static final Item ERLENMEYER_HCN = register(new ErlenmeyerItem(
            Fluids.STILL_HCN, new Item.Settings().recipeRemainder(ERLENMEYER), "HCN"), "erlenmeyer/erlenmeyer_hcn");
    public static final Item ERLENMEYER_CH4O = register(new ErlenmeyerItem(
            Fluids.STILL_CH4O, new Item.Settings().recipeRemainder(ERLENMEYER), "CH4O"), "erlenmeyer/erlenmeyer_ch4o");
    public static final Item ERLENMEYER_SICL4 = register(new ErlenmeyerItem(
            Fluids.STILL_SiCl4, new Item.Settings().recipeRemainder(ERLENMEYER), "SiCl4"), "erlenmeyer/erlenmeyer_sicl4");


    /**
     * Register an Item
     */
    private static <T extends Item> T register(T item, String identifier) {
        return register(item, new Identifier(Minelabs.MOD_ID, identifier));
    }

    private static <T extends Item> T register(T item, Identifier identifier) {
        return Registry.register(Registries.ITEM, identifier, item);
    }

    private static AtomItem registerAtom(Atom atom) {
        AtomItem item = register(new AtomItem(new Item.Settings(), atom), atom.getItemId());
        atom.setItem(item);
        return item;
    }

    /**
     * Main class method<br>
     * Registers all (Block)Items
     */
    public static void onInitialize() {
        ItemGroups.onInitialize();
    }
}
