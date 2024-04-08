package be.minelabs.client.renderer.model;

import be.minelabs.Minelabs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.*;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModelProvider implements ModelResourceProvider {


    protected static final String ALPHA_TRANSFORM_SUFFIX = "/alpha_transform";

    private final ResourceManager resourceManager;

    public ModelProvider(ResourceManager rm) {
        this.resourceManager = rm;
    }

    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
        if (identifier.getPath().startsWith("molecules")) {
            try {
                Reader reader = this.resourceManager.openAsReader(new Identifier(identifier.getNamespace(), "models/" + identifier.getPath() + ".json"));
                return MoleculeModel.deserialize(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (identifier.equals(new Identifier(Minelabs.MOD_ID, "block/mologram_beam"))) {
            return new MologramBeamModel();
        }
        if (identifier.getPath().contains("block/quantumfield") && identifier.getPath().endsWith(ALPHA_TRANSFORM_SUFFIX)) {
            // Apply alpha transformation based on age for quantum fields
            Identifier baseModel = new Identifier(identifier.getNamespace(), identifier.getPath().replace(ALPHA_TRANSFORM_SUFFIX, ""));
            return new AlphaTransformModel(baseModel);
        }

        return null;
    }

    public static class Requester implements ExtraModelProvider {
        /**
         * Request extra models to be loaded here.
         */
        @Override
        public void provideExtraModels(ResourceManager manager, Consumer<Identifier> out) {
            Map<Identifier, Resource> molecules = manager.findResources("models/molecules", (i) -> true);
            for (Identifier resource : molecules.keySet()) {
                out.accept(new Identifier(resource.getNamespace(), resource.getPath().split("models/")[1].split(".json")[0]));
            }
            out.accept(new Identifier(Minelabs.MOD_ID, "block/mologram_beam"));
        }
    }

}