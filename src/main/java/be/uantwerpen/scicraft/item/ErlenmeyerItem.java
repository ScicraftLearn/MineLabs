package be.uantwerpen.scicraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ErlenmeyerItem extends Item implements FluidModificationItem, MoleculeItem {

    private final String molecule;

    public ErlenmeyerItem(Settings settings, String molecule) {
        super(settings);
        this.molecule = molecule;
    }

    @Override
    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        FluidModificationItem.super.onEmptied(player, world, stack, pos);
    }

    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        return false;
    }

    @Override
    public String getMolecule() {
        return molecule;
    }
}
