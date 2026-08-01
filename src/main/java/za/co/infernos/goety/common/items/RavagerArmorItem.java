package za.co.infernos.goety.common.items;

import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

public class RavagerArmorItem extends Item {
    private final int protection;
    private final String tier;

    public RavagerArmorItem(int protection, String tier) {
        this(protection, tier, new Properties().stacksTo(1));
    }

    public RavagerArmorItem(int protection, String tier, Item.Properties properties) {
        super(properties);
        this.protection = protection;
        this.tier = tier;
    }

    public int getProtection() {
        return this.protection;
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.parse("goety:textures/entity/ravager_armor/" + this.tier + ".png");
    }
}
