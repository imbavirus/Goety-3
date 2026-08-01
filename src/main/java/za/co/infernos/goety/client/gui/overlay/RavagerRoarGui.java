package za.co.infernos.goety.client.gui.overlay;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.neutral.IRavager;
import za.co.infernos.goety.config.MainConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

public class RavagerRoarGui {
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static final ResourceLocation LAYER_ID = Goety.location("ravager_roar_hud");
    public static final LayeredDraw.Layer LAYER = (guiGraphics, partialTick) -> {
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        drawHUD(guiGraphics, partialTick, screenWidth, screenHeight);
    };

    public static boolean shouldDisplayBar(){
        return minecraft.player != null && minecraft.player.getVehicle() instanceof IRavager ravager && ravager.getRoarCool() > 0;
    }

    public static void drawHUD(GuiGraphics guiGraphics, DeltaTracker partialTick, int screenWidth, int screenHeight) {
        if(!shouldDisplayBar()) {
            return;
        }
        if (minecraft.player == null){
            return;
        }
        int i = (screenWidth/2) + (za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SoulGuiHorizontal, 0));
        int RoarCool = 0;
        int RoarCoolTotal = 1;
        if (minecraft.player.getVehicle() instanceof IRavager ravager){
            RoarCool = ravager.getRoarCool();
            RoarCoolTotal = ravager.getRoarCoolMax();
        }
        int roarLength = 80;
        roarLength *= (RoarCool / (double)RoarCoolTotal);
        int height = screenHeight + (za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SoulGuiVertical, 0) - 20);
        guiGraphics.blit(Goety.location("textures/gui/ravager_roar_bar.png"), i, height - 9, 0, 0, 96,16, 96, 32);
        guiGraphics.blit(Goety.location("textures/gui/ravager_roar_bar.png"), i + 16, height - 9, 16, 16, roarLength,16, 96, 32);
    }
}