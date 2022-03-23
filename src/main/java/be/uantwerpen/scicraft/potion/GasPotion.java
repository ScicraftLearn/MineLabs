package be.uantwerpen.scicraft.potion;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.collection.DefaultedList;

public class GasPotion extends LingeringPotionItem {
    public GasPotion(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        Potion potion = new Potion("Test");
        stacks.add(PotionUtil.setPotion(new ItemStack(this), potion));
    }
}
