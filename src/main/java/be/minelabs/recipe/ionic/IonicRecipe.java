package be.minelabs.recipe.ionic;

import be.minelabs.recipe.molecules.MoleculeGraphJsonFormat;
import be.minelabs.recipe.molecules.PartialMolecule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;


public class IonicRecipe implements Recipe<IonicInventory> {

    private final JsonObject leftjson;
    private final int leftdensity;
    private final int leftCharge;
    private final JsonObject rightjson;
    private final int rightdensity;
    private final int rightCharge;
    private final Identifier id;
    private final PartialMolecule leftMolecule;
    private final PartialMolecule rightMolecule;
    private final DefaultedList<Ingredient> ingredients = DefaultedList.of();
    private final DefaultedList<Ingredient> leftingredients = DefaultedList.of();
    private final DefaultedList<Ingredient> rightingredients = DefaultedList.of();
    private final ItemStack output;

    private final Integer time;
    private final Boolean container;


    IonicRecipe(JsonObject leftjson, int leftdensity, int leftCharge, JsonObject rightjson, int rightdensity, int rightCharge, ItemStack output, Identifier id) {
        // TODO rework ? + time and container loading
        MoleculeGraphJsonFormat leftGraph = new Gson().fromJson(leftjson, MoleculeGraphJsonFormat.class);
        this.leftMolecule = new PartialMolecule(leftGraph.get());

        MoleculeGraphJsonFormat rightGraph = new Gson().fromJson(rightjson, MoleculeGraphJsonFormat.class);
        this.rightMolecule = new PartialMolecule(rightGraph.get());

        this.leftjson = leftjson;
        this.leftdensity = leftdensity;
        this.leftCharge = leftCharge;
        this.rightjson = rightjson;
        this.rightdensity = rightdensity;
        this.rightCharge = rightCharge;
        this.output = output;
        this.id = id;
        this.time = 23;
        this.container = true;

        this.leftingredients.addAll(leftMolecule.getIngredients().stream().map(atom -> Ingredient.ofItems(atom.getItem())).toList());
        this.rightingredients.addAll(rightMolecule.getIngredients().stream().map(atom -> Ingredient.ofItems(atom.getItem())).toList());
        this.ingredients.addAll(leftingredients);
        this.ingredients.addAll(rightingredients);
    }

    /*
     * Disables Recipe Book error
     */
    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public boolean matches(IonicInventory inventory, World world) {
        boolean left = inventory.getLeftGrid().getPartialMolecule().getStructure().isIsomorphicTo(leftMolecule.getStructure());
        boolean right = inventory.getRightGrid().getPartialMolecule().getStructure().isIsomorphicTo(rightMolecule.getStructure());
        return left && right;
    }

    @Override
    public ItemStack craft(IonicInventory inventory, DynamicRegistryManager registryManager) {
        return this.getOutput(registryManager);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return IonicRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return IonicRecipeType.INSTANCE;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public DefaultedList<Ingredient> getLeftingredients() {
        return leftingredients;
    }

    public DefaultedList<Ingredient> getRightingredients() {
        return rightingredients;
    }

    public int getLeftdensity() {
        return leftdensity;
    }

    public int getRightdensity() {
        return rightdensity;
    }

    public int getLeftCharge() {
        return leftCharge;
    }

    public int getRightCharge() {
        return rightCharge;
    }

    public int getTime() {
        return time;
    }

    public boolean needsContainer() {
        return container;
    }

    public static class IonicRecipeType implements RecipeType<IonicRecipe> {
        public static final IonicRecipeType INSTANCE = new IonicRecipeType();
        public static final String ID = "ionic_crafting";

        private IonicRecipeType() {
        }
    }

    public static class IonicRecipeSerializer implements RecipeSerializer<IonicRecipe> {

        public static final IonicRecipeSerializer INSTANCE = new IonicRecipeSerializer();
        public static final String ID = "ionic_crafting";

        @Override
        public IonicRecipe read(Identifier id, JsonObject json) {
            JsonObject left = json.getAsJsonObject("cation");

            int leftDensity = left.get("density").getAsInt();
            int leftCharge = left.get("charge").getAsInt();

            JsonObject right = json.getAsJsonObject("anion");
            int rightDensity = right.get("density").getAsInt();
            int rightCharge = left.get("charge").getAsInt();

            ItemStack output = ShapedRecipe.outputFromJson(json.getAsJsonObject("result"));
            return new IonicRecipe(left.getAsJsonObject("structure"), leftDensity, leftCharge, right.getAsJsonObject("structure"), rightDensity, rightCharge, output, id);
        }

        @Override
        public IonicRecipe read(Identifier id, PacketByteBuf buf) {
            JsonObject leftJson = JsonParser.parseString(buf.readString()).getAsJsonObject();
            int leftDensity = buf.readInt();
            int leftCharge = buf.readInt();
            JsonObject rightJson = JsonParser.parseString(buf.readString()).getAsJsonObject();
            int rightDensity = buf.readInt();
            int rightCharge = buf.readInt();
            ItemStack output = buf.readItemStack();
            return new IonicRecipe(leftJson, leftDensity, leftCharge, rightJson, rightDensity, rightCharge, output, id);
        }

        @Override
        public void write(PacketByteBuf buf, IonicRecipe recipe) {
            buf.writeString(recipe.leftjson.toString());
            buf.writeInt(recipe.leftdensity);
            buf.writeInt(recipe.leftCharge);
            buf.writeString(recipe.rightjson.toString());
            buf.writeInt(recipe.rightdensity);
            buf.writeInt(recipe.rightCharge);
            buf.writeItemStack(recipe.output);
        }
    }
}
