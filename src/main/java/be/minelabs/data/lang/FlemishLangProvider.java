package be.minelabs.data.lang;

import be.minelabs.block.Blocks;
import be.minelabs.entity.Entities;
import be.minelabs.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;

public class FlemishLangProvider extends MinelabsLangProvider {

    public FlemishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "nl_be");
    }

    public void generateTranslations(TranslationBuilder builder) {
        super.generateTranslations(builder);
        try {
            builder.add(dataOutput.getModContainer()
                    .findPath("assets/minelabs/lang/nl_be.static.json").get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        builder.add(Blocks.LEWIS_BLOCK, "Lewis Werkbank");
        builder.add(Blocks.IONIC_BLOCK, "Ion Werkbank");
        builder.add(Blocks.BOHR_BLUEPRINT, "Bohr Blauwdruk");
        builder.add(Blocks.MOLOGRAM_BLOCK, "Molograaf");

        builder.add(Items.SALT, "Zout");
        builder.add(Items.SALT_SHARD, "Zoutscherf");
        builder.add(Items.SALT_BLOCK, "Zouten Blok");
        builder.add(Items.SMALL_SALT_CRYSTAL, "Klein Zoutkristal");
        builder.add(Items.MEDIUM_SALT_CRYSTAL, "Gemiddeld Zoutkristal");
        builder.add(Items.LARGE_SALT_CRYSTAL, "Groot Zoutkristal");
        builder.add(Items.SALT_CRYSTAL, "Zoutkristallen");
        builder.add(Items.SALT_ORE, "Zouterts");
        builder.add(Items.DEEPSLATE_SALT_ORE, "Deepslate Zouterts");
        builder.add(Items.BUDDING_SALT_BLOCK, "Uitlopende Zout");

        builder.add(Items.LAB_COAT, "Labojas");
        builder.add(Items.SAFETY_GLASSES, "Veiligheidsbril");
        builder.add(Items.FORCE_GLASSES, "Kracht Bril");
        builder.add(Items.LENS, "Microscooplens");
        builder.add(Items.BIG_LENS, "Microscooplens x2");
        builder.add(Blocks.LAB_DRAWER, "Labo Ladenkast");
        builder.add(Blocks.LAB_SINK, "Labo Wastafel");
        builder.add(Blocks.LAB_CABIN, "Labo Kast");
        builder.add(Blocks.LAB_LEWIS, "Lewis Werkbank");
        builder.add(Blocks.LAB_CENTER, "Labotafel (Midden)");
        builder.add(Blocks.LAB_CORNER, "Labotafel (Hoek)");
        builder.add(Blocks.MICROSCOPE, "Microscoop");
        builder.add(Blocks.TUBERACK, "Proefbuisrekje");
        builder.add(Blocks.BURNER, "Bunsen Brander");
        builder.add(Blocks.ERLENMEYER_STAND, "Erlenmeyer Stand");

        builder.add(Items.LASERTOOL_IRON, "Ijzeren Laser Tool");
        builder.add(Items.LASERTOOL_GOLD, "Gouden Laser Tool");
        builder.add(Items.LASERTOOL_DIAMOND, "Diamanten Laser Tool");
        builder.add(Items.MAGNET, "Magneet");
        builder.add(Items.POCKET_HOLE, "Miniatuur Zwart Gat");
        builder.add(Items.FORCE_COMPASS, "Kracht Kompas");

        builder.add(Items.ANTI_NEUTRON, "Anti-Neutron");
        builder.add(Items.ANTI_PROTON, "Anti-Proton");
        builder.add(Items.ANTINEUTRINO, "Anti-Neutrino");
        builder.add(Items.ELECTRON, "Elektron");
        builder.add(Items.NEUTRINO, "Neutrino");
        builder.add(Items.NEUTRON, "Neutron");
        builder.add(Items.POSITRON, "Positron");
        builder.add(Items.PROTON, "Proton");
        builder.add(Items.WEAK_BOSON, "Zwak Boson");
        builder.add(Items.PION_MINUS, "Pion Min");
        builder.add(Items.PION_NUL, "Pion Nul");
        builder.add(Items.PION_PLUS, "Pion Plus");
        builder.add(Items.GLUON, "Gluon");
        builder.add(Items.PHOTON, "Foton");
        builder.add(Items.CHARGED_POINT, "Puntlading");

        builder.add(Items.ANTI_DOWNQUARK_BLUE, "Anti-Downquark");
        builder.add(Items.ANTI_DOWNQUARK_GREEN, "Anti-Downquark");
        builder.add(Items.ANTI_DOWNQUARK_RED, "Anti-Downquark");
        builder.add(Items.ANTI_UPQUARK_BLUE, "Anti-Upquark");
        builder.add(Items.ANTI_UPQUARK_GREEN, "Anti-Upquark");
        builder.add(Items.ANTI_UPQUARK_RED, "Anti-Upquark");
        builder.add(Items.DOWNQUARK_BLUE, "Downquark");
        builder.add(Items.DOWNQUARK_GREEN, "Downquark");
        builder.add(Items.DOWNQUARK_RED, "Downquark");
        builder.add(Items.UPQUARK_BLUE, "Upquark");
        builder.add(Items.UPQUARK_GREEN, "Upquark");
        builder.add(Items.UPQUARK_RED, "Upquark");

        builder.add(Blocks.WEAK_BOSON_QUANTUMFIELD, "Zwak Boson Kwantumveld");
        builder.add(Blocks.UPQUARK_QUANTUMFIELD, "Upquark Kwantumveld");
        builder.add(Blocks.DOWNQUARK_QUANTUMFIELD, "Downquark Kwantumveld");
        builder.add(Blocks.PHOTON_QUANTUMFIELD, "Foton Kwantumveld");
        builder.add(Blocks.NEUTRINO_QUANTUMFIELD, "Neutrino Kwantumveld");
        builder.add(Blocks.ELECTRON_QUANTUMFIELD, "Elektron Kwantumveld");
        builder.add(Blocks.GLUON_QUANTUMFIELD, "Gluon Kwantumveld");

        builder.add(Entities.BOHR_BLUEPRINT_ENTITY_ENTITY_TYPE, "Bohr Entity");
        builder.add(Entities.CORROSIVE_ENTITY, "");
        builder.add(Entities.PARTICLE_ENTITY, "Geladen Entity");
        builder.add(Entities.POINT_CHARGED_ENTITY, "Puntlading Entity");
    }
}
