package za.co.infernos.goety.client.render.layer;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.ModModelLayer;
import za.co.infernos.goety.client.render.model.NecroCapeModel;
import za.co.infernos.goety.common.entities.ally.Doppelganger;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class NamelessSetLayer<T extends Doppelganger> extends RenderLayer<T, PlayerModel<T>> {
    private static final ResourceLocation TEXTURES = Goety.location("textures/models/curios/nameless_cape.png");
    private final NecroCapeModel<T> layerModel;

    public NamelessSetLayer(RenderLayerParent<T, PlayerModel<T>> p_i50919_1_, EntityModelSet p_174555_) {
        super(p_i50919_1_);
        this.layerModel = new NecroCapeModel<>(p_174555_.bakeLayer(ModModelLayer.NAMELESS_SET));
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isUndeadClone()) {
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, -1);
        }
    }
}