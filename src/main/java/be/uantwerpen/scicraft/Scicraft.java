package be.uantwerpen.scicraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.paintings.Paintings;
import be.uantwerpen.scicraft.data.MoleculeManager;
import be.uantwerpen.scicraft.crafting.CraftingRecipes;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.entity.ScientificVillager;
import be.uantwerpen.scicraft.gui.ScreenHandlers;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.sound.SoundEvents;
import be.uantwerpen.scicraft.world.gen.OreGenerations;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;


public class Scicraft implements ModInitializer {

    public static final MoleculeManager MOLECULEMANAGER = new MoleculeManager();

	public static final String MOD_ID = "scicraft";

    // This logger is used to write text to the console and the log file.
    public static final Logger LOGGER = LogManager.getLogger("scicraft");

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Scicraft world!");

        Items.registerItems();
        Blocks.registerBlocks();
        BlockEntities.registerBlockEntities();
        Entities.registerEntities();
        ExtraDispenserBehavior.registerBehaviors();
        SoundEvents.registerSounds();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(MOLECULEMANAGER);
        ScreenHandlers.registerScreens();
        Paintings.registerPaintings();
        OreGenerations.generateOres();

        CraftingRecipes.register();

        ScientificVillager.registerVillagers();
        ScientificVillager.registerTrades();
    }
}