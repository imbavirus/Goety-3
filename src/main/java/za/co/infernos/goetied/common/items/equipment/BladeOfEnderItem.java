package za.co.infernos.goetied.common.items.equipment;

import za.co.infernos.goetied.common.items.ModTiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

public class BladeOfEnderItem extends SwordItem {

    public BladeOfEnderItem() {
        super(ModTiers.VOID, new Item.Properties().fireResistant());
    }
}