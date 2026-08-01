package za.co.infernos.goety.common.entities.ally.undead.skeleton;

import za.co.infernos.goety.common.entities.neutral.AbstractNecromancer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NecromancerServant extends AbstractNecromancer {
    public NecromancerServant(EntityType<? extends AbstractNecromancer> type, Level level) {
        super(type, level);
    }
}