package be.minelabs.recipe.molecules;

import be.minelabs.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValenceElectrons {
    private final int totalElectronCount;

    private Map<String, Integer> directionalValence;

    /**
     * @param bondDirections : amount of bonds in which direction
     * @param valenceE       : amount of electrons on the current atom
     * @param forced         : True = use pairs, False = Use 1's
     */
    public ValenceElectrons(Map<String, Integer> bondDirections, int valenceE, boolean forced) {
        this.totalElectronCount = valenceE;
        directionalValence = new HashMap<>(Map.of(
                "n", 0,
                "e", 0,
                "s", 0,
                "w", 0
        ));
        addElectrons(bondDirections, valenceE, forced);
    }

    /**
     * @param bondDirections : amount of bonds in which direction
     * @param count          : amount of electrons to place around the atom
     * @param forced         : True = use pairs, False = Use 1's
     */
    private void addElectrons(Map<String, Integer> bondDirections, int count, boolean forced) {
        //inverse bonddir & voorkeur N-E-S-W
        // int: in  elke richting 0,1,2
        //int runs = 0;
        while (count > 0) {
            for (String key : getBestList(bondDirections, forced)) {
                if (count == 0) {
                    break;
                }
                if (bondDirections.get(key) == 0) {
                    if (count >= 2 && directionalValence.get(key) == 0 && forced) {
                        addEDir(key, 2);
                        count -= 2;
                    } else {
                        addEDir(key, 1);
                        count -= 1;
                    }
                }
            }
           // runs++;
        }
    }

    /**
     * @param bondDirections : amount of bonds in which direction
     * @param forced         : True = use pairs, False = Use 1's
     * @return A list contain a set order to iterate over
     */
    private List<String> getBestList(Map<String, Integer> bondDirections, boolean forced) {
        if (totalElectronCount > 3 || (totalElectronCount == 2 && !forced)) {
            if (bondDirections.get("n") != 0 || bondDirections.get("s") != 0) {
                return Arrays.asList("e", "w", "n", "s");
            } else if (bondDirections.get("e") != 0 || bondDirections.get("w") != 0) {
                return Arrays.asList("n", "s", "e", "w");
            } else {
                return Arrays.asList("n", "e", "s", "w");
            }
        } else {
            if (bondDirections.get("n") != 0 || bondDirections.get("s") != 0) {
                return Arrays.asList("n", "s", "e", "w");
            } else if (bondDirections.get("e") != 0 || bondDirections.get("w") != 0) {
                return Arrays.asList("e", "w", "n", "s");
            } else {
                return Arrays.asList("n", "e", "s", "w");
            }
        }
    }

    private void addEDir(String direction, int amount) {
        directionalValence.put(direction, directionalValence.get(direction) + amount);
    }

    @NotNull
    public ItemStack getStack(String direction) {
        ItemStack stack = new ItemStack(Items.VALENCEE);
        //TODO make item and overrides for model
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt(direction, directionalValence.get(direction));

        return stack;
    }

    public Integer getDirectionalValence(String direction) {
        return directionalValence.get(direction);
    }
}

