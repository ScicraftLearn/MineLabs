package be.minelabs.client.renderer.entity;

import be.minelabs.Minelabs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntropyCreeperEntityRenderer extends CreeperEntityRenderer {

    public EntropyCreeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    /**
     * Get the texture for the Entity
     *
     * @param creeperEntity : Entity to get the texture for
     * @return Identifier
     */
    @Override
    public Identifier getTexture(CreeperEntity creeperEntity) {
        return new Identifier(Minelabs.MOD_ID, "textures/entity/entropy_creeper/entropy_creeper.png");
    }
}
