package be.minelabs.recipe.molecules;

import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class MoleculeRecipeJsonFormat {

    public MoleculeGraphJsonFormat structure;

    public Integer density;

    public Result result;
    public Boolean container;
    public Integer time;

    public static class Result {
        public String item;
        public Integer count;
    }

    public void validate() {
        // Validate all fields are there (and set default values)
        if (result == null)
            throw new JsonSyntaxException("Attribute 'result' is missing");
        if (result.count == null){
            result.count = 1;
        }
        if (structure == null)
            throw new JsonSyntaxException("Attribute 'structure' is missing");
        if (density == null)
            throw new JsonSyntaxException("Attribute 'density' is missing");
        if (container == null)
            container = true;
        if (time == null)
            // IN TICKS
            time = 23;
    }

    public Item getOutput() {
        return Registries.ITEM.getOrEmpty(new Identifier(result.item))
                // Validate the entered item actually exists
                .orElseThrow(() -> new JsonSyntaxException("No such item " + result.item));
    }
}