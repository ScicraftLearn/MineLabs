package be.minelabs.screen.slot;

import be.minelabs.item.items.AtomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

import java.util.function.Predicate;

/**
 * Lockable slot
 * Only allows changes if unlocked
 */
public class LockableGridSlot extends Slot {

    private final Predicate<ItemStack> predicate;

    public LockableGridSlot(Inventory inventory, int index, int x, int y, Predicate<ItemStack> predicate) {
        super(inventory, index, x, y);
        this.predicate = predicate;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (this.isLocked()) return false;
        if (stack == null || stack.getItem().equals(Items.AIR)) {
            this.setStack(stack);
            return true;
        }
        if (predicate.test(stack)) {
            AtomItem atom = (AtomItem) stack.getItem();
            this.setStack(atom.getDefaultStack());
            this.getStack().getOrCreateNbt().putBoolean("MinelabsItemInLCT", true);
            return true;
        }
        return false;
    }

    public boolean isLocked() {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        if (this.isLocked()) return false;
        return super.canTakeItems(playerEntity);
    }

    @Override
    public ItemStack takeStack(int amount) {
        this.inventory.removeStack(this.getIndex(), amount);
        return ItemStack.EMPTY;
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
