package be.minelabs.client.integration.emi;

import be.minelabs.Minelabs;
import be.minelabs.block.Blocks;
import be.minelabs.client.integration.emi.recipes.CoulombInfoEmiRecipe;
import be.minelabs.client.integration.emi.recipes.BohrEmiRecipe;
import be.minelabs.client.integration.emi.recipes.IonicEmiRecipe;
import be.minelabs.client.integration.emi.recipes.LewisEmiRecipe;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import be.minelabs.recipe.ionic.IonicRecipe;
import be.minelabs.recipe.lewis.MoleculeRecipe;
import be.minelabs.science.coulomb.CoulombData;
import be.minelabs.science.coulomb.CoulombResource;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.stack.EmiStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class MinelabsEmiPlugin implements EmiPlugin {
    // EMI WORKS ON CLIENT ONLY
    private static EmiStack BOHR_STACK = EmiStack.of(Blocks.BOHR_BLUEPRINT);
    public static final EmiRecipeCategory BOHR_CATEGORY = new EmiRecipeCategory(
            new Identifier(Minelabs.MOD_ID, "atom_crafting"), BOHR_STACK);
    private static EmiStack LEWIS_STACK = EmiStack.of(Blocks.LEWIS_BLOCK);
    public static EmiRecipeCategory LEWIS_CATEGORY = new EmiRecipeCategory(
            new Identifier(Minelabs.MOD_ID, "molecule_crafting"), LEWIS_STACK);

    private static EmiStack IONIC_STACK = EmiStack.of(Blocks.IONIC_BLOCK);
    public static EmiRecipeCategory IONIC_CATEGORY = new EmiRecipeCategory(
            new Identifier(Minelabs.MOD_ID, "ionic_crafting"), IONIC_STACK);

    private static EmiStack COULOMB_STACK = EmiStack.of(Items.ELECTRON);
    public static EmiRecipeCategory COULOMB_INFO_CATEGORY = new EmiRecipeCategory(
            new Identifier(Minelabs.MOD_ID, "coulomb_info"), COULOMB_STACK);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(LEWIS_CATEGORY);
        registry.addCategory(IONIC_CATEGORY);
        registry.addCategory(BOHR_CATEGORY);
        registry.addCategory(COULOMB_INFO_CATEGORY);

        registry.addWorkstation(LEWIS_CATEGORY, LEWIS_STACK);
        registry.addWorkstation(IONIC_CATEGORY, IONIC_STACK);
        registry.addWorkstation(BOHR_CATEGORY, BOHR_STACK);

        RecipeManager manager = registry.getRecipeManager();


        for (MoleculeRecipe recipe : manager.listAllOfType(MoleculeRecipe.MoleculeRecipeType.INSTANCE)) {
            registry.addRecipe(new LewisEmiRecipe(recipe));
        }
        for (IonicRecipe recipe : manager.listAllOfType(IonicRecipe.IonicRecipeType.INSTANCE)) {
            registry.addRecipe(new IonicEmiRecipe(recipe));
        }
        for (AtomItem atom : Items.ATOMS) {
            registry.addRecipe(new BohrEmiRecipe(atom));
        }
        for (Map.Entry<Identifier, CoulombData> entry : CoulombResource.INSTANCE.getResourceData().entrySet()) {
            registry.addRecipe(new CoulombInfoEmiRecipe(entry.getKey(), entry.getValue()));
        }
    }
}
