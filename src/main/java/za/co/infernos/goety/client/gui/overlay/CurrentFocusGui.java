package za.co.infernos.goety.client.gui.overlay;

import za.co.infernos.goety.client.gui.screen.inventory.FocusRadialMenuScreen;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;

public class CurrentFocusGui {
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static final ResourceLocation LAYER_ID = za.co.infernos.goety.Goety.location("current_focus_hud");
    public static final LayeredDraw.Layer LAYER = (guiGraphics, partialTick) -> {
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        drawHUD(guiGraphics, partialTick, screenWidth, screenHeight);
    };

    public static boolean shouldDisplayBar(){
        return !WandUtil.findFocus(minecraft.player).isEmpty() && (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) && !(minecraft.screen instanceof FocusRadialMenuScreen) && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.FocusGuiShow, false);
    }

    public static void drawHUD(GuiGraphics guiGraphics, DeltaTracker partialTick, int screenWidth, int screenHeight) {
        if(!shouldDisplayBar()) {
            return;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(1.0F, 1.0F, 1.0F);
        if (WandUtil.findFocus(minecraft.player) != null) {
            guiGraphics.renderFakeItem(WandUtil.findFocus(minecraft.player), ((screenWidth - 16) / 2) + za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.FocusGuiHorizontal, 0), (screenHeight - 52) + za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.FocusGuiVertical, 0));
            guiGraphics.renderItemDecorations(minecraft.font, WandUtil.findFocus(minecraft.player), ((screenWidth - 16) / 2) + za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.FocusGuiHorizontal, 0), (screenHeight - 52) + za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.FocusGuiVertical, 0));
        }
        guiGraphics.pose().popPose();
    }
}