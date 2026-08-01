package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goety.client.render.model.StormCasterModel;
import za.co.infernos.goety.common.entities.ally.undead.bound.BoundStormCaster;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoundStormCasterRenderer<T extends BoundStormCaster> extends MobRenderer<T, StormCasterModel<T>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/bound_illager/bound_storm_caster.png");
    protected static final ResourceLocation CASTING = Goety.location("textures/entity/servants/bound_illager/bound_storm_caster_casting.png");

    public BoundStormCasterRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StormCasterModel<>(renderManagerIn.bakeLayer(ModModelLayer.STORM_CASTER)), 0.5F);
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