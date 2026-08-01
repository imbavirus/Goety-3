package za.co.infernos.goety.common.entities.ally.undead;

import za.co.infernos.goety.common.entities.neutral.AbstractReaper;
import za.co.infernos.goety.common.entities.neutral.Owned;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ReaperServant extends AbstractReaper {
    public ReaperServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }
}