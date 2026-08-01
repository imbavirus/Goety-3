package za.co.infernos.goety.common.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Item;

public class UnholyBloodItem extends Item {
    private static final String TAG_PURE = "Pure";

    public UnholyBloodItem() {
        super(new Properties());
    }

    public static void addPure(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(TAG_PURE, true));
    }
}
