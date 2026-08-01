package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.client.render.item.CustomItemsRenderer;
import za.co.infernos.goety.config.ItemConfig;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class NamelessStaff extends DarkStaff{
    public NamelessStaff() {
        // Lazy evaluation to avoid accessing config before it's loaded
        super(getNamelessStaffDamage(), SpellType.NECROMANCY);
    }
    
    private static double getNamelessStaffDamage() {
        try {
            return za.co.infernos.goety.utils.ConfigHelper.getDouble(ItemConfig.NamelessStaffDamage, 20.0D);
        } catch (IllegalStateException e) {
            // Config not loaded yet, use default
            return 4.0D;
        }
    }


}