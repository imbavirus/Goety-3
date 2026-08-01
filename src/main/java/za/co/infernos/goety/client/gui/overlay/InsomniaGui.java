package za.co.infernos.goety.client.gui.overlay;

import za.co.infernos.goety.Goety;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.resources.ResourceLocation;

public class InsomniaGui {
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static final ResourceLocation LAYER_ID = Goety.location("insomnia_hud");
    public static final LayeredDraw.Layer LAYER = (guiGraphics, partialTick) -> {
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        drawHUD(guiGraphics, partialTick, screenWidth, screenHeight);
    };

    public static boolean shouldDisplayBar(){
//        return minecraft.player != null && (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR);
        return false;
    }

    public static void drawHUD(GuiGraphics guiGraphics, DeltaTracker partialTick, int screenWidth, int screenHeight) {
        if (minecraft.player == null){
            return;
        }
        if(!shouldDisplayBar()) {
            return;
        }

        StatsCounter stats = minecraft.player.getStats();
        int i = Mth.clamp(stats.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
        int xOffset = 10;
        int yOffset = 10;
        // Boss overlay stack management changed in 1.21; we simply don't offset for it here.
        int bossBars = 0;
        if (bossBars > 0) {
            yOffset += Math.min(screenHeight / 3, 12 + 19 * bossBars);
        }
        int potionsActive = 0;

        for (MobEffectInstance mobEffectInstance : minecraft.player.getActiveEffects()) {
            if (mobEffectInstance.showIcon()) {
                ++potionsActive;
            }
        }

        yOffset += Math.min(potionsActive, 2) * 24;

        int eye = 0;
        if (i >= 72000){
            eye = 32;
        } else if (i >= 24000){
            eye = 16;
        }
        guiGraphics.blit(Goety.location("textures/gui/insomnia.png"), xOffset, yOffset, eye, 0, 16, 16, 48, 16);

    }
}