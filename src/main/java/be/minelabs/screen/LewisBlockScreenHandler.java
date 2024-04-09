package be.minelabs.screen;

import be.minelabs.advancement.criterion.Criteria;
import be.minelabs.block.entity.LewisBlockEntity;
import be.minelabs.item.items.AtomItem;
import be.minelabs.recipe.lewis.LewisCraftingGrid;
import be.minelabs.recipe.molecules.Bond;
import be.minelabs.recipe.molecules.MoleculeGraph;
import be.minelabs.inventory.OrderedInventory;
import be.minelabs.screen.slot.CraftingResultSlot;
import be.minelabs.screen.slot.FilteredSlot;
import be.minelabs.screen.slot.LockableGridSlot;
import be.minelabs.item.Items;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
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
import org.jetbrains.annotations.NotNull;

public class LewisBlockScreenHandler extends ScreenHandler {

    // size of inventory without grid
    private final int INVENTORY_SIZE = 11;
    public static final int GRIDSIZE = 25;

    private final LewisCraftingGrid craftingGrid;

    private final Inventory ioInventory;

    //PropertyDelegate that holds the progress and density
    private final PropertyDelegate propertyDelegate;

    //The LewisBlockEntity that belongs to the screen. Can be used to get extra data, as long as that data is synced
    private LewisBlockEntity lewis;


    /**
     * This constructor gets called on the client when the server wants it to open the screenHandler<br>
     * The client will call the other constructor with an empty Inventory and the screenHandler will automatically
     * sync this empty inventory with the inventory on the server.
     * <p>
     * This constructor uses the buffer from {@link LewisBlockEntity#writeScreenOpeningData(ServerPlayerEntity, PacketByteBuf)}
     * This gives it the Blockpos of the BlockEntity
     *
     * @param syncId
     * @param playerInventory
     * @param buf
     */
    public LewisBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new LewisCraftingGrid(5, 5), new OrderedInventory(11), new ArrayPropertyDelegate(3), buf.readBlockPos());
    }

    /**
     * This constructor gets called from the BlockEntity on the server without calling the other constructor first,
     * the server knows the inventory of the container and can therefore directly provide it as an argument.
     * This inventory will then be synced to the client.
     *
     * @param syncId           ID used to sync client and server handlers
     * @param playerInventory  Player's inventory to sync with screen's inventory slots
     * @param propertyDelegate PropertyDelegate is used to sync data across server and client side handlers
     */
    public LewisBlockScreenHandler(int syncId, @NotNull PlayerInventory playerInventory, LewisCraftingGrid craftingGrid, Inventory ioInventory, PropertyDelegate propertyDelegate, BlockPos pos) {
        super(ScreenHandlers.LEWIS_SCREEN_HANDLER, syncId);
        checkSize(ioInventory, 10);
        this.craftingGrid = craftingGrid;
        this.ioInventory = ioInventory;
        this.propertyDelegate = propertyDelegate;
        BlockEntity be = playerInventory.player.world.getBlockEntity(pos);
        if (be instanceof LewisBlockEntity lewis) {
            this.lewis = lewis;
        }

        //Register properties for syncing
        this.addProperties(propertyDelegate);

        //Register slot listener, to reset the recipe if the items change, and sync
        this.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                if (slotId < GRIDSIZE) {
                    lewis.updateRecipe();
                    onGridChangedByPlayer(playerInventory.player);
                }
                handler.updateToClient();
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                //maybe use later
                handler.updateToClient();
            }
        });

        //some inventories do custom logic when a player opens it.
        craftingGrid.onOpen(playerInventory.player);
        ioInventory.onOpen(playerInventory.player);

        //This will place the slot in the correct locations. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job

        addGridSlots();
        addIOSlots();
        addPlayerSlots(playerInventory);

    }

    private void addGridSlots() {
        // Lewis Crafting Table Inventory (5x5 grid)
        for (int m = 0; m < 5; ++m) {
            for (int l = 0; l < 5; ++l) {
                this.addSlot(new LockableGridSlot(craftingGrid, l + m * 5, 8 + l * 18, m * 18 + 18, stack -> {
                    if (stack.getItem() instanceof AtomItem atom) {
                        return switch (atom.getAtom().getType()) {
                            case POST_TRANSITION_METAL, NON_METAL, NOBLE_GAS -> true;
                            default -> false;
                        };
                    }
                    return false;
                }) {//Anonymous implementation to link it to the slots.
                    @Override
                    public boolean isLocked() {
                        return !isInputEmpty(); //Locked if the input had items
                    }
                });
            }
        }
    }

    private void addIOSlots() {
        // Lewis Crafting Table Inventory (9 input slots)
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(ioInventory, m, 8 + m * 18, 5 * 18 + 23) {//Anonymous implementation to link it to the slots.
                @Override
                public boolean isEnabled() {
                    return hasRecipe();
                }

                @Override
                public boolean canInsert(ItemStack stack) {
                    if (getIngredients().size() > this.getIndex()) {
                        return getIngredients().get(this.getIndex()).test(stack);
                    }
                    return false;
                }
            });
        }

        // Lewis Crafting Table Inventory (1 slot for erlenmeyer)
        this.addSlot(new FilteredSlot(ioInventory, 9, 8 + 7 * 18, 2 * 18 + 54, s -> s.isOf(Items.ERLENMEYER)));

        // Lewis Crafting Table Inventory (1 output slot)
        this.addSlot(new CraftingResultSlot(ioInventory, 10, 8 + 7 * 18, 2 * 18 + 18));
    }

    private void addPlayerSlots(PlayerInventory playerInventory) {
        //The player inventory (3x9 slots)
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 145 + m * 18));
            }
        }
        //The player Hotbar (9 slots)
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 203));
        }
    }

    public Inventory getIoInventory() {
        return ioInventory;
    }

    /**
     * Check if a player has access to the inventory and thus the BlockEntity
     *
     * @param player Player to check access for
     * @return Returns true if the player has access
     */
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.ioInventory.canPlayerUse(player);
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
        if (invSlot < GRIDSIZE) {
            slot.setStack(itemStack);
            return itemStack;
        }
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (invSlot < GRIDSIZE + INVENTORY_SIZE) {
                if (!this.insertItem(itemStack2, GRIDSIZE + INVENTORY_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, GRIDSIZE, GRIDSIZE + INVENTORY_SIZE, false)) { //start from slot GRIDSIZE, this is outside of the grid.
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
     * Callback used for advancements
     */
    private void onGridChangedByPlayer(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity serverPlayer))
            return;
        MoleculeGraph structure = getLewisCraftingGrid().getPartialMolecule().getStructure();

        boolean[] foundOrders = {false, false, false};
        for (MoleculeGraph.Edge edge : structure.getEdges()) {
            Bond bond = edge.data;
            foundOrders[bond.bondOrder - 1] = true;
        }
        for (int order = 1; order < foundOrders.length + 1; order++) {
            if (foundOrders[order - 1]) {
                Criteria.LCT_CRITERION.trigger(serverPlayer, order);
            }
        }
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
        if (startIndex < GRIDSIZE) {
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
            //We only want to block our LCT inventory (excluding the players)
            return false;
        }
        return super.canInsertIntoSlot(slot);
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

    /**
     * @return Returns true if the input slots are empty
     */
    public boolean isInputEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!this.getSlot(i + GRIDSIZE).getStack().isEmpty()) return false;
        }
        return true;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return lewis.getIngredients();
    }

    public int getDensity() {
        return propertyDelegate.get(2);
    }

    //A recipe is found (so the density is larger than 0)
    public boolean hasRecipe() {
        return this.getDensity() > 0;
    }

    public LewisCraftingGrid getLewisCraftingGrid() {
        return craftingGrid;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        // Change if to switch/case when more buttons are present
        if (id == 0) {
            if (isInputEmpty()) {
                for (int i = 0; i < LewisBlockScreenHandler.GRIDSIZE; i++) {
                    craftingGrid.removeStack(i);
                }
                craftingGrid.markDirty();
            } else {
                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = ioInventory.removeStack(i);
                    if (!player.getInventory().insertStack(itemStack)) {
                        player.dropItem(itemStack, false);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = propertyDelegate.get(0);
        int maxProgress = propertyDelegate.get(1);
        int arrowSize = 26;
        return maxProgress != 0 && progress != 0 ? progress * arrowSize / maxProgress : 0;
    }

    public int getStatus() {
        if (propertyDelegate.get(2) > 0) {
            // Density found -> json recipe found
            return 2;
        } else if (craftingGrid.isEmpty() || craftingGrid.getPartialMolecule().getStructure().getTotalOpenConnections() != 0) {
            // Empty grid or still has possible connections
            return 0;
        } else if (craftingGrid.getPartialMolecule().getStructure().isConnectedManagerFunctieOmdatJoeyZaagtZoalsVaak()) {
            return 1;
        } else {
            return 3;
        }
    }
}
