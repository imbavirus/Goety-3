package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.model.PikerModel;
import za.co.infernos.goetied.common.entities.ally.illager.PikerServant;
import za.co.infernos.goetied.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class PikerServantRenderer<T extends PikerServant> extends MobRenderer<T, PikerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goetied.location("textures/entity/servants/illager/piker.png");
    protected static final ResourceLocation ORIGINAL = Goetied.location("textures/entity/illagers/piker.png");

    public PikerServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new PikerModel<>(renderManagerIn.bakeLayer(ModModelLayer.PIKER)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, renderManagerIn.getModelSet(), renderManagerIn.getItemInHandRenderer()));
    }

    protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isHostile() || !za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MobsConfig.PikerServantTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE;
        }
    }
}