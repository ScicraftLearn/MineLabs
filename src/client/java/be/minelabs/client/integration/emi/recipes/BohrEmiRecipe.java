package be.minelabs.client.integration.emi.recipes;

import be.minelabs.Minelabs;
import be.minelabs.client.integration.emi.MinelabsEmiPlugin;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import be.minelabs.science.Atom;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.DrawableWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class BohrEmiRecipe extends BasicEmiRecipe {
    // actual TEXTURE (just missing atm) ?
    private static final Identifier BOHR_TEXTURE = new Identifier(Minelabs.MOD_ID, "textures/integration/bohr_image.png");

    public BohrEmiRecipe(AtomItem atomItem) {
        super(MinelabsEmiPlugin.BOHR_CATEGORY, new Identifier(Minelabs.MOD_ID,
                        "/" + MinelabsEmiPlugin.BOHR_CATEGORY.getId().getPath() + "/" + atomItem.getAtom().getName().toLowerCase()),
                160, 80);
        Atom atom = atomItem.getAtom();
        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(Items.ELECTRON)), atom.getAtomNumber()));
        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(Items.NEUTRON)), atom.getInitialNeutrons()));
        inputs.add(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(Items.PROTON)), atom.getAtomNumber()));
        outputs.add(EmiStack.of(atomItem));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 85, 10);
        widgets.addSlot(inputs.get(1), 85, 30);
        widgets.addSlot(inputs.get(2), 85, 50);

        widgets.addTexture(EmiTexture.EMPTY_ARROW, 105, 30);

        widgets.addSlot(outputs.get(0), 130, 30).recipeContext(this);

        //widgets.addTexture(BOHR_TEXTURE, 10, 10, 60, 60, 10, 10);
        widgets.addDrawable(10, 10, 60, 60, (matrices, mouseX, mouseY, delta) -> {
            // TODO custom rendering ?
        });
    }
}
