package za.co.infernos.goety.client.gui.overlay;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.items.magic.ITotem;
import za.co.infernos.goety.api.magic.IChargingSpell;
import za.co.infernos.goety.common.items.magic.FullSpentTotem;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.SEHelper;
import za.co.infernos.goety.utils.TotemFinder;
import za.co.infernos.goety.utils.WandUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.DeltaTracker;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

public class SoulEnergyGui {
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static final ResourceLocation LAYER_ID = Goety.location("soul_energy_hud");
    public static final LayeredDraw.Layer LAYER = (guiGraphics, partialTick) -> {
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        drawHUD(guiGraphics, partialTick, screenWidth, screenHeight);
    };

    public static boolean shouldDisplayBar() {
        return SEHelper.getSoulsContainer(minecraft.player) && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.SoulGuiShow, false)
                && (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR);
    }

    public static Font getFont() {
        return minecraft.font;
    }

    public static void drawHUD(GuiGraphics guiGraphics, DeltaTracker partialTick, int screenWidth, int screenHeight) {
        if (!shouldDisplayBar()) {
            return;
        }

        ItemStack stack = TotemFinder.FindTotem(minecraft.player);
        int SoulEnergy = 0;
        int SoulEnergyTotal = za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxSouls, 0);
        if (SEHelper.getSEActive(minecraft.player)) {
            SoulEnergy = SEHelper.getSESouls(minecraft.player);
            SoulEnergyTotal = za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxArcaSouls, 0);
        } else if (!stack.isEmpty()) {
            net.minecraft.world.item.component.CustomData tag = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
            if (tag != null && tag.contains(ITotem.SOULS_AMOUNT)) {
                SoulEnergy = tag.copyTag().getInt(ITotem.SOULS_AMOUNT);
                if (tag.contains(ITotem.MAX_SOUL_AMOUNT)) {
                    SoulEnergyTotal = ITotem.maximumSouls(stack);
                }
            }
        }

        int i = (screenWidth / 2) + (za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SoulGuiHorizontal, 0));
        int energylength = (int) (117 * (SoulEnergy / (double) SoulEnergyTotal));
        int maxenergy = (int) (117 * (za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxSouls, 0) / (double) SoulEnergyTotal));

        int height = screenHeight + (za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SoulGuiVertical, 0));

        int offset = (int) ((minecraft.player.tickCount + partialTick.getGameTimeDeltaPartialTick(false)) % 234);

        if (SEHelper.getSEActive(minecraft.player)) {
            guiGraphics.blit(Goety.location("textures/gui/soul_energy.png"), i, height - 9, 0, 9, 128, 9, 128, 90);
            guiGraphics.blit(Goety.location("textures/gui/soul_energy.png"), i + 9, height - 9, 9, 18, maxenergy, 9,
                    128, 90);
        } else {
            int height1 = stack.getItem() instanceof FullSpentTotem ? 36 : 0;
            guiGraphics.blit(Goety.location("textures/gui/soul_energy.png"), i, height - 9, 0, height1, 128, 9, 128,
                    90);
        }
        RenderSystem.setShaderTexture(0, Goety.location("textures/gui/soul_energy_bar.png"));
        guiGraphics.blit(Goety.location("textures/gui/soul_energy_bar.png"), i + 9, height - 7, offset, 0, energylength,
                5, 234, 5);

        if (MobUtil.isSpellCasting(minecraft.player)) {
            ItemStack useItem = minecraft.player.getUseItem();
            int useDuration = useItem.getItem().getUseDuration(useItem, minecraft.player);
            float remain = minecraft.player.getUseItemRemainingTicks();
            float useTime0 = (useDuration - remain) / useDuration;
            int bar = 27;
            if (WandUtil.getSpell(minecraft.player) instanceof IChargingSpell spell) {
                if (WandUtil.getShots(minecraft.player) > 0
                        && spell.shotsNumber(minecraft.player, minecraft.player.getUseItem()) > 0) {
                    useDuration = spell.shotsNumber(minecraft.player, minecraft.player.getUseItem());
                    remain = WandUtil.getShots(minecraft.player);
                    useTime0 = remain / useDuration;
                    bar = 45;
                } else if (spell.castUp(minecraft.player, minecraft.player.getUseItem()) > 0) {
                    useDuration = spell.castUp(minecraft.player, minecraft.player.getUseItem());
                    remain = useItem.getItem().getUseDuration(useItem, minecraft.player)
                            - minecraft.player.getUseItemRemainingTicks();
                    useTime0 = remain / useDuration;
                }
            }
            useTime0 = Math.min(1.0F, useTime0);
            int useTime = (int) (117 * useTime0);
            guiGraphics.blit(Goety.location("textures/gui/soul_energy.png"), i + 9, height - 9, 9, bar, useTime, 9, 128,
                    90);
        }

        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.ShowNum, false)) {
            minecraft.getProfiler().push("soulenergy");
            String s = SoulEnergy + "/" + SoulEnergyTotal;
            int i1 = i + 37;
            int j1 = height - 8;
            guiGraphics.drawString(getFont(), s, i1, j1, 16777215);
            minecraft.getProfiler().pop();
        }
    }
}