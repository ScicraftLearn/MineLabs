package be.minelabs.client.mixin;

import be.minelabs.world.dimension.ModDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method="Lnet/minecraft/client/network/ClientPlayerInteractionManager;getReachDistance()F", at=@At("HEAD"), cancellable = true)
    public void injectGetReachDistance(CallbackInfoReturnable<Float> cir) {
        // Increase reach distance in Subatomic dimension. Note that this may not work for targeting entities.
        if (client.world.getRegistryKey().equals(ModDimensions.SUBATOM_KEY))
            cir.setReturnValue(ModDimensions.SUBATOMIC_REACH_DISTANCE);
    }

}
