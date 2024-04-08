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

    public AtomPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hand == Hand.MAIN_HAND && !world.isClient) {
            // Use NBT to load INV
            AtomicInventory inventory = new AtomicInventory(user.getStackInHand(hand).getOrCreateNbt()) {
                // Save NBT on close (no unnecessary syncs)
                @Override
                public void onClose(PlayerEntity player) {
                    super.onClose(player);
                    NbtCompound nbt = player.getStackInHand(hand).getOrCreateNbt();
                    writeNbt(nbt);
                }
            };

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
            AtomicInventory storage_inventory = ((AtomicStorageBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos())).getInventory();
            AtomicInventory pack_inventory = new AtomicInventory(AtomicInventory.PACK_STACK);
            pack_inventory.onOpen(context.getPlayer());
            storage_inventory.tryToFill(pack_inventory);
            pack_inventory.onClose(context.getPlayer());
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
}
