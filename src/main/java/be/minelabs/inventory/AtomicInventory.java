package be.minelabs.inventory;

import be.minelabs.item.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class AtomicInventory extends SimpleInventory {

    // Allows stacks with more then 64 inside of the inventory
    private final int MAX_SIZE;

    public AtomicInventory(int stack_size) {
        super(Items.ATOMS.size());
        MAX_SIZE = stack_size;
    }

    public AtomicInventory(NbtCompound nbt){
        this(256);
        this.readNbt(nbt);
    }

    // Save inventory to NBT if it's the Atom Pack
    @Override
    public void onClose(PlayerEntity player) {
        super.onClose(player);
        if (player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.ATOM_PACK && MAX_SIZE == 256){
            NbtCompound nbt = player.getStackInHand(Hand.MAIN_HAND).getOrCreateNbt();
            this.writeNbt(nbt);
        }
    }

    // Load inventory from NBT if it's the Atom Pack
    @Override
    public void onOpen(PlayerEntity player) {
        if (player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.ATOM_PACK && MAX_SIZE == 256){
            readNbt(player.getStackInHand(Hand.MAIN_HAND).getOrCreateNbt());
        }
        super.onOpen(player);
    }

    /**
     * Try to fill THIS inventory with the origin
     *
     * @param origin : Inventory to TAKE from
     */
    public void tryToFill(AtomicInventory origin){
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).getCount() == getMaxCountPerStack() || origin.getStack(i).isEmpty()){
                // Slot if FULL || Nothing to fill with
                continue;
            } else {
                // Try to fill
                int count = origin.getStack(i).getCount();
                if (stacks.get(i).isEmpty()){
                    stacks.set(i, origin.getStack(i).copy());
                    origin.stacks.set(i, ItemStack.EMPTY);
                } else if (stacks.get(i).getCount() + count > MAX_SIZE){
                    origin.stacks.get(i).decrement(MAX_SIZE-stacks.get(i).getCount());
                    stacks.get(i).setCount(MAX_SIZE);
                } else {
                    stacks.get(i).increment(count);
                    origin.stacks.set(i, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return MAX_SIZE;
    }

    public void readNbt(NbtCompound nbt){
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

    public NbtCompound writeNbt(NbtCompound nbt){
        NbtList nbtList = new NbtList();
        for (int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            if (itemStack.isEmpty()) continue;
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);

            Identifier identifier = Registries.ITEM.getId(itemStack.getItem());
            nbtCompound.putString("id", identifier == null ? "minecraft:air" : identifier.toString());
            nbtCompound.putShort("Count", (short) itemStack.getCount()); // Ensure Count is a short instead of BYTE (max 127)

            nbtList.add(nbtCompound);
        }
        nbt.put("Items", nbtList);
        return nbt;
    }
}
