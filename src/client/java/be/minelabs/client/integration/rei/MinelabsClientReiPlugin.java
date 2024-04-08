package be.minelabs.client.integration.rei;

import be.minelabs.block.Blocks;
import be.minelabs.client.gui.screen.IonicScreen;
import be.minelabs.client.gui.screen.LewisScreen;
import be.minelabs.client.integration.rei.categories.BohrCategory;
import be.minelabs.client.integration.rei.categories.CoulombInfoCategory;
import be.minelabs.client.integration.rei.categories.IonicCategory;
import be.minelabs.client.integration.rei.categories.LewisCategory;
import be.minelabs.client.integration.rei.displays.BohrDisplay;
import be.minelabs.client.integration.rei.displays.CoulombInfoDisplay;
import be.minelabs.client.integration.rei.displays.IonicDisplay;
import be.minelabs.client.integration.rei.displays.LewisDisplay;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import be.minelabs.recipe.ionic.IonicRecipe;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import be.minelabs.science.coulomb.CoulombData;
import be.minelabs.science.coulomb.CoulombResource;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class MinelabsClientReiPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new LewisCategory());
        registry.addWorkstations(LewisCategory.MOLECULE_CRAFTING,
                EntryStacks.of(Blocks.LEWIS_BLOCK), EntryStacks.of(Blocks.LAB_LEWIS));

        registry.add(new IonicCategory());
        registry.addWorkstations(IonicCategory.IONIC_CRAFTING, EntryStacks.of(Blocks.IONIC_BLOCK));

        registry.add(new CoulombInfoCategory());

        registry.add(new BohrCategory());
        registry.addWorkstations(BohrCategory.ATOM_CRAFTING, EntryStacks.of(Blocks.BOHR_BLUEPRINT));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(MoleculeRecipe.class, MoleculeRecipe.MoleculeRecipeType.INSTANCE, LewisDisplay::new);
        registry.registerRecipeFiller(IonicRecipe.class, IonicRecipe.IonicRecipeType.INSTANCE, IonicDisplay::new);

        for (Map.Entry<Identifier, CoulombData> entry : CoulombResource.INSTANCE.getResourceData().entrySet()) {
            registry.add(new CoulombInfoDisplay(entry.getValue(), entry.getKey()));
        }

        for (AtomItem item : Items.ATOMS){
            registry.add(new BohrDisplay(item));
        }
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(screen -> new Rectangle(100, 50, 25, 20),
                LewisScreen.class, LewisCategory.MOLECULE_CRAFTING);
        registry.registerContainerClickArea(screen -> new Rectangle(146, 38, 23, 19),
                IonicScreen.class, IonicCategory.IONIC_CRAFTING);
    }

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        // Might have to rethink these numbers
        zones.register(LewisScreen.class, screen -> List.of(new Rectangle(177, 90, 20, 42)));
    }
}
