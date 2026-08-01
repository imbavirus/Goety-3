package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.api.items.magic.ITotem;
import za.co.infernos.goety.config.ItemConfig;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class FullSpentTotem extends TotemOfSouls{

    public FullSpentTotem(int maxSouls) {
        super(maxSouls);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        if (ITotem.currentSouls(container) > za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.CraftingSouls, 0)) {
            ITotem.decreaseSouls(container, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.CraftingSouls, 0));
            return container;
        }
        return ItemStack.EMPTY;
    }
}