package za.co.infernos.goety.common.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WartlingEggItem extends Item {
    public WartlingEggItem() {
        super(new Properties());
    }

    public static void warlockUse(Level level, Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild && !stack.isEmpty()) {
            stack.shrink(1);
        }
    }
}
