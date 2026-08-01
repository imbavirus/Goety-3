package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.api.items.IPersist;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class EsotericTesseract extends Item implements IPersist {
    public static String BIG = "Big";
    public static String LARGE = "Large";
    public static String HUGE = "Huge";

    public EsotericTesseract() {
        super(new Properties().stacksTo(1).fireResistant().durability(128).rarity(Rarity.RARE));
    }

    public static int getServantsInTesseract(ItemStack stack) {
        return 0;
    }
}
