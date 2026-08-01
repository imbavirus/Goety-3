package za.co.infernos.goetied.common.entities.hostile.ender;

import za.co.infernos.goetied.common.entities.neutral.Owned;
import za.co.infernos.goetied.common.entities.neutral.ender.AbstractSnareling;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class Snareling extends AbstractSnareling implements Enemy {

    public Snareling(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
        this.setHostile(true);
    }
}