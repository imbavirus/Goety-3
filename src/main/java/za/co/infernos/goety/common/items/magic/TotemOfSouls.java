package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.api.items.magic.ITotem;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.config.ItemConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class TotemOfSouls extends Item implements ITotem, ICurioItem {
    public int maxSouls;

    public TotemOfSouls(int maxSouls) {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
        this.maxSouls = maxSouls;
    }

    public int getMaxSouls() {
        return this.maxSouls;
    }

    public static boolean isActivated(ItemStack itemStack) {
        return !itemStack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).isEmpty();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        if (ITotem.currentSouls(container) > za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.CraftingSouls, 0)) {
            ITotem.decreaseSouls(container, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.CraftingSouls, 0));
            return container;
        }
        return new ItemStack(ModItems.SPENT_TOTEM.get());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable("info.goety.totem_of_souls.souls", ITotem.currentSouls(stack), ITotem.maximumSouls(stack)));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ITotem.currentSouls(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int max = ITotem.maximumSouls(stack);
        if (max <= 0) {
            return 0;
        }
        return Math.round(ITotem.currentSouls(stack) * 13.0F / max);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int max = ITotem.maximumSouls(stack);
        float fill = max <= 0 ? 0.0F : Mth.clamp((float) ITotem.currentSouls(stack) / max, 0.0F, 1.0F);
        return Mth.hsvToRgb(0.52F, 0.6F, 0.7F + 0.3F * fill);
    }
}
