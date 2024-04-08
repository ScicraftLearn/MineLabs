package be.minelabs.client.integration.emi.recipes;

import be.minelabs.Minelabs;
import be.minelabs.client.integration.emi.MinelabsEmiPlugin;
import be.minelabs.science.coulomb.CoulombData;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CoulombInfoEmiRecipe extends BasicEmiRecipe {
    private final CoulombData data;

    public CoulombInfoEmiRecipe(Identifier id, CoulombData data) {
        super(MinelabsEmiPlugin.COULOMB_INFO_CATEGORY, id, 140, 150);
        this.data = data;
        String[] split = id.getPath().split("/");
        String itemName = split[split.length - 1].split("\\.")[0];

        Item item = Registries.ITEM.get(new Identifier(Minelabs.MOD_ID, "subatomic/" + itemName));
        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(item))));
        inputs.add(EmiIngredient.of(Ingredient.ofItems(data.getAntiItem())));

        outputs.add(EmiStack.of(data.getAnnihilationDrop()));
        if (!data.stable) {
            outputs.add(EmiStack.of(data.getDecayDrop()));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        // TODO REMOVE TEMPLATE FILE (data/minelabs/science/coulomb/template.json)
        int text_color = 0xffffffff;

        widgets.addSlot(inputs.get(0), 15, 20);
        widgets.addText(Text.translatable("text.minelabs.coulomb_info.charge").append(data.charge.toString()),
                60, 20, text_color, true);
        widgets.addText(Text.translatable("text.minelabs.coulomb_info.mass").append(data.mass.toString()),
                60, 30, text_color, true);
        widgets.addText(data.stable ? Text.translatable("text.minelabs.coulomb_info.stable") : Text.translatable("text.minelabs.coulomb_info.unstable"),
                60, 40, text_color, true);

        widgets.addText(Text.of("======================"), 5, 60, text_color, true);
        widgets.addText(Text.translatable("text.minelabs.coulomb_info.annihilation"),
                5, 75, text_color, true);
        widgets.addSlot(inputs.get(0), 15, 85);
        widgets.addTexture(EmiTexture.PLUS, 40, 88);
        widgets.addSlot(inputs.get(1), 60, 85);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 83, 85);
        widgets.addSlot(outputs.get(0), 110, 85);

        if (!data.stable) {
            widgets.addText(Text.translatable("text.minelabs.coulomb_info.decay"),
                    5, 110, text_color, true);
            widgets.addSlot(inputs.get(0), 15, 120);
            widgets.addTexture(EmiTexture.EMPTY_ARROW, 40, 120);

            // Emi has a way to render chances, but we don't like the look
            widgets.addSlot(outputs.get(1), 70, 120);
            widgets.addText(Text.translatable("text.minelabs.coulomb_info.chance"),
                    95, 120, text_color, true);
            widgets.addText(Text.of(data.decay_chance * 100 + "%"), 100, 130, text_color, true);
        }
    }

    // Should not be included in the Tree view (not a real recipe)
    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
