package za.co.infernos.goety.common.items.brew;

import za.co.infernos.goety.utils.BrewUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import za.co.infernos.goety.utils.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BrewArrowItem extends ArrowItem {
   public BrewArrowItem(Properties p_43354_) {
      super(p_43354_);
   }

   public ItemStack getDefaultInstance() {
      ItemStack stack = super.getDefaultInstance();
      stack.set(net.minecraft.core.component.DataComponents.POTION_CONTENTS, new net.minecraft.world.item.alchemy.PotionContents(net.minecraft.world.item.alchemy.Potions.WATER));
      return stack;
   }

   public void appendHoverText(ItemStack p_43359_, @Nullable Level p_43360_, List<Component> p_43361_, TooltipFlag p_43362_) {
      BrewUtils.addBrewTooltip(p_43359_, p_43361_, 0.125F);
   }
}