package za.co.infernos.goety.common.entities.ally.undead.zombie;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.ally.Summoned;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.core.Holder;

public class JungleZombieServant extends ZombieServant{
    public JungleZombieServant(EntityType<? extends Summoned> type, Level worldIn) {
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

    protected SoundEvent getAmbientSound() {
        return ModSounds.JUNGLE_ZOMBIE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.JUNGLE_ZOMBIE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.JUNGLE_ZOMBIE_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSounds.JUNGLE_ZOMBIE_STEP.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide){
            if (this.tickCount % 5 == 0 && this.level().random.nextBoolean()) {
                double[] colors = MathHelper.rgbParticle(2735172);
                this.level().addParticle(ModParticleTypes.BIG_CULT_SPELL.get(), this.getX(), this.getY() + 1.0D, this.getZ(),
                        colors[0],
                        colors[1],
                        colors[2]);
            }
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && pEntity instanceof LivingEntity livingEntity) {
            Holder<MobEffect> mobEffect = MobEffects.POISON;
            if (CuriosFinder.hasWildRobe(this.getTrueOwner())){
                mobEffect = GoetyEffects.ACID_VENOM;
            }
            livingEntity.addEffect(new MobEffectInstance(mobEffect, MathHelper.secondsToTicks(5)), this);
        }

        return flag;
    }
}
