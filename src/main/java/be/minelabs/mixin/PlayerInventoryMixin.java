package be.minelabs.mixin;

import be.minelabs.inventory.AtomicInventory;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import be.minelabs.item.items.AtomPackItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow public int selectedSlot;

    @Shadow @Final public DefaultedList<ItemStack> main;

    @Shadow public abstract ItemStack getStack(int slot);

    @Shadow public abstract boolean contains(ItemStack stack);

    @Inject(method = "addStack(Lnet/minecraft/item/ItemStack;)I", at = @At(value = "HEAD", target = "Lnet/minecraft/entity/player/PlayerInventory;addStack(Lnet/minecraft/item/ItemStack;)I"), cancellable = true)
    public void injectAddStack(ItemStack stack, CallbackInfoReturnable<Integer> ci){
        // Inject for AtomItem pickup going to atompack. This function is called in a loop until the stack is fully picked up.
        if (!(stack.getItem() instanceof AtomItem)) return;

        Optional<ItemStack> pack = getAtomPackWithRoomForStack(stack);
        if (pack.isEmpty()) return;

        int i = AtomPackItem.insert(pack.get(), stack);

        if (i < stack.getCount())
            ci.setReturnValue(i);
    }

    @Unique
    private boolean isAtomPackWithRoomForStack(ItemStack pack, ItemStack stack){
        if (!pack.isOf(Items.ATOM_PACK)) return false;
        AtomicInventory inventory = AtomPackItem.getInventory(pack);
        return inventory.getRoomFor(stack.getItem()) > 0;
    }

    @Unique
    private Optional<ItemStack> getAtomPackWithRoomForStack(ItemStack stack){
        // Based on PlayerInventory::getOccupiedSlotWithRoomForStack (priority for insertion)
        return IntStream.concat(IntStream.of(selectedSlot, 40), IntStream.range(0, main.size()))
                .mapToObj(this::getStack)
                .filter(pack -> isAtomPackWithRoomForStack(pack, stack))
                .findFirst();
    }
}
