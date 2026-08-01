package za.co.infernos.goety.common.effects;

import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.ModDamageSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SpasmEffect extends GoetyBaseEffect{
    public SpasmEffect() {
        super(MobEffectCategory.HARMFUL, 0xfffcb8);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplify) {
        if (living.getHealth() > 1.0F) {
            if (living.hurt(ModDamageSource.getDamageSource(living.level(), ModDamageSource.SHOCK), 1.0F)) {
                double x = living.level().getRandom().nextDouble() * Mth.nextInt(living.level().getRandom(), -1, 1);
                double z = living.level().getRandom().nextDouble() * Mth.nextInt(living.level().getRandom(), -1, 1);
                MobUtil.push(living, x, living.level().random.nextDouble() / 2.0D, z);
            }
        }
        return true;
    }

    public boolean isDurationEffectTick(int tick, int amplify) {
        int j = 40 >> amplify;
        if (j > 0) {
            return tick % j == 0;
        } else {
            return true;
        }
    }
}
