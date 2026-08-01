package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goetied.client.render.model.StormCasterModel;
import za.co.infernos.goetied.common.entities.ally.illager.StormCasterServant;
import za.co.infernos.goetied.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StormCasterServantRenderer<T extends StormCasterServant> extends MobRenderer<T, StormCasterModel<T>> {
    protected static final ResourceLocation TEXTURE = Goetied.location("textures/entity/servants/illager/storm_caster.png");
    protected static final ResourceLocation ORIGINAL = Goetied.location("textures/entity/illagers/storm_caster.png");

    public StormCasterServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new StormCasterModel<>(renderManagerIn.bakeLayer(ModModelLayer.STORM_CASTER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isHostile() || !za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MobsConfig.StormCasterServantTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE;
        }
    }
}