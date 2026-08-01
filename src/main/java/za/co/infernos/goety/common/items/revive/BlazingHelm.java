package za.co.infernos.goety.common.items.revive;

import net.minecraft.world.item.Rarity;

public class BlazingHelm extends ReviveServantItem {
    public BlazingHelm() {
        super(new Properties().rarity(Rarity.UNCOMMON).setNoRepair().fireResistant().stacksTo(1));
    }
}
