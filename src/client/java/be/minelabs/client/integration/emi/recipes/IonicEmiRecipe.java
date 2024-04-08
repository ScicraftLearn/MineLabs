package be.minelabs.client.integration.emi.recipes;

import be.minelabs.client.integration.emi.MinelabsEmiPlugin;
import be.minelabs.item.Items;
import be.minelabs.recipe.ionic.IonicRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;

public class IonicEmiRecipe extends BasicEmiRecipe {

    private final EmiIngredient container;
    private final int time;

    public IonicEmiRecipe(IonicRecipe recipe) {
        super(MinelabsEmiPlugin.IONIC_CATEGORY, recipe.getId(), 160, 100);

        recipe.getIngredients().forEach(ingredient -> inputs.add(EmiIngredient.of(ingredient)));

        if (recipe.needsContainer()) {
            container = EmiStack.of(Items.ERLENMEYER);
            inputs.add(EmiIngredient.of(Ingredient.ofItems(Items.ERLENMEYER)));
        } else {
            container = EmiStack.EMPTY;
        }
        outputs.add(EmiStack.of(recipe.getOutput(DynamicRegistryManager.EMPTY)));

        time = recipe.getTime();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addFillingArrow(105, 44, time * 50);

        if (!container.isEmpty()) {
            widgets.addSlot(container, 120, 74);
        } else {
            widgets.addSlot(120, 74);
        }

        widgets.addSlot(outputs.get(0), 140, 44);
    }
}
