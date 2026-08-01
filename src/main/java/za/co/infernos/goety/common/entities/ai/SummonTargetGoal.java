package za.co.infernos.goety.common.entities.ai;

import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;

import java.util.function.Predicate;

public class SummonTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {

    public SummonTargetGoal(Mob ownedEntity) {
        super(ownedEntity, LivingEntity.class, 5, true, false, predicate(ownedEntity));
    }

    public SummonTargetGoal(Mob ownedEntity, boolean pMustSee, boolean pMustReach) {
        super(ownedEntity, LivingEntity.class, 5, pMustSee, pMustReach, predicate(ownedEntity));
    }

    public boolean canUse() {
        boolean flag = super.canUse();
        if (this.mob instanceof IOwned owned){
            if (owned.getTrueOwner() != null && this.target == owned.getTrueOwner()){
                return false;
            }
            if (owned.isHostile()){
                return !(this.target instanceof Enemy) && flag;
            }
        }
        return flag;
    }

    public static Predicate<LivingEntity> predicate(LivingEntity attacker){
        return target -> MobUtil.isOwnedTargetable(attacker, target);
    }
}