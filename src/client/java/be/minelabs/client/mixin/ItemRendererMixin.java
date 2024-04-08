package be.minelabs.client.mixin;

import be.minelabs.Minelabs;
import be.minelabs.client.hooks.ItemRendererHook;
import be.minelabs.item.Items;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useBalloonModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode,
                                      boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                      int light, int overlay) {

        // Do not render the balloon in 3D if it is in the GUI, on the ground, or in an item frame/armor stand
        boolean bl = renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED;
        if (stack.isOf(Items.BALLOON) && !bl) {
            return ((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(Minelabs.MOD_ID, "balloon_3d", "inventory"));
        }
        return value;
    }

    @Unique
    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "HEAD"), cancellable = true)
    public void injectRenderGuiItemOverlay(MatrixStack matrices, TextRenderer textRenderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        if (ItemRendererHook.renderGuiItemOverlay((ItemRenderer) (Object) this, matrices, stack, x, y)) {
            ci.cancel();
        }
    }


}
