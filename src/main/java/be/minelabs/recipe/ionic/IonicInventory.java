package be.minelabs.recipe.ionic;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

/**
 * Inventory which can give 2 Lewisgrids.
 */
public class IonicInventory extends SimpleInventory {

    private final IonicCraftingGrid left_grid;
    private final IonicCraftingGrid right_grid;


    public IonicInventory(int gridLeft, int gridRight, int other) {
        super(gridLeft + gridRight + other);
        left_grid = new IonicCraftingGrid(3, 3);
        right_grid = new IonicCraftingGrid(3, 3);

        addListener(sender -> {
            for (int i = 0; i < 18; i++) {
                if (i < 9) {
                    left_grid.setStack(i, sender.getStack(i));
                } else {
                    right_grid.setStack(i-9, sender.getStack(i));
                }
            }
            left_grid.markDirty();
            right_grid.markDirty();
        });
    }

    public IonicCraftingGrid getLeftGrid() {
        return left_grid;
    }

    public IonicCraftingGrid getRightGrid() {
        return right_grid;
    }

    public ItemStack getContainerStack() {
        return getStack(27);
    }

    public ItemStack getOutputStack() {
        return getStack(28);
    }

    public Inventory getIO() {
        return new SimpleInventory(stacks.stream().skip(18).toArray(ItemStack[]::new));
    }
}
