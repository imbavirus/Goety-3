package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.render.model.EarthFistModel;
import za.co.infernos.goety.common.entities.projectiles.EarthFist;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EarthFistRenderer extends EntityRenderer<EarthFist> {
    private static final ResourceLocation TEXTURE = Goety.location("textures/entity/projectiles/earth_fist.png");
    private final EarthFistModel<EarthFist> model;

    public EarthFistRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.model = new EarthFistModel<>(renderManagerIn.bakeLayer(ModModelLayer.EARTH_FIST));
    }

    public void render(EarthFist pEntity, float entityYaw, float pPartialTicks, PoseStack pMatrixStack,
            MultiBufferSource bufferIn, int packedLightIn) {
        if (pEntity.isSentSpikeEvent()) {
            pMatrixStack.pushPose();
            pMatrixStack.mulPose(Axis.YP.rotationDegrees(90.0F - pEntity.getYRot()));
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
            pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
            pMatrixStack.translate(0.0D, -1.0D, 0.0D);
            float f7 = this.getBob(pEntity, pPartialTicks);
            this.model.setupAnim(pEntity, 0.0F, 0.0F, f7, 0, 0);
            this.model.renderToBuffer(pMatrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY,
                    net.minecraft.util.FastColor.ARGB32.color((int) (0.15F * 255), 255, 255, 255));
            pMatrixStack.popPose();
            super.render(pEntity, entityYaw, pPartialTicks, pMatrixStack, bufferIn, packedLightIn);
        }
    }

    protected float getBob(EarthFist p_115305_, float p_115306_) {
        return (float) p_115305_.tickCount + p_115306_;
    }

    @Override
    public ResourceLocation getTextureLocation(EarthFist pEntity) {
        return TEXTURE;
    }
}