package be.minelabs.recipe.lewis;

import be.minelabs.recipe.molecules.Molecule;
import be.minelabs.recipe.molecules.MoleculeRecipeJsonFormat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class MoleculeRecipe implements Recipe<LewisCraftingGrid> {

    private final Molecule molecule;
    private final DefaultedList<Ingredient> ingredients = DefaultedList.of();
    private final Identifier id;
    private final JsonObject json;
    private final Integer density;
    private final Integer time;
    private final Boolean container;

    private final Integer count;


    public MoleculeRecipe(Molecule molecule, Identifier id, JsonObject json, Integer density, Boolean container, Integer time, Integer count) {
        this.molecule = molecule;
        ingredients.addAll(molecule.getIngredients().stream().map(atom -> Ingredient.ofItems(atom.getItem())).toList());
        this.id = id;
        this.json = json;
        this.density = density;
        this.container = container;
        this.time = time;
        this.count = count;
    }

    /*
     * Disables Recipe Book error
     */
    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public Integer getDensity() {
        return density;
    }

    public Molecule getMolecule() {
        return molecule;
    }

    public Boolean needsContainer() {
        return container;
    }

    public Integer getTime() {
        return this.time;
    }


    @Override
    public boolean matches(LewisCraftingGrid inventory, World world) {
        return inventory.getPartialMolecule().getStructure().isIsomorphicTo(molecule.getStructure());
    }

    /**
     * Seems to be used for recipe book functionality (showing which recipes are craftable based on contents of inventory).
     */
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public int getRequiredAmount(int index) {
        return index <= ingredients.size() && index >= 0 ? getRequiredAmount(ingredients.get(index)) : 0;
    }

    public int getRequiredAmount(Ingredient ingredient) {
        ItemStack stack = ingredient.getMatchingStacks()[0];
        int amount = 0;
        for (Ingredient ingred : ingredients) {
            if (ingred.test(stack)) {
                amount += 8;
            }
        }
        return amount;
    }

    @Override
    public ItemStack craft(LewisCraftingGrid inventory, DynamicRegistryManager registryManager) {
        return getOutput(registryManager);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return new ItemStack(molecule.getItem(), this.count);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public JsonObject getJson() {
        return json;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MoleculeRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return MoleculeRecipeType.INSTANCE;
    }

    public static class MoleculeRecipeType implements RecipeType<MoleculeRecipe> {
        public static final MoleculeRecipeType INSTANCE = new MoleculeRecipeType();
        public static final String ID = "molecule_crafting";

        private MoleculeRecipeType() {
        }
    }

    public static class MoleculeRecipeSerializer implements RecipeSerializer<MoleculeRecipe> {

        private MoleculeRecipeSerializer() {
        }

        public static final MoleculeRecipeSerializer INSTANCE = new MoleculeRecipeSerializer();
        public static final String ID = "molecule_crafting";

        @Override
        public MoleculeRecipe read(Identifier id, JsonObject json) {
            MoleculeRecipeJsonFormat recipeJson = new Gson().fromJson(json, MoleculeRecipeJsonFormat.class);
            recipeJson.validate();

            Molecule molecule = new Molecule(recipeJson.structure.get(), recipeJson.getOutput());

            return new MoleculeRecipe(molecule, id, json, recipeJson.density, recipeJson.container, recipeJson.time, recipeJson.result.count);
        }

        @Override
        public MoleculeRecipe read(Identifier id, PacketByteBuf buf) {
            // TODO: untested code
            // Writing graph optimized to bytes is hard -> just send json over.
            String jsonStr = buf.readString();
            JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
            return read(id, json);
        }

        @Override
        public void write(PacketByteBuf buf, MoleculeRecipe recipe) {
            // Writing graph optimized to bytes is hard -> just send json over.
//            Minelabs.LOGGER.info(recipe.getJson().toString());
            buf.writeString(recipe.getJson().toString());
        }
    }

}
