package be.minelabs.mixin;

import be.minelabs.inventory.AtomicInventory;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"))
    public void injectItemPickup(PlayerEntity player, CallbackInfo ci){
        if (this.getStack().getItem() instanceof AtomItem && player.getInventory().containsAny(Set.of(Items.ATOM_PACK))){
            // TODO TRY TO INSERT INTO ATOM PACK
            for (ItemStack atom_pack_stack : getPack(player.getInventory())) {

                AtomicInventory inv = new AtomicInventory(atom_pack_stack.getNbt()); // Load the inv from stack
                inv.addStack(getStack()); // Try addin stack to inv
                atom_pack_stack.setNbt(inv.writeNbt(new NbtCompound())); // Save inv to stack

                // Should stop after Atom stack is empty
            }
        }
    }

    private Set<ItemStack> getPack(PlayerInventory inventory){
        Set<ItemStack> set = new HashSet<>();
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isOf(Items.ATOM_PACK)){
                set.add(inventory.getStack(i));
            }
        }
        return set;
    }
}
