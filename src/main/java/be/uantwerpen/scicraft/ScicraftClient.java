package be.uantwerpen.scicraft;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.entity.AnimatedChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.BlockEntities;
import be.uantwerpen.scicraft.entity.Entities;
import be.uantwerpen.scicraft.item.ItemGroups;
import be.uantwerpen.scicraft.network.NetworkingConstants;
import be.uantwerpen.scicraft.renderer.ChargedBlockEntityRenderer;
import be.uantwerpen.scicraft.renderer.ChargedPlaceholderBlockEntityRenderer;
import be.uantwerpen.scicraft.renderer.EntropyCreeperEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;


@SuppressWarnings("UNUSED")
public class ScicraftClient implements ClientModInitializer {
    @Override()
    public void onInitializeClient() {

        registerEvents();

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_NUL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_MINUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PION_PLUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WEAK_BOSON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ANTINEUTRINO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.POSITRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ELECTRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NEUTRON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.PROTON, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.GREEN_FIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.HELIUM, RenderLayer.getTranslucent());

        // Register rendering for electron entity
        EntityRendererRegistry.register(Entities.ELECTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.PROTON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.NEUTRON_ENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.ENTROPY_CREEPER, EntropyCreeperEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.ANIMATED_CHARGED_BLOCK_ENTITY, ChargedBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntities.CHARGED_PLACEHOLDER_BLOCK_ENTITY, ChargedPlaceholderBlockEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.CHARGED_MOVE_STATE, (client, handler, buf, responseSender) -> {
            BlockPos target = buf.readBlockPos();
            String block_name = buf.readString();
            boolean annihilation = buf.readBoolean();
            client.execute(() -> {
                if (client.world != null) {
                    if (client.world.getBlockEntity(target) instanceof AnimatedChargedBlockEntity particle2) {
                        particle2.render_state = particle2.string2BlockState(block_name);
                        particle2.annihilation = annihilation;
                    }
                }
            });
        });
    }

    private void registerEvents() {
        UseItemCallback.EVENT.register((player, item, hand) ->
                {
                    //Checks to prevent null errors
                    ItemStack stack=player.getMainHandStack();
                    if(stack!=ItemStack.EMPTY || stack!=null){
                        if(stack.getItem().getGroup()!=ItemGroups.ATOMS){
                            System.out.println("Geen blokken toegelaten");
                            return TypedActionResult.fail(stack);
                        }
                        else{
                            System.out.println("Atomen mogen!");
                            return TypedActionResult.consume(stack);
                        }
                    }
                    else {
                        return TypedActionResult.pass(ItemStack.EMPTY);
                    }
                }
        );

    }
}
