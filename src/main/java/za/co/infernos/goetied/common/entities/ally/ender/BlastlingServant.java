package za.co.infernos.goetied.common.entities.ally.ender;

import za.co.infernos.goetied.common.entities.neutral.Owned;
import za.co.infernos.goetied.common.entities.neutral.ender.AbstractBlastling;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BlastlingServant extends AbstractBlastling {

    public BlastlingServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }
}