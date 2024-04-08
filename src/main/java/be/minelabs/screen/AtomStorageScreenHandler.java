package be.minelabs.screen;

import be.minelabs.inventory.AtomicInventory;
import be.minelabs.item.Items;
import be.minelabs.screen.slot.AtomSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;

public class AtomStorageScreenHandler extends ScreenHandler {

    private final AtomicInventory inventory;

    public AtomStorageScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new AtomicInventory(AtomicInventory.STORAGE_STACK));
    }

    public AtomStorageScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlers.ATOM_STORAGE_SCREEN_HANDLER, syncId);
        checkSize(inventory, Items.ATOMS.size());
        this.inventory = (AtomicInventory) inventory;
        this.inventory.onOpen(playerInventory.player);

        addAtomSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            if (player.getMainHandStack().isOf(Items.ATOM_PACK)) {
                // Don't allow the pack to be moved
                if (ItemStack.areEqual(slot.getStack(), player.getMainHandStack())) {
                    return ItemStack.EMPTY; // SOMETHING ELSE ??
                }
            }

            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                // Atomic -> player
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                // Player -> Atomic
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        ItemStack itemStack;
        Slot slot;
        boolean bl = false;
        int i = startIndex;
        if (fromLast) {
            i = endIndex - 1;
        }
        if (stack.isStackable()) {
            while (!stack.isEmpty() && (fromLast ? i >= startIndex : i < endIndex)) {
                slot = this.slots.get(i); // Slot to insert into
                itemStack = slot.getStack();
                if (!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
                    int j = itemStack.getCount() + stack.getCount();
                    if (j <= slot.getMaxItemCount()) {
                        stack.setCount(0);
                        itemStack.setCount(j);
                        slot.markDirty();
                        bl = true;
                    } else if (itemStack.getCount() < slot.getMaxItemCount()) {
                        stack.decrement(stack.getMaxCount() - itemStack.getCount());
                        itemStack.setCount(stack.getMaxCount());
                        slot.markDirty();
                        bl = true;
                    }
                }
                if (fromLast) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        if (!stack.isEmpty()) {
            i = fromLast ? endIndex - 1 : startIndex;
            while (fromLast ? i >= startIndex : i < endIndex) {
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    if (stack.getCount() > slot.getMaxItemCount()) {
                        slot.setStack(stack.split(slot.getMaxItemCount()));
                    } else {
                        slot.setStack(stack.split(stack.getCount()));
                    }
                    slot.markDirty();
                    bl = true;
                    break;
                }
                if (fromLast) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        return bl;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        if (slot.inventory instanceof AtomicInventory) {
            return false;
        }
        return super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        // DONT BLOCK : SLOTINDEX < 0
        // USED FOR SHIFT/DRAG/THROW
        if (slotIndex > 0 && player.getMainHandStack().isOf(Items.ATOM_PACK)
                && ItemStack.areEqual(slots.get(slotIndex).getStack(), player.getMainHandStack())) {
            // Don't allow the pack to be moved
            return; // SOMETHING ELSE ??
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        inventory.onClose(player);
    }

    private void addAtomSlots() {
        this.addSlot(new AtomSlot(inventory, 0, 8, 7));
        this.addSlot(new AtomSlot(inventory, 1, 322, 7));

        int index = 2;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 18; j++) {
                int offset = 8;

                if (j > 1 && j <= 16) {
                    offset += 4;
                } else if (j > 16) {
                    offset += 8;
                }
                if (i < 2 && j > 1 && j < 12)
                    continue;

                if (index == 56 || index == 88) {
                    for (int k = 0; k < 15; k++) {
                        this.addSlot(new AtomSlot(inventory, index, 48 + k * 18, index < 87 ? 137 : 155));
                        index++;
                    }
                } else {
                    this.addSlot(new AtomSlot(inventory, index, offset + j * 18, 25 + i * 18));
                    index++;
                }
            }
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 84 + l * 18, 192 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 84 + i * 18, 250));
        }
    }
}
