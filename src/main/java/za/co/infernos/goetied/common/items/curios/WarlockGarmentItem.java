package za.co.infernos.goetied.common.items.curios;

import za.co.infernos.goetied.common.items.ModItems;
import za.co.infernos.goetied.common.items.WartlingEggItem;
import za.co.infernos.goetied.utils.CuriosFinder;
import za.co.infernos.goetied.utils.ItemHelper;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WarlockGarmentItem extends SingleStackItem {

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isClientSide) {
            if (entityIn instanceof Player player) {
                if (CuriosFinder.hasCurio(player, this)) {
                    if (player.tickCount % 60 == 0) {
                        if (!ItemHelper.findItem(player, ModItems.WARTFUL_EGG.get()).isEmpty()) {
                            ItemStack itemStack = ItemHelper.findItem(player, ModItems.WARTFUL_EGG.get());
                            player.getActiveEffects().stream()
                                    .filter(mobEffect -> mobEffect.getEffect().value()
                                            .getCategory() == MobEffectCategory.HARMFUL)
                                    .findFirst().ifPresent(effect -> {
                                        WartlingEggItem.warlockUse(worldIn, player, itemStack);
                                    });
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }
}