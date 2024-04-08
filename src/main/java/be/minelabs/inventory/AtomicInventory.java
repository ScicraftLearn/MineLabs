package be.minelabs.inventory;

import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class AtomicInventory extends SimpleInventory {

    // TODO FIX HOPPER MAX STACK 64
    //  it uses the stack.getMaxStackSize() instead of the inventory's
    public static final int STORAGE_STACK = 512;
    public static final int PACK_STACK = 256;


    // Allows stacks with more then 64 inside of the inventory
    private final int MAX_SIZE;

    public AtomicInventory(int stack_size) {
        super(Items.ATOMS.size());
        MAX_SIZE = stack_size;
    }

    public AtomicInventory(NbtCompound nbt) {
        this(PACK_STACK);
        this.readNbt(nbt);
    }

    /**
     * Try to fill THIS inventory with the origin
     *
     * @param origin : Inventory to TAKE from
     */
    public void tryToFill(AtomicInventory origin) {
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).getCount() == getMaxCountPerStack() || origin.getStack(i).isEmpty()) {
                // Slot if FULL || Nothing to fill with
                continue;
            } else {
                // Try to fill
                if (stacks.get(i).isEmpty()) {
                    stacks.set(i, origin.getStack(i).copy());
                    origin.stacks.set(i, ItemStack.EMPTY);
                } else {
                    transfer(origin.stacks.get(i), stacks.get(i));
                }
            }
        }
    }

    /**
     * Try to add the given stack to the inv
     *
     * @param stack : Stack to add (can only be of AtomItem)
     * @return ItemStack.EMPTY if fully inserted OR remainder of the stack
     */
    @Override
    public ItemStack addStack(ItemStack stack) {
        int atomicIndex = getAtomicIndex(stack);
        if (stack.getItem() instanceof AtomItem) {
            ItemStack inv_stack = stacks.get(atomicIndex);
            if (inv_stack.isEmpty()) {
                // Empty stack
                setStack(atomicIndex, stack.copy());
                stack.setCount(0);
                return ItemStack.EMPTY;
            } else {
                // Not an empty stack
                if (ItemStack.canCombine(inv_stack, stack)) {
                    this.transfer(stack, inv_stack);
                    if (stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return stack;
    }

    /**
     * Remove a given amount if possible from the inventory
     *
     * @param ingredient : what item type to remove
     * @param count      : amount to remove
     * @return return what needs to be removed.
     */
    public int removeStack(Ingredient ingredient, int count) {
        for (ItemStack stack : stacks) {
            if (ingredient.test(stack)) {
                int amount = stack.getCount();
                stack.decrement(count);
                return amount >= count ? 0 : count - amount;
            }
        }
        return count;
    }

    /**
     * Transfer source stack into target stack
     *
     * @param source : ItemStack to take from
     * @param target : ItemStack to increase
     */
    private void transfer(ItemStack source, ItemStack target) {
        int j = Math.min(source.getCount(), getMaxCountPerStack() - target.getCount());
        if (j > 0) {
            target.increment(j);
            source.decrement(j);
            this.markDirty();
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return MAX_SIZE;
    }

    public void readNbt(NbtCompound nbt) {
        NbtList nbtList = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 0xFF;
            if (j < 0 || j >= stacks.size()) continue;

            ItemStack stack = new ItemStack(
                    Registries.ITEM.get(new Identifier(nbtCompound.getString("id"))),
                    nbtCompound.getShort("Count"));
            stacks.set(j, stack);
        }
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList nbtList = new NbtList();
        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            if (itemStack.isEmpty()) continue;
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte) i);

            Identifier identifier = Registries.ITEM.getId(itemStack.getItem());
            nbtCompound.putString("id", identifier == null ? "minecraft:air" : identifier.toString());
            nbtCompound.putShort("Count", (short) itemStack.getCount()); // Ensure Count is a short instead of BYTE (max 127)

            nbtList.add(nbtCompound);
        }
        nbt.put("Items", nbtList);
        return nbt;
    }

    /**
     * Check if the inventory has the ingredient and the correct amount
     *
     * @param ingredient : ingredient/Item to compare
     * @param amount     : amount to be present in inverntory
     * @return boolean, true/false
     */
    public boolean contains(Ingredient ingredient, int amount) {
        for (ItemStack stack : stacks) {
            if (ingredient.test(stack) && stack.getCount() >= amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * What is allowed to be inserted
     *
     * @param stack : try to insert
     * @return boolean
     */
    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof AtomItem;
    }

    /**
     * Get the index of the stack in the AtomicInventory
     *
     * @param stack : stack to get index for
     * @return integer: atomic number - 1 OR -1 (on fail)
     */
    private int getAtomicIndex(ItemStack stack) {
        if (stack.getItem() instanceof AtomItem item) {
            return (item.getAtom().getAtomNumber() - 1);
        }
        return -1;
    }

    /**
     * Can insert stack into slot
     *
     * @param slot  : slot to insert into
     * @param stack : stack to insert
     * @return boolean
     */
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        int atomic = getAtomicIndex(stack);
        return atomic != -1 && atomic == slot;
    }

}
