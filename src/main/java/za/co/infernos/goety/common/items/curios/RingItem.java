package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class RingItem extends SingleStackItem {

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        if (stack.getItem() == ModItems.RING_OF_WANT.get()) {
            return enchantment == ModEnchantments.WANTING.get();
        }
        return false;
    }
}