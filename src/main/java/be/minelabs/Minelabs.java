package be.minelabs;

import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.Blocks;
import be.minelabs.entity.Entities;
import be.minelabs.event.ServerModEvents;
import be.minelabs.fluid.Fluids;
import be.minelabs.item.Items;
import be.minelabs.particle.ParticleTypes;
import be.minelabs.recipe.CraftingRecipes;
import be.minelabs.science.coulomb.CoulombResource;
import be.minelabs.screen.ScreenHandlers;
import be.minelabs.sound.SoundEvents;
import be.minelabs.util.NucleusStabilityTable;
import be.minelabs.village.Villagers;
import be.minelabs.world.MinelabsGameRules;
import be.minelabs.world.dimension.ModDimensions;
import be.minelabs.world.gen.WorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Minelabs implements ModInitializer {

    public static final String MOD_ID = "minelabs";

    // This logger is used to write text to the console and the log file.
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(NucleusStabilityTable.INSTANCE);
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(CoulombResource.INSTANCE);

        Items.onInitialize();
        Blocks.onInitialize();
        Fluids.onInitialize();
        Entities.onInitialize();

        SoundEvents.onInitialize();
        ParticleTypes.onInitialize();
        ScreenHandlers.onInitialize();

        WorldGeneration.onInitialize();
        ModDimensions.onInitialize();

        CraftingRecipes.onInitialize();
        Villagers.onInitialize();

        MinelabsGameRules.onInitialize();
        ServerModEvents.onInitialize();
        Criteria.onInitialize();
    }
}