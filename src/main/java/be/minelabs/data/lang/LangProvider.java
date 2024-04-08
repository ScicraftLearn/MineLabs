package be.minelabs.data.lang;

import be.minelabs.block.Blocks;
import be.minelabs.entity.Entities;
import be.minelabs.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;

public class LangProvider extends MinelabsLangProvider {

    public LangProvider(FabricDataOutput dataOutput) {
        // DEFAULT LANG: en_us
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        super.generateTranslations(builder);
        try {
            builder.add(dataOutput.getModContainer()
                    .findPath("assets/minelabs/lang/en_us.static.json").get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        builder.add(Blocks.LEWIS_BLOCK, "Lewis Crafting Table");
        builder.add(Blocks.IONIC_BLOCK, "Ionic Crafting Table");
        builder.add(Blocks.BOHR_BLUEPRINT, "Bohr Blueprint");
        builder.add(Blocks.MOLOGRAM_BLOCK, "Mologram");

        builder.add(Items.SALT, "Salt");
        builder.add(Items.SALT_SHARD, "Salt Shard");
        builder.add(Items.SALT_BLOCK, "Block of Salt");
        builder.add(Items.SMALL_SALT_CRYSTAL, "Small Salt Crystal");
        builder.add(Items.MEDIUM_SALT_CRYSTAL, "Medium Salt Crystal");
        builder.add(Items.LARGE_SALT_CRYSTAL, "Large Salt Crystal");
        builder.add(Items.SALT_CRYSTAL, "Salt Crystal");
        builder.add(Items.SALT_ORE, "Salt Ore");
        builder.add(Items.DEEPSLATE_SALT_ORE, "Deepslate Salt Ore");
        builder.add(Items.BUDDING_SALT_BLOCK, "Budding Salt");

        builder.add(Items.LAB_COAT, "Lab Coat");
        builder.add(Items.SAFETY_GLASSES, "Safety Glasses");
        builder.add(Items.FORCE_GLASSES, "Force Glasses");
        builder.add(Items.LENS, "Microscope Lens");
        builder.add(Items.BIG_LENS, "Microscope Lens x2");
        builder.add(Blocks.LAB_DRAWER, "Lab Drawers");
        builder.add(Blocks.LAB_SINK, "Lab Sink");
        builder.add(Blocks.LAB_CABIN, "Lab Cabin");
        builder.add(Blocks.LAB_LEWIS, "Lewis Crafting Table");
        builder.add(Blocks.LAB_CENTER, "Lab Center");
        builder.add(Blocks.LAB_CORNER, "Lab Corner");
        builder.add(Blocks.MICROSCOPE, "Microscope");
        builder.add(Blocks.TUBERACK, "Tube rack");
        builder.add(Blocks.BURNER, "Bunsen Burner");
        builder.add(Blocks.ERLENMEYER_STAND, "Erlenmeyer Stand");

        builder.add(Items.LASERTOOL_IRON, "Iron Laser Tool");
        builder.add(Items.LASERTOOL_GOLD, "Gold Laser Tool");
        builder.add(Items.LASERTOOL_DIAMOND, "Diamond Laser Tool");
        builder.add(Items.MAGNET, "Magnet");
        builder.add(Items.POCKET_HOLE, "Pocket Black Hole");
        builder.add(Items.FORCE_COMPASS, "Force Compass");
        builder.add(Items.ATOM_PACK, "Atom pack");
        builder.add(Blocks.ATOMIC_STORAGE, "Atomic Storage");

        builder.add(Items.ANTI_NEUTRON, "AntiNeutron");
        builder.add(Items.ANTI_PROTON, "AntiProton");
        builder.add(Items.ANTINEUTRINO, "AntiNeutrino");
        builder.add(Items.ELECTRON, "Electron");
        builder.add(Items.NEUTRINO, "Neutrino");
        builder.add(Items.NEUTRON, "Neutron");
        builder.add(Items.POSITRON, "Positron");
        builder.add(Items.PROTON, "Proton");
        builder.add(Items.WEAK_BOSON, "Weak Boson");
        builder.add(Items.PION_MINUS, "Pion Minus");
        builder.add(Items.PION_NUL, "Pion Nul");
        builder.add(Items.PION_PLUS, "Pion Plus");
        builder.add(Items.GLUON, "Gluon");
        builder.add(Items.PHOTON, "Photon");
        builder.add(Items.CHARGED_POINT, "Point Charge");

        builder.add(Items.ANTI_DOWNQUARK_BLUE, "Anti Downquark");
        builder.add(Items.ANTI_DOWNQUARK_GREEN, "Anti Downquark");
        builder.add(Items.ANTI_DOWNQUARK_RED, "Anti Downquark");
        builder.add(Items.ANTI_UPQUARK_BLUE, "Anti Upquark");
        builder.add(Items.ANTI_UPQUARK_GREEN, "Anti Upquark");
        builder.add(Items.ANTI_UPQUARK_RED, "Anti Upquark");
        builder.add(Items.DOWNQUARK_BLUE, "Downquark");
        builder.add(Items.DOWNQUARK_GREEN, "Downquark");
        builder.add(Items.DOWNQUARK_RED, "Downquark");
        builder.add(Items.UPQUARK_BLUE, "Upquark");
        builder.add(Items.UPQUARK_GREEN, "Upquark");
        builder.add(Items.UPQUARK_RED, "Upquark");

        builder.add(Blocks.WEAK_BOSON_QUANTUMFIELD, "Weak Boson Quantumfield");
        builder.add(Blocks.UPQUARK_QUANTUMFIELD, "Upquark Quantumfield");
        builder.add(Blocks.DOWNQUARK_QUANTUMFIELD, "Downquark Quantumfield");
        builder.add(Blocks.PHOTON_QUANTUMFIELD, "Photon Quantumfield");
        builder.add(Blocks.NEUTRINO_QUANTUMFIELD, "Neutrino Quantumfield");
        builder.add(Blocks.ELECTRON_QUANTUMFIELD, "Electron Quantumfield");
        builder.add(Blocks.GLUON_QUANTUMFIELD, "Gluon Quantumfield");

        builder.add(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, "Bohr Entity");
        builder.add(Entities.CORROSIVE_ENTITY, "");
        builder.add(Entities.PARTICLE_ENTITY, "Charged Entity");
        builder.add(Entities.POINT_CHARGED_ENTITY, "Point Charged Entity");
    }
}
