package be.uantwerpen.scicraft.block;

import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import be.uantwerpen.scicraft.item.Items;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BohrBlock extends BlockWithEntity implements BlockEntityProvider {
    public BohrBlock() {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().strength(1f).nonOpaque());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BohrBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        BlockEntity blockEntity = world.getBlockEntity(pos);

        DefaultedList<ItemStack> neutronInventory = ((BohrBlockEntity) blockEntity).getNeutronInventory();

        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() == Items.NEUTRON) {
            for (int i = 0; i < 3; i++) {
                if (neutronInventory.get(i).isEmpty()) {
                    neutronInventory.set(i, (player.getStackInHand(hand).copy()));
                    neutronInventory.get(i).setCount(1);
                    return ActionResult.SUCCESS;
                }
                if (neutronInventory.get(i).getCount() < 64) {
                    neutronInventory.get(i).setCount(neutronInventory.get(i).getCount() + 1);
                    player.getStackInHand(hand).decrement(1);
                    System.out.println("Slot holds : " + neutronInventory.get(i));
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.SUCCESS;
    }
}
