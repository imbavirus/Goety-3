package za.co.infernos.goety.common.entities.hostile.ender;

import za.co.infernos.goety.common.entities.neutral.Owned;
import za.co.infernos.goety.common.entities.neutral.ender.AbstractBlastling;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class Blastling extends AbstractBlastling implements Enemy {

    public Blastling(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
        this.setHostile(true);
    }
}