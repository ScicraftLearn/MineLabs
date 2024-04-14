package be.minelabs.inventory;

import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import be.minelabs.science.Atom;
import com.google.common.collect.Lists;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class AtomicInventory implements Inventory {

    // TODO FIX HOPPER MAX STACK 64
    //  it uses the stack.getMaxStackSize() instead of the inventory's


    // Allows stacks with more then 64 inside of the inventory
    private final int stackSize;
    private final List<InventoryChangedListener> listeners = Lists.newArrayList();

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(Items.ATOMS.size(), ItemStack.EMPTY);

    public AtomicInventory(int stackSize) {
        this.stackSize = stackSize;
    }

    public void addListener(InventoryChangedListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(InventoryChangedListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void markDirty() {
        for (InventoryChangedListener inventoryChangedListener : this.listeners) {
            inventoryChangedListener.onInventoryChanged(this);
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        // Trigger additional call to listeners
        // When ItemStacks are edited directly, this does not trigger a listener call, but the state is changed.
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(inventory, slot, amount);
        if (!result.isEmpty())
            markDirty();
        return result;
    }


    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = inventory.get(slot);
        if (itemStack.isEmpty())
            return ItemStack.EMPTY;

        inventory.set(slot, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (!stack.isEmpty() && stackToSlot(stack) != slot)
            throw new IllegalArgumentException("Trying to set " + stack + " to slot " + slot + " of atom storage which does not correspond");

        inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack())
            stack.setCount(this.getMaxCountPerStack());

        this.markDirty();
    }

    @Override
    public int getMaxCountPerStack() {
        return stackSize;
    }

    public void readNbt(NbtCompound nbt) {
        nbt.getKeys().forEach(symbol -> {
            Atom atom = Atom.getBySymbol(symbol);
            if (atom == null) return;
            int slot = itemToSlot(atom.getItem());
            inventory.set(slot, new ItemStack(atom.getItem(), nbt.getInt(symbol)));
        });
    }

    public NbtCompound writeNbt() {
        NbtCompound ret = new NbtCompound();
        for (ItemStack stack : inventory) {
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof AtomItem atomItem)
                ret.putInt(atomItem.getAtom().getSymbol(), stack.getCount());
        }

        return ret;
    }

    public int getRoomFor(Item item){
        int slot = itemToSlot(item);
        if (slot == -1) return 0;
        return getMaxCountPerStack() - getStack(slot).getCount();
    }

    /**
     * Add as much as possible from stack to inventory. Returns what couldn't be added.
     */
    public ItemStack addStack(ItemStack stack){
        ItemStack itemStack = stack.copy();
        int slot = stackToSlot(stack);
        int toTransfer = Math.min(itemStack.getCount(), getRoomFor(stack.getItem()));
        if (toTransfer > 0){
            inventory.get(slot).setCount(inventory.get(slot).getCount() + toTransfer);
            itemStack.split(toTransfer);
            markDirty();
        }
        return itemStack;
    }

    public List<ItemStack> getStacks(){
        return inventory;
    }

    public void setStack(ItemStack stack){
        setStack(stackToSlot(stack), stack);
    }

    /**
     * Get the index of the stack in the AtomicInventory
     */
    private static int stackToSlot(ItemStack stack) {
        return itemToSlot(stack.getItem());
    }

    private static int itemToSlot(Item item){
        if (item instanceof AtomItem atomItem) {
            return (atomItem.getAtom().getAtomNumber() - 1);
        }
        return -1;
    }

    /**
     * Can insert stack into slot
     */
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        int atomic = stackToSlot(stack);
        return atomic == slot;
    }

    @Override
    public int count(Item item) {
        return inventory.get(itemToSlot(item)).getCount();
    }

    @Override
    public void clear() {
        inventory.forEach(s -> s.setCount(0));
    }
}
