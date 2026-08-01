package za.co.infernos.goety.common.items;

import za.co.infernos.goety.api.items.ISoulRepair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import javax.annotation.Nonnull;

public class PhilosophersStone extends Item implements IItemExtension, ISoulRepair {
    public PhilosophersStone(){
        super(new Properties().durability(64));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        if (container.getDamageValue() <= container.getMaxDamage()) {
            container.setDamageValue(itemStack.getDamageValue() + 1);
        } else {
            container = ItemStack.EMPTY;
        }
        return container;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.getItem() == Items.CRYING_OBSIDIAN || super.isValidRepairItem(pToRepair, pRepair);
    }

}