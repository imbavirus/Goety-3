package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.entities.projectiles.BlossomBall;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BlossomBallRenderer extends AbstractFungusRenderer<BlossomBall> {
    private static final ResourceLocation TEXTURE = Goetied.location("textures/entity/projectiles/blossom_ball.png");

    public BlossomBallRenderer(EntityRendererProvider.Context p_174426_) {
        super(p_174426_);
    }

    protected void scale(BlossomBall p_116294_, PoseStack p_116295_, float p_116296_) {
        p_116295_.scale(1.5F, 1.5F, 1.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BlossomBall p_114482_) {
        return TEXTURE;
    }
}