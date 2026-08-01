package za.co.infernos.goetied.common.effects;

import za.co.infernos.goetied.client.particles.GroundAuraParticle;
import za.co.infernos.goetied.utils.ColorUtil;
import za.co.infernos.goetied.utils.MobUtil;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class AuraEffect extends GoetiedBaseEffect {

    public AuraEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplify) {
        Holder<MobEffect> effect = null;
        if (this == GoetiedEffects.SHIELDING.get()) {
            effect = GoetiedEffects.SHIELDED;
        } else if (this == GoetiedEffects.RALLYING.get()) {
            effect = GoetiedEffects.RALLIED;
        }
        if (effect != null) {
            for (LivingEntity ally : livingEntity.level().getEntitiesOfClass(LivingEntity.class, livingEntity.getBoundingBox().inflate(8.0D))) {
                if (ally != livingEntity && MobUtil.areAllies(livingEntity, ally)) {
                    ally.addEffect(new MobEffectInstance(effect, 5, amplify, false, false));
                }
            }
            if (livingEntity.tickCount % 20 == 0) {
                if (livingEntity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(new GroundAuraParticle.Option(livingEntity.getId(), 4.0F, new ColorUtil(this.getColor())), livingEntity.getX(), livingEntity.getY() + 0.25F, livingEntity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0.0F);
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tick, int level) {
        return true;
    }
}
