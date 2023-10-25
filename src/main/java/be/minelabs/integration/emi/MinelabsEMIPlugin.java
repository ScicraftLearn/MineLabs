package be.minelabs.integration.emi;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.integration.emi.recipes.BohrEmiRecipe;
import be.minelabs.integration.emi.recipes.IonicEMIRecipe;
import be.minelabs.integration.emi.recipes.LewisEmiRecipe;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import be.minelabs.recipe.ionic.IonicRecipe;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

public class MinelabsEMIPlugin implements EmiPlugin {
    private static EmiStack BOHR_STACK = EmiStack.of(Blocks.BOHR_BLUEPRINT);
    public static final EmiRecipeCategory BOHR_CATEGORY = new EmiRecipeCategory(
            new Identifier(Minelabs.MOD_ID, ""), BOHR_STACK);
    private static EmiStack LEWIS_STACK = EmiStack.of(Blocks.LEWIS_BLOCK);
    public static EmiRecipeCategory LEWIS_CATEGORY = new EmiRecipeCategory(
            new Identifier(Minelabs.MOD_ID, "molecule_crafting"), LEWIS_STACK);

    private static EmiStack IONIC_STACK = EmiStack.of(Blocks.LEWIS_BLOCK);
    public static EmiRecipeCategory IONIC_CATEGORY = new EmiRecipeCategory(
            new Identifier(Minelabs.MOD_ID, "ionic_crafting"), IONIC_STACK);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(LEWIS_CATEGORY);
        registry.addCategory(IONIC_CATEGORY);
        registry.addCategory(BOHR_CATEGORY);

        registry.addWorkstation(LEWIS_CATEGORY, LEWIS_STACK);
        registry.addWorkstation(IONIC_CATEGORY, IONIC_STACK);
        registry.addWorkstation(BOHR_CATEGORY, BOHR_STACK);

        RecipeManager manager = registry.getRecipeManager();


        for (MoleculeRecipe recipe : manager.listAllOfType(MoleculeRecipe.MoleculeRecipeType.INSTANCE)) {
            registry.addRecipe(new LewisEmiRecipe(recipe));
        }
        for (IonicRecipe recipe : manager.listAllOfType(IonicRecipe.IonicRecipeType.INSTANCE)) {
            registry.addRecipe(new IonicEMIRecipe(recipe));
        }
        for (AtomItem atom : Items.ATOMS) {
            registry.addRecipe(new BohrEmiRecipe(atom));
        }
    }
}
