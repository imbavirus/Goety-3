package za.co.infernos.goety.common.items;

import net.minecraft.world.item.Item;

public class QuickGrowSeedItem extends Item {
    private final boolean poisonous;

    public QuickGrowSeedItem() {
        this(false);
    }

    public QuickGrowSeedItem(boolean poisonous) {
        super(new Properties());
        this.poisonous = poisonous;
    }

    public boolean isPoisonous() {
        return this.poisonous;
    }
}
