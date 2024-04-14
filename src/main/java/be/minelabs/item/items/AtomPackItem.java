package be.minelabs.item.items;

import be.minelabs.block.Blocks;
import be.minelabs.block.entity.AtomicStorageBlockEntity;
import be.minelabs.inventory.AtomicInventory;
import be.minelabs.screen.AtomStorageScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AtomPackItem extends Item {

    public static final int STACK_SIZE = 256;
    private static final String NBT_INVENTORY_KEY = "Items";

    public AtomPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hand == Hand.MAIN_HAND && !world.isClient) {
            // Use NBT to load INV
            AtomicInventory inventory = getInventory(user.getStackInHand(hand));

            user.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return Text.translatable(getTranslationKey());
                }

                @Nullable
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new AtomStorageScreenHandler(syncId, playerInventory, inventory);
                }
            });
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        // Stops the bobbing of the item when the field gets updated
        return false;
    }

    /**
     * Transfer Item from Atom Pack to Atom Storage (All other usages are skipped)
     *
     * @param context the usage context
     * @return ActionResult
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.ATOMIC_STORAGE && context.getPlayer().isSneaking()) {
            AtomicInventory blockInventory = ((AtomicStorageBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos())).getInventory();
            AtomicInventory packInventory = getInventory(context.getStack());
            packInventory.onOpen(context.getPlayer());
            for (ItemStack stack : packInventory.getStacks()){
                packInventory.setStack(blockInventory.addStack(stack));
            }
            packInventory.onClose(context.getPlayer());
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    public static AtomicInventory getInventory(ItemStack pack){
        // TODO: issue: when multiple inventories open (screen and item pickup from world for example), they don't sync and overwrite each other's changes.
        AtomicInventory inventory = new AtomicInventory(STACK_SIZE);
        NbtCompound nbt = pack.getSubNbt(NBT_INVENTORY_KEY);
        if (nbt != null)
            inventory.readNbt(nbt);
        inventory.addListener(i -> {
            pack.setSubNbt(NBT_INVENTORY_KEY, inventory.writeNbt());
        });
        return inventory;
    }

    public static int insert(ItemStack pack, ItemStack toInsert){
        AtomicInventory inv = getInventory(pack);
        ItemStack leftover = inv.addStack(toInsert);
        return leftover.getCount();
    }

}
