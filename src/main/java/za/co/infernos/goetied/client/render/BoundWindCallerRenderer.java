package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goetied.client.render.model.WindCallerModel;
import za.co.infernos.goetied.common.entities.ally.undead.bound.BoundWindCaller;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoundWindCallerRenderer<T extends BoundWindCaller> extends MobRenderer<T, WindCallerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goetied.location("textures/entity/servants/bound_illager/bound_wind_caller.png");
    protected static final ResourceLocation CASTING = Goetied.location("textures/entity/servants/bound_illager/bound_wind_caller_casting.png");

    public BoundWindCallerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new WindCallerModel<>(renderManagerIn.bakeLayer(ModModelLayer.WIND_CALLER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
    }

    protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getTextureLocation(T p_114482_) {
        if (p_114482_.isAttacking()){
            return CASTING;
        }
        return TEXTURE;
    }
}