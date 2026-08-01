package za.co.infernos.goetied.common.items.revive;

import net.minecraft.world.item.Rarity;

public class HowlingSoul extends ReviveServantItem {
    public HowlingSoul() {
        super(new Properties().rarity(Rarity.UNCOMMON).setNoRepair().stacksTo(1));
    }
}
