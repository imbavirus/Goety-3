package za.co.infernos.goety.common.items.magic;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class InfernalTome extends Item {
    public static String CHANT_TIMES = "Chant Times";

    public InfernalTome() {
        super(new Properties().durability(64).fireResistant().rarity(Rarity.UNCOMMON));
    }

    public static int getChantTimes(ItemStack stack) {
        net.minecraft.world.item.component.CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
        return data.isEmpty() ? 0 : data.copyTag().getInt(CHANT_TIMES);
    }

    public static void setChantTimes(ItemStack stack, int time) {
        net.minecraft.world.item.component.CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(CHANT_TIMES, time));
    }

    public static void increaseChantTimes(ItemStack stack) {
        setChantTimes(stack, getChantTimes(stack) + 1);
    }

    public static boolean isChanting(ItemStack stack) {
        return getChantTimes(stack) > 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<net.minecraft.network.chat.Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
    }
}
