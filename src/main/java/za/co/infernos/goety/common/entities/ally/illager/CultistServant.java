package za.co.infernos.goety.common.entities.ally.illager;

import za.co.infernos.goety.common.entities.neutral.Owned;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

public abstract class CultistServant extends RaiderServant{

    public CultistServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.getNavigation().setCanFloat(true);
    }

    public CultistServantArmPose getArmPose() {
        return CultistServantArmPose.CROSSED;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.62F;
    }

    public enum CultistServantArmPose {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        PRAYING,
        SPELL_AND_WEAPON,
        BOW_AND_ARROW,
        TORCH_AND_WEAPON,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        BOMB_AND_WEAPON,
        THROW_SPEAR,
        ITEM,
        DYING,
        NEUTRAL;
    }
}