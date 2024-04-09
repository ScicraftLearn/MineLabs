package be.minelabs.client.renderer.entity;

import be.minelabs.Minelabs;
import be.minelabs.entity.BohrBlueprintEntity;
import be.minelabs.item.Items;
import be.minelabs.util.AtomConfiguration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

@Environment(EnvType.CLIENT)
public class BohrBlueprintEntityRenderer extends EntityRenderer<BohrBlueprintEntity> {
    private static final ItemStack PROTON = new ItemStack(Items.PROTON, 1);
    private static final ItemStack NEUTRON = new ItemStack(Items.NEUTRON, 1);
    private static final ItemStack ELECTRON = new ItemStack(Items.ELECTRON, 1);

    // Multiplier for distance to reduce radius at which entity is rendered. In default settings 64 allows rendering it from 16 blocks.
    // Higher multiplier means smaller render radius.
    private static final float RENDER_DISTANCE_MULTIPLIER = 64;

    // radius usable for atom rendering
    private static final float MAX_RENDER_RADIUS = 0.75f + 11f / 16f - 0.1f;

    private static final float ELECTRON_SCALE = 0.1f;
    private static final float ELECTRON_FIRST_SHELL_RADIUS = 0.5f;
    private static final int ELECTRON_SHELLS_AMOUNT = 7;
    // distance between two shells
    private static final float ELECTRON_SHELL_RADIUS_OFFSET = (MAX_RENDER_RADIUS - ELECTRON_FIRST_SHELL_RADIUS) / (ELECTRON_SHELLS_AMOUNT - 1);
    private static final Quaternionf[] ELECTRON_SHELL_ROTATIONS = {
            RotationAxis.POSITIVE_X.rotationDegrees(0),
            RotationAxis.POSITIVE_Y.rotationDegrees(90),
            RotationAxis.POSITIVE_X.rotationDegrees(90),
            RotationAxis.POSITIVE_Y.rotationDegrees(45),
            RotationAxis.POSITIVE_X.rotationDegrees(45),
            RotationAxis.POSITIVE_Y.rotationDegrees(135),
            RotationAxis.POSITIVE_X.rotationDegrees(135)
    };
    // rotation period in ticks
    private static final float ELECTRON_ROTATION_PERIOD = 4 * 20;
    // amount of points to use for the electron shell orbit line
    private static final int ELECTRON_LINE_NUMPOINTS = 32;
    // from 0 to 255
    private static final int ELECTRON_SHELL_LINE_GRAYSCALE_COLOR = 62;
    private static final int ELECTRON_SHELL_LINE_MAX_ALPHA = 150;
    // until this range the lines are fully visible (no fading)
    private static final double ELECTRON_SHELL_LINE_MIN_RENDER_RANGE = 4f;
    // line fades out until alpha zero at this distance
    private static final double ELECTRON_SHELL_LINE_MAX_RENDER_RANGE = 8f;

    // instability animation
    private static final double ELECTRON_INSTABILITY_MIN_PERIOD = 0.8d * 20;
    private static final double ELECTRON_INSTABILITY_MAX_PERIOD = 0.3d * 20;
    private static final float ELECTRON_INSTABILITY_MAX_OFFSET = ELECTRON_SHELL_RADIUS_OFFSET * 1.2f;

    // size of individual particle items
    private static final float NUCLEUS_SCALE = 0.15f;

    // for scaling nucleus radius depending on amount of particles
    private static final float NUCLEUS_MIN_RADIUS = 0.02f;
    private static final float NUCLEUS_MAX_RADIUS = 0.4f;

    private static final int NUCLEUS_MAX_ITEMS_RENDERED = 100;
    // radius of nucleus gets bigger until this capacity.
    private static final int NUCLEUS_MAX_CONTENT_FOR_RADIUS = BohrBlueprintEntity.MAX_PROTONS + BohrBlueprintEntity.MAX_NEUTRONS;

    private static final float NUCLEUS_INSTABILITY_MAX_SCALE = 2f;
    private static final float NUCLEUS_INSTABILITY_PULSE_PERIOD = 1f * 20;

    private static final float NUCLEUS_INSTABILITY_MIN_PULSE_PERCENT = 1f / 1.5f;
    private static final float NUCLEUS_INSTABILITY_MAX_PULSE_PERCENT = 1f / 4f;

    private static final int NUCLEUS_INSTABILITY_GROUPS = 50;

    /**
     * Computes uniformly spread out points on a unit sphere using Fibonacci grid
     */
    private static List<Vector3f> uniformSphericalCoordinates(int nrOfPoints) {
        if (nrOfPoints == 1)
            return List.of(new Vector3f(0, 0, 0));

        List<Vector3f> points = new ArrayList<>(nrOfPoints);
        double goldenRatio = Math.PI * (Math.sqrt(5f) - 1f);
        for (int i = 0; i < nrOfPoints; i++) {
            float y = 1 - 2 * i / ((float) (nrOfPoints - 1));
            double r = (float) Math.sqrt(1 - y * y);
            float x = (float) (Math.sin(goldenRatio * i) * r);
            float z = (float) (Math.cos(goldenRatio * i) * r);

            points.add(new Vector3f(x, y, z));
        }

        return points;
    }

    private int[] getElectronShellConfiguration(int nE) {
        if (nE >= ELECTRON_SHELL_CAPACITIES.length)
            nE = ELECTRON_SHELL_CAPACITIES.length - 1;
        return ELECTRON_SHELL_CAPACITIES[nE];
    }


    private final ItemRenderer itemRenderer;

    public BohrBlueprintEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public Identifier getTexture(BohrBlueprintEntity entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    public Vec3d getPositionOffset(BohrBlueprintEntity entity, float tickDelta) {
        return new Vec3d(0, entity.getHeight() / 2, 0);
    }

    @Override
    protected int getBlockLight(BohrBlueprintEntity entity, BlockPos pos) {
        return 15;      // render as if always fully lit
    }

    @Override
    protected int getSkyLight(BohrBlueprintEntity entity, BlockPos pos) {
        return 15;      // render as if always fully lit
    }

    @Override
    public boolean shouldRender(BohrBlueprintEntity entity, Frustum frustum, double x, double y, double z) {
        // act like it is further away to reduce rendering range and still take renderDistanceMultiplier setting into account.
        return super.shouldRender(entity, frustum, x, y, z) && entity.shouldRender(entity.getPos().squaredDistanceTo(x, y, z) * RENDER_DISTANCE_MULTIPLIER);
    }

    @Override
    public void render(BohrBlueprintEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Should someone use data set to go above the limit of the entity, this will ensure the rendering does not crash.
        AtomConfiguration atomConfig = entity.getAtomConfig();
        int nP = Math.min(atomConfig.getProtons(), BohrBlueprintEntity.MAX_PROTONS);
        int nN = Math.min(atomConfig.getNeutrons(), BohrBlueprintEntity.MAX_NEUTRONS);
        int nE = Math.min(atomConfig.getElectrons(), BohrBlueprintEntity.MAX_ELECTRONS);

        float electronInstability = atomConfig.getElectronInstability();
        float nucleusInstability = atomConfig.getNucleusInstability();

        float time = entity.age + tickDelta;
        double dToCamera = Math.sqrt(dispatcher.getSquaredDistanceToCamera(entity));

        MinecraftClient.getInstance().getProfiler().push("bohr");

//        renderResultingAtom(tickDelta, matrices, entity, light, vertexConsumers);

        matrices.push();

        MinecraftClient.getInstance().getProfiler().push("nucleus");
        renderNucleus(nP, nN, nucleusInstability, time, matrices, vertexConsumers, light);
        MinecraftClient.getInstance().getProfiler().swap("electrons");
        renderElectrons(nE, electronInstability, dToCamera, time, matrices, vertexConsumers, light);
        MinecraftClient.getInstance().getProfiler().pop();

        matrices.pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private void renderElectrons(int nE, float instability, double dToCamera, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Electrons are rendered in at most 7 different shells with chosen yaw, pitch and roll.
        // Within an orbit the electrons are equally spaced from each other.

        int[] electronShellConfiguration = getElectronShellConfiguration(nE);

        float radius = ELECTRON_FIRST_SHELL_RADIUS;

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));

        for (int i = 0; i < electronShellConfiguration.length; i++) {
            int electronsInShell = electronShellConfiguration[i];
            Quaternionf rotation = ELECTRON_SHELL_ROTATIONS[i];

            // don't update normals for rendering electron items (lighting effect)
            matrices.push();
            matrices.multiplyPositionMatrix(rotation.get(new Matrix4f()));
            renderElectronShellElectrons(electronsInShell, radius, instability, time, matrices, vertexConsumers, light);
            matrices.pop();

            // do updated normals for rendering line
            matrices.push();
            matrices.multiply(rotation);
            renderElectronShellLine(radius, dToCamera, matrices, vertexConsumers);
            matrices.pop();

            radius += ELECTRON_SHELL_RADIUS_OFFSET;
        }
        matrices.pop();
    }

    /**
     * Render an electron shell in the XY-plane with specified radius and number of electrons.
     */
    private void renderElectronShellElectrons(int nE, float radius, float instability, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // rotation animation of electrons on shell
        float angle = (time % ELECTRON_ROTATION_PERIOD) / ELECTRON_ROTATION_PERIOD * 360;

        matrices.push();
        // don't update normals. This allows the lighting to be computed as if the outside is always lit
        matrices.multiplyPositionMatrix(RotationAxis.POSITIVE_Z.rotationDegrees(angle).get(new Matrix4f()));

        // render electron at top of orbit then rotate and repeat
        float angleBetweenElectrons = 360f / nE;
        for (int e = 0; e < nE; e++) {
            Vector3f pos = new Vector3f(0, radius, 0);
            float percentOfOrbit = (float) e / nE;
            renderElectron(matrices, pos, instability, percentOfOrbit, time, vertexConsumers, light);

            // don't update normals
            matrices.multiplyPositionMatrix(RotationAxis.POSITIVE_Z.rotationDegrees(angleBetweenElectrons).get(new Matrix4f()));

        }
        matrices.pop();
    }

    private void renderElectronShellLine(float radius, double dToCamera, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        if (dToCamera > ELECTRON_SHELL_LINE_MAX_RENDER_RANGE)
            return;

        double delta = (dToCamera - ELECTRON_SHELL_LINE_MIN_RENDER_RANGE) / (ELECTRON_SHELL_LINE_MAX_RENDER_RANGE - ELECTRON_SHELL_LINE_MIN_RENDER_RANGE);
        int alpha = (int) Math.floor(MathHelper.clampedLerp(ELECTRON_SHELL_LINE_MAX_ALPHA, 0, delta));

        float angleBetweenLinePoints = 360f / ELECTRON_LINE_NUMPOINTS;

        // compute normal of line which should point towards next point
        // other angle of triangle formed by origin and line
        double beta = Math.toRadians(180 - angleBetweenLinePoints) / 2f;
        float normalX = (float) -Math.sin(beta);
        float normalY = (float) -Math.cos(beta);

        VertexConsumer lineBuffer = vertexConsumers.getBuffer(RenderLayer.getLines());
        matrices.push();
        MatrixStack.Entry matrixEntry = matrices.peek();
        for (int e = 0; e < ELECTRON_LINE_NUMPOINTS; e++) {
            lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, radius, 0)
                    .color(ELECTRON_SHELL_LINE_GRAYSCALE_COLOR, ELECTRON_SHELL_LINE_GRAYSCALE_COLOR, ELECTRON_SHELL_LINE_GRAYSCALE_COLOR, alpha)
                    .normal(matrixEntry.getNormalMatrix(), normalX, normalY, 0)
                    .next();

            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angleBetweenLinePoints));

            lineBuffer.vertex(matrixEntry.getPositionMatrix(), 0, radius, 0)
                    .color(ELECTRON_SHELL_LINE_GRAYSCALE_COLOR, ELECTRON_SHELL_LINE_GRAYSCALE_COLOR, ELECTRON_SHELL_LINE_GRAYSCALE_COLOR, alpha)
                    .normal(matrixEntry.getNormalMatrix(), -normalX, normalY, 0)
                    .next();
        }

        matrices.pop();
    }

    private void renderNucleus(int nP, int nN, float instability, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // nothing to render (prevent division by zero)
        if (nP + nN == 0)
            return;

        // place nucleus particles in predetermined coordinates
        int nToRender = Math.min(NUCLEUS_MAX_ITEMS_RENDERED, nP + nN);
        List<Vector3f> positions = uniformSphericalCoordinates(nToRender);
        Collections.shuffle(positions, new Random(42));

        // we keep ratio of protons rendered as close to requested as possible
        float pRatio = (float) nP / (float) (nP + nN);
        int nPToRender = (int) Math.floor(pRatio * nToRender);

        float volumePercent = (float) (nP + nN) / NUCLEUS_MAX_CONTENT_FOR_RADIUS;
        float radiusPercent = (float) Math.pow(3 * volumePercent / (4 * Math.PI), 1f / 3);    // based on volume of sphere
        float nucleusRadius = MathHelper.clampedLerp(NUCLEUS_MIN_RADIUS, NUCLEUS_MAX_RADIUS, radiusPercent);

        matrices.push();
        for (int i = 0; i < nToRender;i++){
            Vector3f pos = positions.get(i).mul(nucleusRadius);
            ItemStack type = i < nPToRender ? PROTON : NEUTRON;
            renderNucleusParticle(type, pos, instability, i, nToRender, time, matrices, vertexConsumers, light);
        }
        matrices.pop();
    }

    private Vector3f getElectronInstabilityOffset(float instability, float percentOfOrbit, float time) {
        // if you don't want all electrons to be in sync
//        time = time + percentOfOrbit * ELECTRON_ROTATION_PERIOD;

        double period = MathHelper.lerp(instability, ELECTRON_INSTABILITY_MIN_PERIOD, ELECTRON_INSTABILITY_MAX_PERIOD);
        float offset = instability * ELECTRON_INSTABILITY_MAX_OFFSET;
        float yOffset = (float) (Math.sin(Math.toRadians(time / period * 360)) + 1) / 2 * offset;
        return new Vector3f(0, yOffset, 0);
    }

    private void renderElectron(MatrixStack matrices, Vector3f pos, float instability, float percentOfOrbit, float time, VertexConsumerProvider vertexConsumers, int light) {
        if (instability > 0)
            pos.add(getElectronInstabilityOffset(instability, percentOfOrbit, time));

        matrices.push();
        matrices.translate(pos.x, pos.y, pos.z);
        // because we use rotation and the offset is always in the y direction, this faces them outwards
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        matrices.scale(ELECTRON_SCALE, ELECTRON_SCALE, ELECTRON_SCALE);
        renderItem(ELECTRON, matrices, vertexConsumers, light);
        matrices.pop();
    }

    /**
     * Evaluate pulse function with long tail in progress [0, 1].
     * pulsePercent is percent of function that is pulse. Rest is tail.
     */
    private float getPulseWithTail(float progress, float pulsePercent) {
        if (progress <= pulsePercent) {
            // local progress to simulate pulse
            progress = progress / pulsePercent;
            return (1 + (float) Math.cos(progress * 2 * Math.PI + Math.PI)) / 2;
        } else {
            return 0f;
        }
    }

    private float getNuclideInstabilityScale(float instability, int index, int total, float time) {
        float pulsePercent = MathHelper.lerp(instability, NUCLEUS_INSTABILITY_MAX_PULSE_PERCENT, NUCLEUS_INSTABILITY_MIN_PULSE_PERCENT);

        // This keeps width of pulse the same (independent of instability)
        float period = NUCLEUS_INSTABILITY_PULSE_PERIOD / pulsePercent;
        float progress = (time % period) / period;

        // group specific progress
        int groups = Math.min(total, NUCLEUS_INSTABILITY_GROUPS);
        float groupOffset = (float) (index % groups) / groups;
        progress = (progress + groupOffset) % 1;

        float minScale = 1;
        float maxScale = instability * NUCLEUS_INSTABILITY_MAX_SCALE;
        float value = getPulseWithTail(progress, pulsePercent);
        float scale = MathHelper.lerp(value, minScale, maxScale);

        return scale;
    }

    private void renderNucleusParticle(ItemStack type, Vector3f pos, float instability, int index, int total, float time, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (instability > 0)
            pos.mul(getNuclideInstabilityScale(instability, index, total, time));

        matrices.push();
        matrices.translate(pos.x, pos.y, pos.z);
        matrices.scale(NUCLEUS_SCALE, NUCLEUS_SCALE, NUCLEUS_SCALE);

        // have normals face sky
        matrices.peek().getNormalMatrix().mul(RotationAxis.POSITIVE_X.rotationDegrees(90).get(new Matrix3f()));

        // have everything facing the camera orientation
        matrices.multiplyPositionMatrix(dispatcher.getRotation().get(new Matrix4f()));

        renderItem(type, matrices, vertexConsumers, light);
        matrices.pop();
    }

    private void renderItem(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        MinecraftClient.getInstance().getProfiler().push("item renderer");
        itemRenderer.renderItem(stack, ModelTransformationMode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, null, 0);
        MinecraftClient.getInstance().getProfiler().pop();
    }

    private void renderResultingAtom(float tickDelta, MatrixStack matrixStack, BohrBlueprintEntity entity, int light, VertexConsumerProvider vertexConsumers) {
        ItemStack atomStack = entity.getCraftableAtom();
        if (atomStack.isEmpty()) return;

        matrixStack.push();
        matrixStack.translate(0, 5f / 16 - 1 - getPositionOffset(entity, tickDelta).getY(), 0.25);
        matrixStack.scale(2, 0.5f, 2);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));

        itemRenderer.renderItem(atomStack, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers, null, 0);
        matrixStack.pop();
    }


    // Maps the number of electrons to the correct shell configuration by index.
    // Placed at the end of the file for better readability.
    private static final int[][] ELECTRON_SHELL_CAPACITIES = {
            {},
            {1},
            {2},
            {2, 1},
            {2, 2},
            {2, 3},
            {2, 4},
            {2, 5},
            {2, 6},
            {2, 7},
            {2, 8},
            {2, 8, 1},
            {2, 8, 2},
            {2, 8, 3},
            {2, 8, 4},
            {2, 8, 5},
            {2, 8, 6},
            {2, 8, 7},
            {2, 8, 8},
            {2, 8, 8, 1},
            {2, 8, 8, 2},
            {2, 8, 9, 2},
            {2, 8, 10, 2},
            {2, 8, 11, 2},
            {2, 8, 13, 1},
            {2, 8, 13, 2},
            {2, 8, 14, 2},
            {2, 8, 15, 2},
            {2, 8, 16, 2},
            {2, 8, 18, 1},
            {2, 8, 18, 2},
            {2, 8, 18, 3},
            {2, 8, 18, 4},
            {2, 8, 18, 5},
            {2, 8, 18, 6},
            {2, 8, 18, 7},
            {2, 8, 18, 8},
            {2, 8, 18, 8, 1},
            {2, 8, 18, 8, 2},
            {2, 8, 18, 9, 2},
            {2, 8, 18, 10, 2},
            {2, 8, 18, 12, 1},
            {2, 8, 18, 13, 1},
            {2, 8, 18, 13, 2},
            {2, 8, 18, 15, 1},
            {2, 8, 18, 16, 1},
            {2, 8, 18, 18},
            {2, 8, 18, 18, 1},
            {2, 8, 18, 18, 2},
            {2, 8, 18, 18, 3},
            {2, 8, 18, 18, 4},
            {2, 8, 18, 18, 5},
            {2, 8, 18, 18, 6},
            {2, 8, 18, 18, 7},
            {2, 8, 18, 18, 8},
            {2, 8, 18, 18, 8, 1},
            {2, 8, 18, 18, 8, 2},
            {2, 8, 18, 18, 9, 2},
            {2, 8, 18, 19, 9, 2},
            {2, 8, 18, 21, 8, 2},
            {2, 8, 18, 22, 8, 2},
            {2, 8, 18, 23, 8, 2},
            {2, 8, 18, 24, 8, 2},
            {2, 8, 18, 25, 8, 2},
            {2, 8, 18, 25, 9, 2},
            {2, 8, 18, 27, 8, 2},
            {2, 8, 18, 28, 8, 2},
            {2, 8, 18, 29, 8, 2},
            {2, 8, 18, 30, 8, 2},
            {2, 8, 18, 31, 8, 2},
            {2, 8, 18, 32, 8, 2},
            {2, 8, 18, 32, 9, 2},
            {2, 8, 18, 32, 10, 2},
            {2, 8, 18, 32, 11, 2},
            {2, 8, 18, 32, 12, 2},
            {2, 8, 18, 32, 13, 2},
            {2, 8, 18, 32, 14, 2},
            {2, 8, 18, 32, 15, 2},
            {2, 8, 18, 32, 17, 1},
            {2, 8, 18, 32, 18, 1},
            {2, 8, 18, 32, 18, 2},
            {2, 8, 18, 32, 18, 3},
            {2, 8, 18, 32, 18, 4},
            {2, 8, 18, 32, 18, 5},
            {2, 8, 18, 32, 18, 6},
            {2, 8, 18, 32, 18, 7},
            {2, 8, 18, 32, 18, 8},
            {2, 8, 18, 32, 18, 8, 1},
            {2, 8, 18, 32, 18, 8, 2},
            {2, 8, 18, 32, 18, 9, 2},
            {2, 8, 18, 32, 18, 10, 2},
            {2, 8, 18, 32, 20, 9, 2},
            {2, 8, 18, 32, 21, 9, 2},
            {2, 8, 18, 32, 22, 9, 2},
            {2, 8, 18, 32, 24, 8, 2},
            {2, 8, 18, 32, 25, 8, 2},
            {2, 8, 18, 32, 25, 9, 2},
            {2, 8, 18, 32, 27, 8, 2},
            {2, 8, 18, 32, 28, 8, 2},
            {2, 8, 18, 32, 29, 8, 2},
            {2, 8, 18, 32, 30, 8, 2},
            {2, 8, 18, 32, 31, 8, 2},
            {2, 8, 18, 32, 32, 8, 2},
            {2, 8, 18, 32, 32, 8, 3},
            {2, 8, 18, 32, 32, 10, 2},
            {2, 8, 18, 32, 32, 11, 2},
            {2, 8, 18, 32, 32, 12, 2},
            {2, 8, 18, 32, 32, 13, 2},
            {2, 8, 18, 32, 32, 14, 2},
            {2, 8, 18, 32, 32, 15, 2},
            {2, 8, 18, 32, 32, 16, 2},
            {2, 8, 18, 32, 32, 17, 2},
            {2, 8, 18, 32, 32, 18, 2},
            {2, 8, 18, 32, 32, 18, 3},
            {2, 8, 18, 32, 32, 18, 4},
            {2, 8, 18, 32, 32, 18, 5},
            {2, 8, 18, 32, 32, 18, 6},
            {2, 8, 18, 32, 32, 18, 7},
            {2, 8, 18, 32, 32, 18, 8},  // Og
            {2, 8, 18, 32, 32, 18, 9},  // made up to have some padding, likely not correct
            {2, 8, 18, 32, 32, 18, 10},
            {2, 8, 18, 32, 32, 18, 11},
            {2, 8, 18, 32, 32, 18, 12},
            {2, 8, 18, 32, 32, 18, 13},
            {2, 8, 18, 32, 32, 18, 14},
            {2, 8, 18, 32, 32, 18, 15},
            {2, 8, 18, 32, 32, 18, 16}, // 126
    };

}