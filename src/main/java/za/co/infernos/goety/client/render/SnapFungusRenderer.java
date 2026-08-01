package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.projectiles.SnapFungus;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SnapFungusRenderer extends AbstractFungusRenderer<SnapFungus> {
    private static final ResourceLocation TEXTURE = Goety.location("textures/entity/projectiles/snap_fungus.png");

    public SnapFungusRenderer(EntityRendererProvider.Context p_174426_) {
        super(p_174426_);
    }

    protected void scale(SnapFungus p_116294_, PoseStack p_116295_, float p_116296_) {
        p_116295_.scale(0.5F, 0.5F, 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SnapFungus p_114482_) {
        return TEXTURE;
    }
}