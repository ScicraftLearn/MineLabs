package be.uantwerpen.scicraft.renderer;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.entity.AnimatedChargedBlockEntity;
import be.uantwerpen.scicraft.block.entity.BohrBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


@Environment(EnvType.CLIENT)
public class BohrBlockEntityRenderer<T extends BohrBlockEntity> implements BlockEntityRenderer<T> {

	private static ItemStack nucleus_stack = new ItemStack(Items.FIREWORK_STAR, 1); // minecraft Items firework star
	private static ItemStack proton_stack = new ItemStack(Blocks.PROTON, 1);
	private static ItemStack neutron_stack = new ItemStack(Blocks.NEUTRON, 1);
//	private static ItemStack electron_shell_stack = new ItemStack(Blocks.PROTON, 1);
	private static ItemStack electron_stack = new ItemStack(Blocks.ELECTRON, 1);

	private static List<Vec3f> icosahedron = new ArrayList<>();
	static {
		for (int i = 1; i < 13; i++) {
			Vec3f punt1 = new Vec3f();
			if (i == 1) {
				punt1 = new Vec3f(0, 0, (float)Math.sqrt(5)/2);
			}
			else if (i > 1 && i < 7) {
				punt1 = new Vec3f((float)Math.cos((i-2)*(2*Math.PI)/5), (float)Math.sin((i-2)*(2*Math.PI)/5), 0.5f);
			}
			else if (i > 6 && i < 12) {
				double a = (Math.PI / 5) + (i - 7) * (2 * Math.PI) / 5;
				punt1 = new Vec3f((float)Math.cos(a), (float)Math.sin(a), -0.5f);
			}
			else if (i == 12) {
				punt1 = new Vec3f(0, 0, (float)-Math.sqrt(5)/2);
			}
			icosahedron.add(punt1);
		}
	}


    private Context context;

	public BohrBlockEntityRenderer(Context ctx) {
    	this.context = ctx;
    }

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		World world = blockEntity.getWorld();
		matrices.push();

		PlayerEntity player = MinecraftClient.getInstance().player;
		int roll = player.getRoll();

		int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());

		double offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;

		// Move the item
//		matrices.translate(0.5, 1.25 + offset, 0.5);
		// scale
		matrices.translate(0.5f, 1.75f, 0.5f);
		matrices.scale(1.5f,1.5f,1.5f);
		// Rotate the item
//		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 4));
		MinecraftClient.getInstance().getItemRenderer().renderItem(nucleus_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);

//		matrices.scale(5f,5f,5f);
//		matrices.translate(-0.5f, -1.25f, -0.5f);

		for (int i = 0; i < 12; i++) {
			float offset_x = icosahedron.get(i).getX()/1.5f;
			float offset_y = icosahedron.get(i).getY()/1.5f;
			float offset_z = icosahedron.get(i).getZ()/1.5f;

			matrices.translate(offset_x, offset_y, offset_z);
			MinecraftClient.getInstance().getItemRenderer().renderItem(proton_stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);
			matrices.translate(-offset_x, -offset_y, -offset_z);

		}






//		matrices.translate(0.5f, 2f, 0.5f);
//		matrices.scale(0.2f,0.2f,0.2f);
//		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumerProvider, 0);






		matrices.pop();
//		double offset = 0;
//		if (blockEntity.time != 0) {
//			double time_fraction = Math.max(0, Math.min(1, (blockEntity.getWorld().getTime() + tickDelta - blockEntity.time) / AnimatedChargedBlockEntity.time_move_ticks));
//			if (blockEntity.annihilation) {
//				offset = .5 * time_fraction * time_fraction;
//			} else {
//				offset = time_fraction < 0.5 ? 2 * time_fraction * time_fraction : 2 * time_fraction * (-time_fraction + 2) - 1;
//			}
//		}
//		if (!(blockEntity.annihilation && offset ==.5)) {
//			matrices.translate(blockEntity.movement_direction.getX() * offset, blockEntity.movement_direction.getY() * offset, blockEntity.movement_direction.getZ() * offset);
//			BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
//			blockRenderManager.getModelRenderer().render(
//					world,
//					blockRenderManager.getModel(blockEntity.render_state),
//					blockEntity.render_state,
//					blockEntity.getPos(),
//					matrices,
//					vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockEntity.render_state)),
//					false, net.minecraft.util.math.random.Random.create(),
//					blockEntity.render_state.getRenderingSeed(blockEntity.getPos()),
//					OverlayTexture.DEFAULT_UV);
//		}

	}

	/**
	 * Handles the scaling and stuff for the nucleus (and protons and neutrons).
	 */
	public void makeNucleus() {

	}

	/**
	 * Handles the scaling and stuff for the electron shells.
	 */
	public void makeElectronshells() {

	}

	/**
	 * Handles the scaling and stuff for the electrons.
	 */
	public void makeElectrons() {

	}

}


