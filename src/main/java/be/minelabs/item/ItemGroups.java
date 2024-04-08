package be.minelabs.item;

import be.minelabs.Minelabs;
import be.minelabs.science.Atom;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroups {
    // Hard-coded ItemGroup, update from 19.3 allows to use ItemGroupEvents to extend ItemGroups. Currently, only
    //  addition by had is allowed or automatic addition (in Items.Settings). You cant combine both because of the
    //  implementation within the Fabric-API.
    public static final ItemGroup MINELABS = FabricItemGroup.builder(
                    new Identifier(Minelabs.MOD_ID, "minelabs"))
            .icon(() -> new ItemStack(Items.MOLOGRAM))
            .entries((displayContext, entries) -> {
                entries.add(new ItemStack(Items.ATOM_FLOOR));
                entries.add(new ItemStack(Items.SAFETY_GLASSES));
                entries.add(new ItemStack(Items.FORCE_GLASSES));
                entries.add(new ItemStack(Items.LAB_COAT));
                entries.add(new ItemStack(Items.LAB_CABIN));
                entries.add(new ItemStack(Items.LAB_DRAWER));
                entries.add(new ItemStack(Items.LAB_SINK));
                entries.add(new ItemStack(Items.LAB_LEWIS));
                entries.add(new ItemStack(Items.LAB_CENTER));
                entries.add(new ItemStack(Items.LAB_CORNER));
                entries.add(new ItemStack(Items.MICROSCOPE));
                entries.add(new ItemStack(Items.BURNER));
                entries.add(new ItemStack(Items.TUBERACK));
                entries.add(new ItemStack(Items.LENS));
                entries.add(new ItemStack(Items.BIG_LENS));
                entries.add(new ItemStack(Items.TIME_FREEZE_BLOCK));
                entries.add(new ItemStack(Items.ELECTRIC_FIELD_SENSOR));
                entries.add(new ItemStack(Items.FORCE_COMPASS));
                entries.add(new ItemStack(Items.MOLOGRAM));
                entries.add(new ItemStack(Items.BALLOON));
                entries.add(new ItemStack(Items.BOHR_BLUEPRINT));
                entries.add(new ItemStack(Items.LEWIS_BLOCK_ITEM));
                entries.add(new ItemStack(Items.IONIC_BLOCK_ITEM));
                entries.add(new ItemStack(Items.LASERTOOL_IRON));
                entries.add(new ItemStack(Items.LASERTOOL_GOLD));
                entries.add(new ItemStack(Items.LASERTOOL_DIAMOND));
                entries.add(new ItemStack(Items.ENTROPY_CREEPER_SPAWN_EGG));
                entries.add(new ItemStack(Items.MAGNET));
                entries.add(new ItemStack(Items.POCKET_HOLE));
            })
            .build();

    public static final ItemGroup CHEMICALS = FabricItemGroup.builder(
                    new Identifier(Minelabs.MOD_ID, "chemicals"))
            .icon(() -> new ItemStack(Items.ERLENMEYER))
            .entries((displayContext, entries) -> {
                entries.add(new ItemStack(Items.SALT));
                entries.add(new ItemStack(Items.SALT_SHARD));
                entries.add(new ItemStack(Items.SALT_ORE));
                entries.add(new ItemStack(Items.DEEPSLATE_SALT_ORE));
                entries.add(new ItemStack(Items.SALT_BLOCK));
                entries.add(new ItemStack(Items.SALT_CRYSTAL));
                entries.add(new ItemStack(Items.SMALL_SALT_CRYSTAL));
                entries.add(new ItemStack(Items.MEDIUM_SALT_CRYSTAL));
                entries.add(new ItemStack(Items.LARGE_SALT_CRYSTAL));
                entries.add(new ItemStack(Items.BUDDING_SALT_BLOCK));
                entries.add(new ItemStack(Items.LITHIUM_CHLORIDE_DUST));
                entries.add(new ItemStack(Items.STRONTIUM_CHLORIDE_DUST));
                entries.add(new ItemStack(Items.STRONTIUM_NITRATE_DUST));
                entries.add(new ItemStack(Items.CALCIUM_CHLORIDE_DUST));
                entries.add(new ItemStack(Items.SODIUM_CARBONATE_DUST));
                entries.add(new ItemStack(Items.BORAX_DUST));
                entries.add(new ItemStack(Items.COPPER_SULFATE_DUST));
                entries.add(new ItemStack(Items.BORIC_ACID));
                entries.add(new ItemStack(Items.COPPER_CHLORIDE_DUST));
                entries.add(new ItemStack(Items.POTASSIUM_NITRATE_DUST));
                entries.add(new ItemStack(Items.POTASSIUM_CHLORIDE_DUST));
                entries.add(new ItemStack(Items.POTASSIUM_SULFATE_DUST));
                entries.add(new ItemStack(Items.MAGNESIUM_SULFATE_DUST));
                entries.add(new ItemStack(Items.ASH_DUST));
                entries.add(new ItemStack(Items.DUST_ALH3));
                entries.add(new ItemStack(Items.DUST_SIO2));
                entries.add(new ItemStack(Items.DUST_AlCl3));
                entries.add(new ItemStack(Items.DUST_BN));
                entries.add(new ItemStack(Items.DUST_BEO));
                entries.add(new ItemStack(Items.DUST_SIC));
                entries.add(new ItemStack(Items.DUST_ALN));
                entries.add(new ItemStack(Items.DUST_MGO));
                entries.add(new ItemStack(Items.DUST_SO3));
                entries.add(new ItemStack(Items.ERLENMEYER));
                entries.add(new ItemStack(Items.ERLENMEYER_O2));
                entries.add(new ItemStack(Items.ERLENMEYER_N2));
                entries.add(new ItemStack(Items.ERLENMEYER_CH4));
                entries.add(new ItemStack(Items.ERLENMEYER_H2));
                entries.add(new ItemStack(Items.ERLENMEYER_NO));
                entries.add(new ItemStack(Items.ERLENMEYER_NO2));
                entries.add(new ItemStack(Items.ERLENMEYER_Cl2));
                entries.add(new ItemStack(Items.ERLENMEYER_CO2));
                entries.add(new ItemStack(Items.ERLENMEYER_CO));
                entries.add(new ItemStack(Items.ERLENMEYER_NH3));
                entries.add(new ItemStack(Items.ERLENMEYER_N2O));
                entries.add(new ItemStack(Items.ERLENMEYER_HCl));
                entries.add(new ItemStack(Items.ERLENMEYER_He));
                entries.add(new ItemStack(Items.ERLENMEYER_Ne));
                entries.add(new ItemStack(Items.ERLENMEYER_Ar));
                entries.add(new ItemStack(Items.ERLENMEYER_CL2O));
                entries.add(new ItemStack(Items.ERLENMEYER_CH4S));
                entries.add(new ItemStack(Items.ERLENMEYER_CH2O));
                entries.add(new ItemStack(Items.ERLENMEYER_H2CO3));
                entries.add(new ItemStack(Items.ERLENMEYER_BH3));
                entries.add(new ItemStack(Items.ERLENMEYER_HF));
                entries.add(new ItemStack(Items.ERLENMEYER_SIH4));
                entries.add(new ItemStack(Items.ERLENMEYER_PH3));
                entries.add(new ItemStack(Items.ERLENMEYER_H2S));
                entries.add(new ItemStack(Items.ERLENMEYER_CF4));
                entries.add(new ItemStack(Items.ERLENMEYER_BF3));
                entries.add(new ItemStack(Items.ERLENMEYER_BCL3));
                entries.add(new ItemStack(Items.ERLENMEYER_SO2));
                entries.add(new ItemStack(Items.ERLENMEYER_CLF));
                entries.add(new ItemStack(Items.ERLENMEYER_F2));
                entries.add(new ItemStack(Items.ERLENMEYER_C2H6O));
                entries.add(new ItemStack(Items.ERLENMEYER_HNO3));
                entries.add(new ItemStack(Items.ERLENMEYER_H2O));
                entries.add(new ItemStack(Items.ERLENMEYER_NCL3));
                entries.add(new ItemStack(Items.ERLENMEYER_CS2));
                entries.add(new ItemStack(Items.ERLENMEYER_CCL4));
                entries.add(new ItemStack(Items.ERLENMEYER_PCl3));
                entries.add(new ItemStack(Items.ERLENMEYER_SCl2));
                entries.add(new ItemStack(Items.ERLENMEYER_HCN));
                entries.add(new ItemStack(Items.ERLENMEYER_CH4O));
                entries.add(new ItemStack(Items.ERLENMEYER_SICL4));
            })
            .build();

    public static final ItemGroup ATOMS = FabricItemGroup.builder(
                    new Identifier(Minelabs.MOD_ID, "atoms"))
            .icon(() -> new ItemStack(Atom.HYDROGEN.getItem()))
            .entries((displayContext, entries) -> entries.addAll(Items.ATOMS.stream().map(ItemStack::new).toList()))
            .build();

    public static final ItemGroup ELEMENTARY_PARTICLES = FabricItemGroup.builder(
                    new Identifier(Minelabs.MOD_ID, "elementary_particles"))
            .icon(() -> new ItemStack(Items.PION_NUL))
            .entries((displayContext, entries) -> {
                entries.add(new ItemStack(Items.UPQUARK_RED));
                entries.add(new ItemStack(Items.UPQUARK_GREEN));
                entries.add(new ItemStack(Items.UPQUARK_BLUE));
                entries.add(new ItemStack(Items.ANTI_UPQUARK_RED));
                entries.add(new ItemStack(Items.ANTI_UPQUARK_GREEN));
                entries.add(new ItemStack(Items.ANTI_UPQUARK_BLUE));
                entries.add(new ItemStack(Items.DOWNQUARK_RED));
                entries.add(new ItemStack(Items.DOWNQUARK_GREEN));
                entries.add(new ItemStack(Items.DOWNQUARK_BLUE));
                entries.add(new ItemStack(Items.ANTI_DOWNQUARK_RED));
                entries.add(new ItemStack(Items.ANTI_DOWNQUARK_GREEN));
                entries.add(new ItemStack(Items.ANTI_DOWNQUARK_BLUE));
                entries.add(new ItemStack(Items.GLUON));
                entries.add(new ItemStack(Items.ELECTRON));
                entries.add(new ItemStack(Items.POSITRON));
                entries.add(new ItemStack(Items.PHOTON));
                entries.add(new ItemStack(Items.NEUTRINO));
                entries.add(new ItemStack(Items.ANTINEUTRINO));
                entries.add(new ItemStack(Items.WEAK_BOSON));
                entries.add(new ItemStack(Items.PROTON));
                entries.add(new ItemStack(Items.ANTI_PROTON));
                entries.add(new ItemStack(Items.NEUTRON));
                entries.add(new ItemStack(Items.ANTI_NEUTRON));
                entries.add(new ItemStack(Items.PION_NUL));
                entries.add(new ItemStack(Items.PION_MINUS));
                entries.add(new ItemStack(Items.PION_PLUS));
                entries.add(new ItemStack(Items.CHARGED_POINT));
            })
            .build();

    public static final ItemGroup QUANTUM_FIELDS = FabricItemGroup.builder(
                    new Identifier(Minelabs.MOD_ID, "quantum_fields"))
            .icon(() -> new ItemStack(Items.GLUON_QUANTUMFIELD))
            .entries((displayContext, entries) -> {
                entries.add(new ItemStack(Items.UPQUARK_QUANTUMFIELD));
                entries.add(new ItemStack(Items.DOWNQUARK_QUANTUMFIELD));
                entries.add(new ItemStack(Items.GLUON_QUANTUMFIELD));
                entries.add(new ItemStack(Items.ELECTRON_QUANTUMFIELD));
                entries.add(new ItemStack(Items.PHOTON_QUANTUMFIELD));
                entries.add(new ItemStack(Items.NEUTRINO_QUANTUMFIELD));
                entries.add(new ItemStack(Items.WEAK_BOSON_QUANTUMFIELD));
            })
            .build();


    public static void onInitialize() {
    }
}
