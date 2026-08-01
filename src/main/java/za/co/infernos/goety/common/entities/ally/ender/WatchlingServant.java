package za.co.infernos.goety.common.entities.ally.ender;

import za.co.infernos.goety.common.entities.neutral.Owned;
import za.co.infernos.goety.common.entities.neutral.ender.AbstractWatchling;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class WatchlingServant extends AbstractWatchling {

    public WatchlingServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }
}