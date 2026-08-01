package za.co.infernos.goetied.common.items.equipment;

import za.co.infernos.goetied.common.items.ModItems;
import za.co.infernos.goetied.common.items.ModTiers;
import net.minecraft.world.item.*;

public class ModToolItems {

    public static class DarkSwordItem extends SwordItem{

        public DarkSwordItem() {
            super(ModTiers.DARK, ModItems.baseProperties());
        }

        @Override
        public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
            return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
        }
    }

    public static class DarkShovelItem extends ShovelItem{

        public DarkShovelItem() {
            super(ModTiers.DARK, ModItems.baseProperties());
        }

        @Override
        public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
            return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
        }
    }

    public static class DarkPickaxeItem extends PickaxeItem{

        public DarkPickaxeItem() {
            super(ModTiers.DARK, ModItems.baseProperties());
        }

        @Override
        public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
            return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
        }
    }

    public static class DarkAxeItem extends AxeItem{

        public DarkAxeItem() {
            super(ModTiers.DARK, ModItems.baseProperties());
        }

        @Override
        public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
            return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
        }
    }

    public static class DarkHoeItem extends HoeItem{

        public DarkHoeItem() {
            super(ModTiers.DARK, ModItems.baseProperties());
        }

        @Override
        public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
            return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
        }
    }
}