package za.co.infernos.goetied.common.entities.ally.ender;

import za.co.infernos.goetied.common.entities.neutral.Owned;
import za.co.infernos.goetied.common.entities.neutral.ender.AbstractSnareling;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class SnarelingServant extends AbstractSnareling {

    public SnarelingServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }
}