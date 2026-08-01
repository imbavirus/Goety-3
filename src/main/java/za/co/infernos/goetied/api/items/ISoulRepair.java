package za.co.infernos.goetied.api.items;

import za.co.infernos.goetied.utils.ItemHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface ISoulRepair {

    default void repairTick(ItemStack stack, Entity entityIn, boolean isSelected){
        ItemHelper.repairTick(stack, entityIn, isSelected);
    }
}