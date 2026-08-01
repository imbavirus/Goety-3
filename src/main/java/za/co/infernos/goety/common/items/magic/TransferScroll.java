package za.co.infernos.goety.common.items.magic;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class TransferScroll extends Item {
    private static final String TAG_SUMMON = "TransferSummon";

    public TransferScroll() {
        super(new Item.Properties().stacksTo(1));
    }

    public static boolean hasSummon(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(TAG_SUMMON);
    }

    public static LivingEntity getSummon(ItemStack stack) {
        return null;
    }

    public static void setSummon(ItemStack stack, LivingEntity entity) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putUUID(TAG_SUMMON, entity.getUUID()));
    }

    public static void removeSummon(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(TAG_SUMMON));
    }
}
