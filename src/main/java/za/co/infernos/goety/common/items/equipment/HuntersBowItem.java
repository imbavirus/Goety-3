package za.co.infernos.goety.common.items.equipment;

import za.co.infernos.goety.config.ItemConfig;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class HuntersBowItem extends BowItem {
    public HuntersBowItem() {
        super((new Properties()).rarity(Rarity.UNCOMMON).durability(getHuntersBowDurability()));
    }
    
    private static int getHuntersBowDurability() {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.HuntersBowDurability, 0);
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 384; // Default bow durability
        }
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.getItem() instanceof BowItem;
    }
}