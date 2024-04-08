package be.minelabs.client.block;

import be.minelabs.block.Blocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class BlockRenderLayers {

    public static void onInitializeClient(){
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ATOM_FLOOR, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ANTINEUTRINO, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LARGE_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MEDIUM_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SMALL_SALT_CRYSTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.SALT_WIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ELECTRIC_FIELD_SENSOR_BLOCK, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GLUON_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.DOWNQUARK_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.UPQUARK_QUANTUMFIELD, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ELECTRON_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRINO_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PHOTON_QUANTUMFIELD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WEAK_BOSON_QUANTUMFIELD, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MOLOGRAM_BLOCK, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GREEN_FIRE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ERLENMEYER_STAND, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BURNER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MICROSCOPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.TUBERACK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_CABIN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_DRAWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_SINK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_CORNER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LAB_CENTER, RenderLayer.getCutout());
    }

}
