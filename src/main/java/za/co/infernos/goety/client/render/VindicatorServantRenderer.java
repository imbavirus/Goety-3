package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.layer.HierarchicalArmorLayer;
import za.co.infernos.goety.client.render.model.IllagerServantModel;
import za.co.infernos.goety.common.entities.ally.illager.VindicatorServant;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class VindicatorServantRenderer<T extends VindicatorServant> extends MobRenderer<T, IllagerServantModel<T>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/illager/vindicator.png");
    protected static final ResourceLocation ORIGINAL = Goety.location("textures/entity/illager/vindicator.png");

    public VindicatorServantRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new IllagerServantModel<>(renderManagerIn.bakeLayer(ModModelLayer.ILLAGER_SERVANT)), 0.5F);
        this.addLayer(new HierarchicalArmorLayer<>(this, renderManagerIn));
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()) {
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T illager, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (illager.isAggressive()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, illager, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
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
        if (entity.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.VindicatorServantTexture, false)){
            return ORIGINAL;
        } else {
            return TEXTURE;
        }
    }
}