package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.utils.CuriosFinder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IllusionRobeItem extends SingleStackItem {

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isClientSide) {
            if (entityIn instanceof LivingEntity livingEntity) {
                if (CuriosFinder.hasCurio(livingEntity, this)) {
                    if (livingEntity.hasEffect(MobEffects.BLINDNESS)){
                        livingEntity.removeEffect(MobEffects.BLINDNESS);
                    }
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }
}