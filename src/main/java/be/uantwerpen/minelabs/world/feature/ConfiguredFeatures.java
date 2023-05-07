package be.uantwerpen.minelabs.world.feature;

import be.uantwerpen.minelabs.Minelabs;
import be.uantwerpen.minelabs.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class ConfiguredFeatures {

    public static final List<OreFeatureConfig.Target> OVERWORLD_SALT_ORES = List.of(
            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Blocks.SALT_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_SALT_ORE.getDefaultState()));


    // Actual registry
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> SALT_ORE = net.minecraft.world.gen.feature.ConfiguredFeatures
            .register("salt_ore", Feature.ORE, new OreFeatureConfig(OVERWORLD_SALT_ORES, 10));

    public static void registerFeatures() {
        Minelabs.LOGGER.info("registering features");
    }
}
