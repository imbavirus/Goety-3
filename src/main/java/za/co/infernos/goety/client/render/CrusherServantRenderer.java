package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goety.client.render.model.CrusherModel;
import za.co.infernos.goety.common.entities.ally.illager.CrusherServant;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrusherServantRenderer<T extends CrusherServant> extends MobRenderer<T, CrusherModel<T>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/illager/crusher.png");
    protected static final ResourceLocation STORM = Goety.location("textures/entity/servants/illager/crusher_storm.png");
    protected static final ResourceLocation ORIGINAL = Goety.location("textures/entity/illagers/crusher.png");
    protected static final ResourceLocation ORIGINAL_STORM = Goety.location("textures/entity/illagers/crusher_storm.png");

    public CrusherServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CrusherModel<>(renderManagerIn.bakeLayer(ModModelLayer.CRUSHER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.isStorm() ? 1.25F : 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.CrusherServantTexture, false)){
            if (entity.isStorm()){
                return ORIGINAL_STORM;
            }
            return ORIGINAL;
        } else {
            if (entity.isStorm()){
                return STORM;
            }
            return TEXTURE;
        }
    }
}