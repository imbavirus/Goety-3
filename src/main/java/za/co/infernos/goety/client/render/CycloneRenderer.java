package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.CycloneModel;
import za.co.infernos.goety.common.entities.projectiles.Cyclone;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CycloneRenderer extends EntityRenderer<Cyclone> {
    private static final ResourceLocation TEXTURES = ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID,
            "textures/entity/projectiles/cyclone.png");
    private final CycloneModel<Cyclone> model;

    public CycloneRenderer(EntityRendererProvider.Context p_i46179_1_) {
        super(p_i46179_1_);
        this.model = new CycloneModel<>(p_i46179_1_.bakeLayer(ModModelLayer.FIRE_TORNADO));
    }

    public void render(Cyclone entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn,
            MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(getRenderType(entityIn));
        this.model.setupAnim(entityIn, 0.0F, 0.0F, entityIn.tickCount + partialTicks, 0, 0);
        matrixStackIn.translate(0.0D, (double) (entityIn.getBbHeight()), 0.0D);
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
        float size = entityIn.getSize();
        matrixStackIn.scale(size, size, size);
        this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY,
                net.minecraft.util.FastColor.ARGB32.color((int) (0.45F * 255), 255, 255, 255));
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected RenderType getRenderType(Cyclone p_230496_1_) {
        return RenderType.entityTranslucent(getTextureLocation(p_230496_1_));
    }

    @Override
    public ResourceLocation getTextureLocation(Cyclone pEntity) {
        return TEXTURES;
    }
}