package be.minelabs.mixin;

import be.minelabs.world.dimension.ModDimensions;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    // Allows packets through with a further away break distance. This is needed for the subatomic dimension.
    @Shadow @Final
    public static double MAX_BREAK_SQUARED_DISTANCE = MathHelper.square(ModDimensions.SUBATOMIC_REACH_DISTANCE);
}
