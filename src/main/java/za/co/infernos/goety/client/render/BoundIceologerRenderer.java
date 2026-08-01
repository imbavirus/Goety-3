package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goety.client.render.model.IceologerModel;
import za.co.infernos.goety.common.entities.ally.undead.bound.BoundIceologer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoundIceologerRenderer<T extends BoundIceologer> extends MobRenderer<T, IceologerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/bound_illager/bound_iceologer.png");
    protected static final ResourceLocation CASTING = Goety.location("textures/entity/servants/bound_illager/bound_iceologer_casting.png");

    public BoundIceologerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new IceologerModel<>(renderManagerIn.bakeLayer(ModModelLayer.ICEOLOGER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
    }

    protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getTextureLocation(T p_114482_) {
        if (p_114482_.getCurrentAnimation() == p_114482_.getAnimationState("attack")){
            return CASTING;
        }
        return TEXTURE;
    }
}