package be.minelabs.mixin;

import be.minelabs.inventory.AtomicInventory;
import be.minelabs.item.Items;
import be.minelabs.item.items.AtomItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();


    @Shadow public abstract void setStack(ItemStack stack);

    @Shadow public abstract boolean cannotPickup();

    @Shadow public abstract @Nullable Entity getOwner();

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getCount()I"))
    public void injectItemPickup(PlayerEntity player, CallbackInfo ci){
        Entity owner = getOwner();
        if (!cannotPickup() && (owner == null || owner.getUuid().equals(player.getUuid()))){ // BASE MINECRAFT PICKUP

            if (this.getStack().getItem() instanceof AtomItem && player.getInventory().containsAny(Set.of(Items.ATOM_PACK))){
                for (ItemStack atom_pack_stack : getPack(player.getInventory())) {

                    AtomicInventory inv = new AtomicInventory(atom_pack_stack.getOrCreateNbt()); // Load the inv from stack
                    setStack(inv.addStack(getStack())); // Try adding stack to inv && SET itemEntity stack to the returned enity
                    atom_pack_stack.setNbt(inv.writeNbt(new NbtCompound())); // Save inv to stack
                    // Pickup sound / advancement / stats are no longer updated
                    if (getStack().isEmpty()) {
                        break;
                    }
                }
            }
        }
    }

    @Unique
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
