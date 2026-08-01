package za.co.infernos.goety.common.items.revive;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;

public class SoulJar extends ReviveServantItem {
    public static final String TAG_CAIRN = "Cairn";
    public static final String TAG_MOSSY = "Mossy";
    public static final String TAG_DROWNED = "Drowned";
    public static final String TAG_WITHER = "Wither";

    public SoulJar() {
        super(new Properties().rarity(Rarity.UNCOMMON).setNoRepair().stacksTo(1));
    }

    public static boolean isCairn(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TAG_CAIRN);
    }

    public static void setCairn(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(TAG_CAIRN, true));
    }

    public static boolean isMossy(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TAG_MOSSY);
    }

    public static void setMossy(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(TAG_MOSSY, true));
    }

    public static boolean isDrowned(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TAG_DROWNED);
    }

    public static void setDrowned(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(TAG_DROWNED, true));
    }

    public static boolean isWither(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TAG_WITHER);
    }

    public static void setWither(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(TAG_WITHER, true));
    }
}
