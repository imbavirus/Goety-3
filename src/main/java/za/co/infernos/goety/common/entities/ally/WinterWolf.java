package za.co.infernos.goety.common.entities.ally;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.neutral.Owned;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.MathHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.core.Holder;

public class WinterWolf extends BlackWolf{

    public WinterWolf(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag) {
            if (entityIn instanceof LivingEntity livingEntity) {
                Holder<MobEffect> effect = MobEffects.MOVEMENT_SLOWDOWN;
                if (CuriosFinder.hasFrostRobes(this.getMasterOwner())){
                    effect = GoetyEffects.FREEZING;
                }
                livingEntity.addEffect(new MobEffectInstance(effect, MathHelper.secondsToTicks(5), 0), this);
            }
        }
        return flag;
    }
}