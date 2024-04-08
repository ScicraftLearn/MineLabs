package be.minelabs.screen.slot;

import be.minelabs.item.Items;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class AtomSlot extends FilteredSlot{

    // If we want to manually use this type of slot (with costum filter)
    public AtomSlot(Inventory inventory, int index, int x, int y, Predicate<ItemStack> filter) {
        super(inventory, index, x, y, filter);
    }

    /**
     * Specific use inside of Atom Pack/Storage
     *
     * @param inventory : inventory to use
     * @param index : index (also atom index)
     * @param x : start x-coord of box
     * @param y : start y-coord of box
     */
    public AtomSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y, itemStack -> itemStack.getItem() == Items.ATOMS.get(index));
    }

    // We don't care about the max size of the Item Stack
    @Override
    public int getMaxItemCount(ItemStack stack) {
        return getMaxItemCount();
    }

    @Override
    public ItemStack takeStack(int amount) {
        return super.takeStack(Math.min(amount, 64));
    }
}
