package za.co.infernos.goety.common.entities.ally.spider;

import za.co.infernos.goety.common.entities.neutral.AbstractBroodMother;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BroodMotherServant extends AbstractBroodMother {
    public BroodMotherServant(EntityType<? extends AbstractBroodMother> type, Level worldIn) {
        super(type, worldIn);
    }
}