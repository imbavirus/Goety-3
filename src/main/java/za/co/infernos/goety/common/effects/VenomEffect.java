package za.co.infernos.goety.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class VenomEffect extends GoetyBaseEffect {
    public VenomEffect() {
        super(MobEffectCategory.HARMFUL, 0x44b529);
    }

    @Override
    public boolean applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if (p_19467_.getHealth() > p_19467_.getMaxHealth() * 0.1F) {
            p_19467_.hurt(p_19467_.damageSources().magic(), 1.0F);
        }
        return true;
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        int j = 25 >> p_19456_;
        if (j > 0) {
            return p_19455_ % j == 0;
        } else {
            return true;
        }
    }
}