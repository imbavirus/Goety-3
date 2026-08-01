package za.co.infernos.goetied.common.items.curios;

import za.co.infernos.goetied.common.enchantments.ModEnchantments;
import za.co.infernos.goetied.common.items.ModItems;
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