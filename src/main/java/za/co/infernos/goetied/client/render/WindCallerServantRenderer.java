package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goetied.client.render.model.WindCallerModel;
import za.co.infernos.goetied.common.entities.ally.illager.WindCallerServant;
import za.co.infernos.goetied.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WindCallerServantRenderer<T extends WindCallerServant> extends MobRenderer<T, WindCallerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goetied.location("textures/entity/servants/illager/wind_caller.png");
    protected static final ResourceLocation ORIGINAL = Goetied.location("textures/entity/servants/illager/wind_caller_original.png");

    public WindCallerServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new WindCallerModel<>(renderManagerIn.bakeLayer(ModModelLayer.WIND_CALLER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isHostile() || !za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MobsConfig.WindCallerServantTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE;
        }
    }
}