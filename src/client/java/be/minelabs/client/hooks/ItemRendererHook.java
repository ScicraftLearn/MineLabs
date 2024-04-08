package be.minelabs.client.hooks;

import be.minelabs.item.items.AtomItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

/**
 * See Applied Energistics 2 <p>
 *
 * <a href="https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/forge/1.19.4/src/main/java/appeng/hooks/ItemRendererHooks.java">github page</a>
 */
public final class ItemRendererHook {

    private ItemRendererHook() {
    }

    public static boolean renderGuiItemOverlay(ItemRenderer renderer, MatrixStack matrices, ItemStack stack, int x, int y) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (stack.getItem() instanceof AtomItem atomItem) {
            int count = stack.getCount();
            if (count > 99) {
                // custom rendering
                // TODO HELP
                /*
                var unwrapped = GenericStack.unwrapItemStack(stack);
                if (unwrapped != null) {
                    AEKeyRendering.drawInGui(minecraft, poseStack, x, y, unwrapped.what());

                    if (unwrapped.amount() > 0) {
                        String amtText = unwrapped.what().formatAmount(unwrapped.amount(), AmountFormat.SLOT);
                        Font font = minecraft.font;
                        StackSizeRenderer.renderSizeLabel(poseStack, font, x, y, amtText, false);
                    }

                    return true;
                }
                 */


            }
            return false;
        }


        return false;
    }
}
