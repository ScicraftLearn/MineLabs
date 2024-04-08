package be.minelabs.client.integration.rei.util;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;

import java.util.Collections;
import java.util.List;

public class Util {

    public static List<EntryIngredient> getInputList(DefaultedList<Ingredient> ingredients) {
        if (ingredients.isEmpty()) return Collections.emptyList();
        return ingredients.stream().map(EntryIngredients::ofIngredient).toList();
    }
}
