package za.co.infernos.goety.client.render.layer;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.ally.undead.skeleton.*;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SkeletonServantClothingLayer<T extends AbstractSkeletonServant, M extends EntityModel<T>>
        extends RenderLayer<T, M> {
    private static final ResourceLocation TEXTURES = Goety
            .location("textures/entity/servants/skeleton/skeleton_servant_overlay.png");
    private static final ResourceLocation STRAY_ORIGINAL = ResourceLocation.withDefaultNamespace(
            "textures/entity/skeleton/stray_overlay.png");
    private static final ResourceLocation STRAY = Goety
            .location("textures/entity/servants/skeleton/stray_servant_overlay.png");
    private static final ResourceLocation MOSSY = Goety
            .location("textures/entity/servants/skeleton/mossy_skeleton_servant_overlay.png");
    private static final ResourceLocation WITHER = Goety
            .location("textures/entity/servants/skeleton/wither_skeleton_servant_overlay.png");
    private final SkeletonModel<T> layerModel;

    public SkeletonServantClothingLayer(RenderLayerParent<T, M> p_i50919_1_, EntityModelSet p_174555_) {
        super(p_i50919_1_);
        this.layerModel = new SkeletonModel<>(p_174555_.bakeLayer(ModelLayers.STRAY_OUTER_LAYER));
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T skeleton,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
            float headPitch) {
        ResourceLocation resourceLocation = null;
        if (skeleton instanceof StrayServant) {
            if (skeleton.isHostile() || !za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.StrayServantTexture, false)) {
                resourceLocation = STRAY_ORIGINAL;
            } else {
                resourceLocation = STRAY;
            }
        } else if (skeleton instanceof MossySkeletonServant && !skeleton.isHostile()
                && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.MossySkeletonServantTexture, false)) {
            resourceLocation = MOSSY;
        } else if (skeleton instanceof WitherSkeletonServant && !skeleton.isHostile()
                && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.WitherSkeletonServantTexture, false)) {
            resourceLocation = WITHER;
        } else if (skeleton instanceof SkeletonServant && !skeleton.isHostile()
                && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.SkeletonServantTexture, false)) {
            resourceLocation = TEXTURES;
        }
        if (resourceLocation != null) {
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, resourceLocation, matrixStackIn,
                    bufferIn, packedLightIn, skeleton, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                    partialTicks, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
        }
    }
}