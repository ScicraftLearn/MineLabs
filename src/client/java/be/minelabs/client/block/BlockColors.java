package be.minelabs.client.block;

import be.minelabs.block.Blocks;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class BlockColors {

    public static void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            if (tintIndex == 1) {
                return 0x3495eb;
            }
            return -1;
        }, Blocks.LAB_SINK);
    }
}
