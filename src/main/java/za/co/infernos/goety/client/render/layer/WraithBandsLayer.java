package za.co.infernos.goety.client.render.layer;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.ModModelLayer;
import za.co.infernos.goety.client.render.model.WraithModel;
import za.co.infernos.goety.common.entities.neutral.AbstractWraith;
import za.co.infernos.goety.config.MobsConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class WraithBandsLayer<T extends AbstractWraith> extends RenderLayer<T, WraithModel<T>> {
    private static final ResourceLocation TEXTURES = Goety.location("textures/entity/wraith/wraith_servant_bands.png");
    private final WraithModel<T> layerModel;

    public WraithBandsLayer(RenderLayerParent<T, WraithModel<T>> p_i50919_1_, EntityModelSet p_174555_) {
        super(p_i50919_1_);
        this.layerModel = new WraithModel<>(p_174555_.bakeLayer(ModModelLayer.WRAITH));
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T wraith, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.WraithServantTexture, false) && !wraith.isHostile()) {
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, wraith, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 0xFFFFFFFF);
        }
    }
}