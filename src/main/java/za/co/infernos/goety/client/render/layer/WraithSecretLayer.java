package za.co.infernos.goety.client.render.layer;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.ModModelLayer;
import za.co.infernos.goety.client.render.model.WraithModel;
import za.co.infernos.goety.common.entities.neutral.AbstractWraith;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class WraithSecretLayer<T extends AbstractWraith> extends RenderLayer<T, WraithModel<T>> {
    private static final ResourceLocation TEXTURES = Goety.location("textures/entity/wraith/wraith_secret.png");
    private final WraithModel<T> layerModel;

    public WraithSecretLayer(RenderLayerParent<T, WraithModel<T>> p_i50919_1_, EntityModelSet p_174555_) {
        super(p_i50919_1_);
        this.layerModel = new WraithModel<>(p_174555_.bakeLayer(ModModelLayer.WRAITH));
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isInterested()) {
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 0xFFFFFFFF);
        }
    }
}