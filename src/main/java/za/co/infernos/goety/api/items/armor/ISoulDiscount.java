package za.co.infernos.goety.api.items.armor;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;

public interface ISoulDiscount {

    @Deprecated
    default int getSoulDiscount(EquipmentSlot equipmentSlot) {
        return 0;
    }

    default int getSoulDiscount(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        return 0;
    }

    default Component soulDiscountTooltip(ItemStack itemStack){
        EquipmentSlot slot = itemStack.getItem() instanceof Equipable equipable ? equipable.getEquipmentSlot() : EquipmentSlot.MAINHAND;
        int discount = this.getSoulDiscount(slot, itemStack);
        if (discount > 0) {
            return Component.literal(String.valueOf(discount)).append("% ").append(Component.translatable("info.goety.armor.discount")).withStyle(ChatFormatting.DARK_AQUA);
        } else {
            return Component.empty();
        }
    }
}