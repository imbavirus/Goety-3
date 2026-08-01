package za.co.infernos.goety.client.gui.overlay;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.hostile.Wight;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class DreadOverlay {
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static final ResourceLocation LAYER_ID = Goety.location("static_overlay");
    public static final LayeredDraw.Layer LAYER = (guiGraphics, partialTick) -> {
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        drawOverlay(guiGraphics, partialTick, screenWidth, screenHeight);
    };

    public static void drawOverlay(GuiGraphics ms, DeltaTracker partialTick, int screenWidth, int screenHeight) {
        if (minecraft.player != null){
            Player player = minecraft.player;
            Wight wight = Wight.findWight(player);
            if (wight != null) {
                ResourceLocation overlay;
                int frame = minecraft.gui.getGuiTicks() % 16;

                overlay = switch (frame) {
                    default -> Goety.location("textures/gui/dread/dread_overlay_0.png");
                    case 4, 5, 6, 7 -> Goety.location("textures/gui/dread/dread_overlay_1.png");
                    case 8, 9, 10, 11 -> Goety.location("textures/gui/dread/dread_overlay_2.png");
                    case 12, 13, 14, 15 -> Goety.location("textures/gui/dread/dread_overlay_3.png");
                };

                float alpha = 1.0F - (Math.min(1.0F, wight.distanceTo(player) / 48.0F));
                renderOverlay(ms, overlay, alpha, screenWidth, screenHeight);
            }
        }
    }

    public static void renderOverlay(GuiGraphics guiGraphics, ResourceLocation location, float alpha, int screenWidth, int screenHeight) {
        // Using GuiGraphics for rendering in 1.21.1
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.blit(location, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.pose().popPose();
    }
}