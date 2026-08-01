package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goetied.client.render.model.IceologerModel;
import za.co.infernos.goetied.common.entities.ally.illager.IceologerServant;
import za.co.infernos.goetied.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class IceologerServantRenderer<T extends IceologerServant> extends MobRenderer<T, IceologerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goetied.location("textures/entity/servants/illager/iceologer.png");
    protected static final ResourceLocation ORIGINAL = Goetied.location("textures/entity/servants/illager/iceologer_original.png");

    public IceologerServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new IceologerModel<>(renderManagerIn.bakeLayer(ModModelLayer.ICEOLOGER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isHostile() || !za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MobsConfig.IceologerServantTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE;
        }
    }
}