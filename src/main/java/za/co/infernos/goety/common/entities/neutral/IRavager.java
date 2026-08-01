package za.co.infernos.goety.common.entities.neutral;

import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.utils.MathHelper;
import net.minecraft.world.item.ItemStack;

public interface IRavager {
    ItemStack getArmor();

    default int getRoarCool(){
        return 0;
    }

    default int getRoarCoolMax(){
        return MathHelper.secondsToTicks(za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.RavagerRoarCooldown, 0));
    }

    default void forceRoar(){
    }
}