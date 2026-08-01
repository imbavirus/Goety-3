package za.co.infernos.goety.common.entities.ally.undead.zombie;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.ally.Summoned;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.ModDamageSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class FrayedServant extends ZombieServant{
    public FrayedServant(EntityType<? extends Summoned> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.JungleZombieServantHealth, 20.0D))
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.JungleZombieServantDamage, 20.0D))
                .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.JungleZombieServantArmor, 20.0D));
    }

    public void setConfigurableAttributes(){
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.JungleZombieServantHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.JungleZombieServantArmor, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.JungleZombieServantDamage, 20.0D));
    }

    protected float getDamageAfterMagicAbsorb(DamageSource p_34149_, float p_34150_) {
        p_34150_ = super.getDamageAfterMagicAbsorb(p_34149_, p_34150_);
        if (p_34149_.getEntity() == this) {
            p_34150_ = 0.0F;
        }

        if (ModDamageSource.shockAttacks(p_34149_) || p_34149_.is(DamageTypeTags.IS_LIGHTNING)) {
            p_34150_ *= 0.15F;
        }

        return p_34150_;
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && pEntity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(GoetyEffects.SPASMS, MathHelper.secondsToTicks(5)), this);
        }

        return flag;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance pPotioneffect) {
        return super.canBeAffected(pPotioneffect)
                && pPotioneffect.getEffect() != GoetyEffects.SPASMS.get();
    }
}