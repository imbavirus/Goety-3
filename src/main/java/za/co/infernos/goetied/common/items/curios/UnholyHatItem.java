package za.co.infernos.goetied.common.items.curios;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.effects.GoetiedEffects;
import za.co.infernos.goetied.utils.CuriosFinder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class UnholyHatItem extends MagicCrownItem {

    public UnholyHatItem() {
        super(new Properties().fireResistant().stacksTo(1), SpellType.NETHER);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isClientSide) {
            if (entityIn instanceof LivingEntity livingEntity) {
                if (CuriosFinder.hasCurio(livingEntity, this)) {
                    if (livingEntity.hasEffect(GoetiedEffects.BURN_HEX)){
                        livingEntity.removeEffect(GoetiedEffects.BURN_HEX);
                    }
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}