package be.minelabs.screen;

import be.minelabs.block.entity.IonicBlockEntity;
import be.minelabs.recipe.ionic.IonicInventory;
import be.minelabs.screen.slot.CraftingResultSlot;
import be.minelabs.screen.slot.FilteredSlot;
import be.minelabs.screen.slot.LockableGridSlot;
import be.minelabs.item.Items;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class IonicBlockScreenHandler extends ScreenHandler {

    public static final int GRIDSIZE = 9;
    private final IonicInventory inventory;
    //The IonicBlockEntity
    private IonicBlockEntity ionic;
    //PropertyDelegate that holds the progress, density and charge of both sides.
    private final PropertyDelegate propertyDelegate;

    /**
     * This constructor gets called on the client when the server wants it to open the screenHandler<br>
     * The client will call the other constructor with an empty Inventory and the screenHandler will automatically
     * sync this empty inventory with the inventory on the server.
     * <p>
     * This constructor uses the buffer from {@link IonicBlockEntity#writeScreenOpeningData(ServerPlayerEntity, PacketByteBuf)}
     * This gives it the Blockpos of the BlockEntity
     */
    public IonicBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buff) {
        this(syncId, playerInventory, new IonicInventory(9, 9, 11), new ArrayPropertyDelegate(8), buff.readBlockPos());
    }

    /**
     * This constructor gets called from the BlockEntity on the server without calling the other constructor first,
     * the server knows the inventory of the container and can therefore directly provide it as an argument.
     * This inventory will then be synced to the client.
     *
     * @param syncId
     * @param playerInventory
     * @param inventory
     * @param propertyDelegate
     * @param pos
     */
    public IonicBlockScreenHandler(int syncId, PlayerInventory playerInventory, IonicInventory inventory, PropertyDelegate propertyDelegate, BlockPos pos) {
        super(ScreenHandlers.IONIC_SCREEN_HANDLER, syncId);
        checkSize(inventory, 29);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        BlockEntity be = playerInventory.player.world.getBlockEntity(pos);
        if (be instanceof IonicBlockEntity ionic) {
            this.ionic = ionic;
        }

        //Register properties for syncing
        this.addProperties(propertyDelegate);

        //Register slot listener, to reset the recipe if the items change, and sync
        this.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                if (slotId < GRIDSIZE * 2) {
                    setLeftCharge(inventory.getLeftGrid().getCharge());
                    setRightCharge(-inventory.getRightGrid().getCharge());

                    ionic.updateRecipe();
                    onGridChanged(playerInventory.player);
                }
                updateToClient();
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                if (property == 6 || property == 7) {
                    ionic.updateRecipe();
                }
            }
        });

        inventory.onOpen(playerInventory.player);

        addGridSlots();
        addIOSlots();
        addPlayerSlots(playerInventory);
    }

    /**
     * Callback used for advancements
     */
    private void onGridChanged(PlayerEntity player) {
        // TODO advancements
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    /**
     * Gets called when a player shift clicks while the screen is open
     *
     * @param player  Player that's shift clicking
     * @param invSlot SlotId of the clicked slot (-999 if outside of inventory, else -1 if not on slot)
     * @return The {@link ItemStack} to be left in the clicked slot
     */
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (invSlot < GRIDSIZE * 2) {
            slot.setStack(itemStack);
            return itemStack;
        }
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, GRIDSIZE * 2, this.inventory.size(), false)) { //start from slot GRIDSIZE, this is outside of the grid.
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }

    /**
     * Inserts the item into a slot, trying indexes from {@param startIndex} to {@param endIndex}.
     * If {@param fromLast}, it goes from {@param endIndex} to {@param startIndex}.
     *
     * @param stack
     * @param startIndex
     * @param endIndex
     * @param fromLast
     * @return
     */
    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        if (startIndex < GRIDSIZE * 2) {
            return false;
        }
        return super.insertItem(stack, startIndex, endIndex, fromLast);
    }

    /**
     * Called when dragging items in the inventory
     *
     * @param slot
     * @return
     */
    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        if (slot instanceof LockableGridSlot) {
            //We only want to block our ICT inventory (excluding the players)
            return false;
        }
        return super.canInsertIntoSlot(slot);
    }

    /**
     * @return Returns true if the input slots are empty
     */
    public boolean isInputEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!this.getSlot(i + (GRIDSIZE * 2)).getStack().isEmpty()) return false;
        }
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        return switch (id) {
            case 0 -> {
                // Clear btn
                if (isInputEmpty()) {
                    // clear grid + amount numbers
                    for (int i = 0; i < GRIDSIZE * 2; i++) {
                        inventory.setStack(i, ItemStack.EMPTY);
                    }
                    setLeftAmount(1);
                    setRightAmount(1);

                    inventory.markDirty();
                } else {
                    for (int i = 18; i < 27; i++) {
                        ItemStack itemStack = inventory.removeStack(i);
                        if (!player.getInventory().insertStack(itemStack)) {
                            player.dropItem(itemStack, false);
                        }
                    }
                }
                yield true;
            }
            case 1 -> {
                // LEFT MINUS
                int charge = getLeftAmount();
                if (charge <= 1) {
                    yield false;
                }
                setLeftAmount(charge - 1);
                yield true;
            }
            case 2 -> {
                // LEFT PLUS
                int charge = getLeftAmount();
                if (charge >= 9) {
                    yield false;
                }
                setLeftAmount(charge + 1);
                yield true;
            }
            case 3 -> {
                // RIGHT MINUS
                int charge = getRightAmount();
                if (charge <= 1) {
                    yield false;
                }
                setRightAmount(charge - 1);
                yield true;
            }
            case 4 -> {
                // RIGHT PLUS
                int charge = getRightAmount();
                if (charge >= 9) {
                    yield false;
                }
                setRightAmount(charge + 1);
                yield true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (slotIndex > 0 && slotIndex < GRIDSIZE * 2) {
            // ONLY GRID slots
            if (SlotActionType.CLONE == actionType && player.getAbilities().creativeMode && getCursorStack().isEmpty()) {
                Slot slot = this.slots.get(slotIndex);
                if (!slot.hasStack())
                    return;
                ItemStack itemStack2 = slot.getStack().getItem().getDefaultStack().copy();
                itemStack2.setCount(itemStack2.getMaxCount());
                this.setCursorStack(itemStack2);
                return;
            } else if (actionType == SlotActionType.PICKUP && getSlot(slotIndex).getStack() != ItemStack.EMPTY && getCursorStack() != ItemStack.EMPTY) {
                // Don't "swap" items
                getSlot(slotIndex).canInsert(getCursorStack());
                return;
            }
        }
        super.onSlotClick(slotIndex, button, actionType, player);
    }

    public DefaultedList<Ingredient> getIngredients() {
        return ionic.getIngredients();
    }

    public int getSplitIndex() {
        return ionic.getSplit();
    }

    public int getProgress() {
        return propertyDelegate.get(0);
    }

    public int getLeftDensity() {
        return propertyDelegate.get(1);
    }

    public int getRightDensity() {
        return propertyDelegate.get(2);
    }

    public void setLeftCharge(int amount) {
        propertyDelegate.set(3, amount);
    }

    public int getLeftCharge() {
        return propertyDelegate.get(3);
    }

    public void setRightCharge(int amount) {
        propertyDelegate.set(4, amount);
    }

    public int getRightCharge() {
        return propertyDelegate.get(4);
    }


    public int getLeftAmount() {
        return propertyDelegate.get(6);
    }

    public void setLeftAmount(int value) {
        if (value <= 1) {
            propertyDelegate.set(6, 1);
            return;
        }
        if (value >= 9) {
            propertyDelegate.set(6, 9);
            return;
        }
        propertyDelegate.set(6, value);
    }

    public int getRightAmount() {
        return propertyDelegate.get(7);
    }

    public void setRightAmount(int value) {
        if (value <= 1) {
            propertyDelegate.set(7, 1);
            return;
        }
        if (value >= 9) {
            propertyDelegate.set(7, 9);
            return;
        }
        propertyDelegate.set(7, value);
    }


    //A recipe is found (so the density is larger than 0)
    public boolean hasRecipe() {
        return this.getLeftDensity() > 0 && this.getRightDensity() > 0;
    }

    public IonicInventory getInventory() {
        return this.inventory;
    }

    private void addIOSlots() {
        int y = 107;
        //row of inputslots
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i + 18, 12 + i * 18, y) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    if (getIngredients().size() > this.getIndex() - GRIDSIZE * 2) {
                        return getIngredients().get(this.getIndex() - GRIDSIZE * 2).test(stack);
                    }
                    return false;
                }
            });
        }
        //erlemeyer slot
        this.addSlot(new FilteredSlot(inventory, 27, 178, y, s -> s.isOf(Items.ERLENMEYER)));
        //result slot
        this.addSlot(new CraftingResultSlot(inventory, 28, 178, 47));
    }

    private void addGridSlots() {
        //first 3x3 gridslots(left)
        //14 is the x position where the top-left corner of the square for the item needs to be drawn, 18 is height of 1 square
        //22 is the y position where the top-left corner of the square needs to be
        for (int i = 0; i < 3; i++) {
            for (int y = 0; y < 3; y++) {
                this.addSlot(new LockableGridSlot(inventory, i * 3 + y, 12 + y * 18, 28 + i * 18, stack -> true) {
                    @Override
                    public boolean isLocked() {
                        return !isInputEmpty();
                    }
                });
            }
        }
        //second 3x3 gridslots(right)
        for (int i = 0; i < 3; i++) {
            for (int y = 0; y < 3; y++) {
                this.addSlot(new LockableGridSlot(inventory, i * 3 + y + 9, 87 + y * 18, 28 + i * 18, stack -> true) {
                    @Override
                    public boolean isLocked() {
                        return !isInputEmpty();
                    }
                });
            }
        }
    }

    private void addPlayerSlots(PlayerInventory playerInventory) {
        //The player inventory (3x9 slots)
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 23 + l * 18, 139 + m * 18));
            }
        }
        //The player Hotbar (9 slots)
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 23 + m * 18, 197));
        }
    }

    public int getScaledProgress() {
        int progress = getProgress();
        int maxProgress = propertyDelegate.get(5);
        int arrowSize = 26;
        return maxProgress != 0 && progress != 0 ? progress * arrowSize / maxProgress : 0;
    }

    public boolean isCrafting() {
        return getProgress() > 0;
    }

    /*
     * 0: Invalid
     * 1: To many molecules
     * 2: Not implemented
     * 3: Valid
     */
    public int getStatus() {
        // TODO improve (to many MOL)
        if (inventory.getLeftGrid().isEmpty() || inventory.getRightGrid().isEmpty()) {
            return 0;
        }
        if (getLeftAmount() * getLeftCharge() + getRightAmount() * getRightCharge() == 0) {
            if (hasRecipe()) {
                // GREEN
                return 3;
            } else {
                // NOT IMPLEMENTED
                return 2;
            }
        }
        return 0;
    }
}
