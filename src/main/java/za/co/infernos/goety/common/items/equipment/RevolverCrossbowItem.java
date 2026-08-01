package za.co.infernos.goety.common.items.equipment;

import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;

public class RevolverCrossbowItem extends CrossbowItem {

    public RevolverCrossbowItem() {
        super(new Item.Properties().stacksTo(1).durability(465));
    }
}