package be.uantwerpen.minelabs.item;

import be.uantwerpen.minelabs.block.Blocks;
import be.uantwerpen.minelabs.entity.ElectronEntity;
import be.uantwerpen.minelabs.entity.PositronEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PositronItem extends BlockItem {
    public PositronItem(Settings settings) {
        super(Blocks.POSITRON, settings);
    }

    //@Override
    //public ActionResult useOnBlock(ItemUsageContext context) {
    //    context.getWorld().setBlockState(new ItemPlacementContext(context).getBlockPos(), Blocks.POSITRON.getDefaultState());
    //    return ActionResult.SUCCESS;
    //}

    /**
     * When PositronItem is right-clicked, use up the item if necessary and spawn the entity
     *
     * @param world minecraft world
     * @param user  player invoking the right click action
     * @param hand  the hand of the user
     * @return TypedActionResult, indicates if the use of the item succeeded or not
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand); // creates a new ItemStack instance of the user's itemStack in-hand

        /* TODO sound effect of Positron throw
         * Example with snowball sound
         * world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 1F);
         */

        /* TODO cooldown on the throw of an Positron (like the cooldown on Ender Pearls)
         * Example cooldown of 5 ticks
         * user.getItemCooldownManager().set(this, 5);
         */
        if (!world.isClient) {
            // Spawns the positron entity with correct initial velocity (velocity has the same direction as the players looking direction)
            PositronEntity positronEntity = new PositronEntity(world, user);
            positronEntity.setItem(itemStack);
            positronEntity.setVelocity(user, user.getPitch(), user.getYaw(), user.getRoll(), 1.5F, 0F);
            world.spawnEntity(positronEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
