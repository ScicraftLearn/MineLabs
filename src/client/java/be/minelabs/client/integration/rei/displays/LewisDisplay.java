package be.minelabs.client.integration.rei.displays;

import be.minelabs.client.integration.rei.categories.LewisCategory;
import be.minelabs.client.integration.rei.util.Util;
import be.minelabs.item.Items;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.List;

public class LewisDisplay extends BasicDisplay {

    private final EntryIngredient container;
    private final int duration;

    public LewisDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
        container = EntryIngredient.empty();
        duration = 0;
    }

    public LewisDisplay(MoleculeRecipe recipe) {
        super(Util.getInputList(recipe.getIngredients()),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.getOutput(DynamicRegistryManager.EMPTY)))));
        if (recipe.needsContainer()) {
            container = EntryIngredient.of(EntryStacks.of(Items.ERLENMEYER));
        } else {
            container = EntryIngredient.empty();
        }
        duration = recipe.getTime();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return LewisCategory.MOLECULE_CRAFTING;
    }

    public EntryIngredient getContainer() {
        return container;
    }

    public int getDuration() {
        return this.duration;
    }
}
