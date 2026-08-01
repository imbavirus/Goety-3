package za.co.infernos.goetied.client.render;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goetied.client.render.model.CryologerModel;
import za.co.infernos.goetied.common.entities.ally.illager.AbstractIllagerServant;
import za.co.infernos.goetied.common.entities.ally.illager.CryologerServant;
import za.co.infernos.goetied.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class CryologerServantRenderer<T extends CryologerServant> extends MobRenderer<T, CryologerModel<T>> {
    protected static final ResourceLocation TEXTURE = Goetied.location("textures/entity/servants/illager/cryologer.png");
    protected static final ResourceLocation ORIGINAL = Goetied.location("textures/entity/illagers/cryologer.png");

    public CryologerServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CryologerModel<>(renderManagerIn.bakeLayer(ModModelLayer.CRYOLOGER)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()){
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entitylivingbaseIn.getArmPose() != AbstractIllagerServant.IllagerServantArmPose.CROSSED) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }

            }
        });
    }

    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.isHostile() || !za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MobsConfig.CryologerServantTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE;
        }
    }
}