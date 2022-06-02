package be.uantwerpen.scicraft.item;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.entity.ProtonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ProtonItem extends BlockItem {
    public ProtonItem(Item.Settings settings) {
        super(Blocks.PROTON, settings);
    }

//    /**
//     * When ProtonItem is right-clicked, use up the item if necessary and spawn the entity
//     *
//     * @param world minecraft world
//     * @param user  player invoking the right click action
//     * @param hand  the hand of the user
//     * @return TypedActionResult, indicates if the use of the item succeeded or not
//     */
//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack itemStack = user.getStackInHand(hand); // creates a new ItemStack instance of the user's itemStack in-hand
//
//        /* TODO sound effect of Proton throw
//         * Example with snowball sound
//         * world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 1F);
//         */
//
//        /* TODO cooldown on the throw of an proton (like the cooldown on Ender Pearls)
//         * Example cooldown of 5 ticks
//         * user.getItemCooldownManager().set(this, 5);
//         */
//        if (!world.isClient) {
//            // Spawns the proton entity with correct initial velocity (velocity has the same direction as the players looking direction)
//            ProtonEntity protonEntity = new ProtonEntity(world, user);
//            protonEntity.setItem(itemStack);
//            protonEntity.setVelocity(user, user.getPitch(), user.getYaw(), user.getRoll(), 1.5F, 0F);
//            world.spawnEntity(protonEntity);
//        }
//
//        user.incrementStat(Stats.USED.getOrCreateStat(this));
//        if (!user.getAbilities().creativeMode) {
//            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
//        }
//
//        return TypedActionResult.success(itemStack, world.isClient());
//    }
}
