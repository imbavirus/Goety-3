package za.co.infernos.goety.mixin;

import za.co.infernos.goety.client.particles.LightningEffect;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Inject(
            method = "renderLevel",
            at = @At(
                    shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V",
                    ordinal = 0
            )
    )
    private void renderOverlays(DeltaTracker deltaTracker, boolean drawBlockOutline, Camera camera,
                                GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projMat, Matrix4f viewMat, CallbackInfo ci) {
        float partialTicks = deltaTracker.getGameTimeDeltaTicks();
        PoseStack ps = new PoseStack();
        LightningEffect.onWorldRenderLast(camera, partialTicks, ps, renderBuffers);
    }
}