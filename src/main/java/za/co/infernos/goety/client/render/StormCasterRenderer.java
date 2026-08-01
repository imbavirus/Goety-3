package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goety.client.render.model.StormCasterModel;
import za.co.infernos.goety.common.entities.hostile.illagers.StormCaster;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StormCasterRenderer<T extends StormCaster> extends MobRenderer<T, StormCasterModel<T>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/illagers/storm_caster.png");

    public StormCasterRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StormCasterModel<>(renderManagerIn.bakeLayer(ModModelLayer.STORM_CASTER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}