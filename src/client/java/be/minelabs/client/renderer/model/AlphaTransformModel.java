package be.minelabs.client.renderer.model;

import be.minelabs.block.blocks.QuantumfieldBlock;
import be.minelabs.state.property.Properties;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AlphaTransformModel implements UnbakedModel {

    private final Identifier baseModel;

    public AlphaTransformModel(Identifier baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(baseModel);
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        modelLoader.apply(baseModel).setParents(modelLoader);
    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        return new Baked(baker.bake(baseModel, rotationContainer));
    }

    public static class Baked implements BakedModel, FabricBakedModel {

        private final BakedModel baseModel;

        protected Baked(BakedModel baseModel) {
            this.baseModel = baseModel;
        }

        @Override
        public boolean isVanillaAdapter() {
            return false;
        }

        /**
         * 0.8 exp(-((x-0.5)/0.4)^2) from 0 to 1
         */
        private float computeAlpha(float ageRatio){
            return 0.8f * (float) Math.exp(- Math.pow(((ageRatio - 0.5) / 0.4) , 2));
        }

        @Override
        public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
            if (state.getBlock() instanceof QuantumfieldBlock){
                float alpha = 0.8f;
                if(state.get(Properties.CLOUD)) {
                    float ageRatio = (float) QuantumfieldBlock.getAge(state) / QuantumfieldBlock.MAX_AGE;
                    alpha = 0.2f * computeAlpha(ageRatio);
                }

                context.pushTransform(new AlphaTransform(alpha));
                context.bakedModelConsumer().accept(baseModel, state);
                context.popTransform();
                return;
            }
            context.bakedModelConsumer().accept(baseModel, state);
        }

        @Override
        public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
            context.bakedModelConsumer().accept(baseModel);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
            return baseModel.getQuads(state, face, random);
        }

        @Override
        public boolean useAmbientOcclusion() {
            return baseModel.useAmbientOcclusion();
        }

        @Override
        public boolean hasDepth() {
            return baseModel.hasDepth();
        }

        @Override
        public boolean isSideLit() {
            return baseModel.isSideLit();
        }

        @Override
        public boolean isBuiltin() {
            return false;
        }

        @Override
        public Sprite getParticleSprite() {
            return baseModel.getParticleSprite();
        }

        @Override
        public ModelTransformation getTransformation() {
            return baseModel.getTransformation();
        }

        @Override
        public ModelOverrideList getOverrides() {
            return baseModel.getOverrides();
        }

        private static class AlphaTransform implements RenderContext.QuadTransform {

            private final float alpha;
            private static final RenderMaterial MATERIAL = RendererAccess.INSTANCE.getRenderer().materialFinder().emissive(0, true).find();

            protected AlphaTransform(float alpha) {
                this.alpha = alpha;
            }

            @Override
            public boolean transform(MutableQuadView quad) {
                quad.material(MATERIAL);
                // Don't add alpha to quads with symbol
                if (quad.colorIndex() == 0)
                    return true;
                int c = ColorHelper.Argb.getArgb((int) (alpha * 255), 255, 255, 255);
                quad.spriteColor(0, c, c, c, c);
                return true;
            }
        }

    }
}
