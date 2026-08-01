package za.co.infernos.goety.client.render;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.projectiles.RazorWind;
import za.co.infernos.goety.utils.ColorUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RazorWindRenderer extends EntityRenderer<RazorWind> {
    private static final ResourceLocation[] TEXTURES = {
            Goety.location("textures/entity/projectiles/slash/1.png"),
            Goety.location("textures/entity/projectiles/slash/2.png"),
            Goety.location("textures/entity/projectiles/slash/3.png"),
            Goety.location("textures/entity/projectiles/slash/4.png")
    };

    public RazorWindRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(RazorWind entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();

        PoseStack.Pose pose = poseStack.last();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        entity.animationTime++;
        float oldWith = (float) entity.oldBB.getXsize();
        float width = entity.getBbWidth();
        width = oldWith + (width - oldWith) * Math.min(partialTicks, 1);

        drawSlash(pose,entity, bufferSource, light, width, 0);

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(PoseStack.Pose pose, RazorWind entity, MultiBufferSource bufferSource, int light, float width, int offset) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity,offset)));

        VertexConsumer consumer2 = bufferSource.getBuffer(ModRenderType.wraith(getTextureLocation(entity,offset)));
        float halfWidth = width * 0.5F;

        ColorUtil colorUtil = new ColorUtil(ChatFormatting.WHITE);
        float alpha = 0.5F;
        Vector3f n = new Vector3f(0.0F, 1.0F, 0.0F).mul(normalMatrix);
        consumer.addVertex(poseMatrix, -halfWidth, -0.1F, -halfWidth).setColor(colorUtil.red, colorUtil.green, colorUtil.blue, alpha).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(n.x, n.y, n.z);
        consumer.addVertex(poseMatrix, halfWidth, -0.1F, -halfWidth).setColor(colorUtil.red, colorUtil.green, colorUtil.blue, alpha).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(n.x, n.y, n.z);
        consumer.addVertex(poseMatrix, halfWidth, -0.1F, halfWidth).setColor(colorUtil.red, colorUtil.green, colorUtil.blue, alpha).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(n.x, n.y, n.z);
        consumer.addVertex(poseMatrix, -halfWidth, -0.1F, halfWidth).setColor(colorUtil.red, colorUtil.green, colorUtil.blue, alpha).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(n.x, n.y, n.z);

        consumer2.addVertex(poseMatrix, -halfWidth, -0.1F, -halfWidth).setColor(colorUtil.red, colorUtil.green, colorUtil.blue, alpha).setUv(0.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(n.x, n.y, n.z);
        consumer2.addVertex(poseMatrix, halfWidth, -0.1F, -halfWidth).setColor(colorUtil.red, colorUtil.green, colorUtil.blue, alpha).setUv(1.0F, 1.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(n.x, n.y, n.z);
        consumer2.addVertex(poseMatrix, halfWidth, -0.1F, halfWidth).setColor(colorUtil.red, colorUtil.green, colorUtil.blue, alpha).setUv(1.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(n.x, n.y, n.z);
        consumer2.addVertex(poseMatrix, -halfWidth, -0.1F, halfWidth).setColor(colorUtil.red, colorUtil.green, colorUtil.blue, alpha).setUv(0.0F, 0.0F).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(n.x, n.y, n.z);
    }

    @Override
    public ResourceLocation getTextureLocation(RazorWind entity) {
        int frame = (entity.animationTime / 4) % TEXTURES.length;
        return TEXTURES[frame];
    }

    private ResourceLocation getTextureLocation(RazorWind entity,int offset) {
        int frame = (entity.animationTime / 6 + offset) % TEXTURES.length;
        return TEXTURES[frame];
    }
}