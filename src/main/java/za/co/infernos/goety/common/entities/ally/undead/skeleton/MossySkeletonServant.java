package za.co.infernos.goety.common.entities.ally.undead.skeleton;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.effects.GoetyEffects;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.Holder;

public class MossySkeletonServant extends AbstractSkeletonServant {
    public MossySkeletonServant(EntityType<? extends AbstractSkeletonServant> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MossySkeletonServantHealth, 20.0D))
                .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MossySkeletonServantArmor, 20.0D))
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MossySkeletonServantDamage, 20.0D));
    }

    public void setConfigurableAttributes(){
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MossySkeletonServantHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MossySkeletonServantArmor, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MossySkeletonServantDamage, 20.0D));
    }

    public double getBaseRangeDamage(){
        return za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MossySkeletonServantRangeDamage, 20.0D);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.MOSSY_SKELETON_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.MOSSY_SKELETON_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.MOSSY_SKELETON_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSounds.MOSSY_SKELETON_STEP.get();
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

    @Override
    public SoundEvent getShootSound() {
        return ModSounds.MOSSY_SKELETON_SHOOT.get();
    }

    protected AbstractArrow getMobArrow(ItemStack pArrowStack, float pDistanceFactor) {
        AbstractArrow abstractarrowentity = super.getMobArrow(pArrowStack, pDistanceFactor);
        if (abstractarrowentity instanceof Arrow arrow) {
            int amplifier = this.isUpgraded() ? 1 : 0;
            Holder<MobEffect> mobEffect = MobEffects.POISON;
            if (CuriosFinder.hasWildRobe(this.getTrueOwner())){
                mobEffect = GoetyEffects.ACID_VENOM;
            }
            arrow.addEffect(new MobEffectInstance(mobEffect, MathHelper.secondsToTicks(3), amplifier));
        }

        return abstractarrowentity;
    }
}
