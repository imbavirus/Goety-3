package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.client.particles.CircleExplodeParticleOption;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.client.particles.VerticalCircleExplodeParticleOption;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.*;
import net.minecraft.world.effect.MobEffectInstance;
import za.co.infernos.goety.common.effects.GoetyEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BioMine extends SpellEntity {
    public float extraRadius = 0.0F;
    public int extraDuration = 0;
    public int lifeTicks = MathHelper.secondsToTicks(5);

    public BioMine(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("LifeTicks")){
            this.lifeTicks = pCompound.getInt("LifeTicks");
        }
        if (pCompound.contains("Duration")){
            this.extraDuration = pCompound.getInt("Duration");
        }
        if (pCompound.contains("CurrentTicks")){
            this.tickCount = pCompound.getInt("CurrentTicks");
        }
        if (pCompound.contains("ExtraRadius")) {
            this.setExtraRadius(pCompound.getFloat("ExtraRadius"));
        }
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("LifeTicks", this.lifeTicks);
        pCompound.putInt("Duration", this.extraDuration);
        pCompound.putInt("CurrentTicks", this.tickCount);
        pCompound.putFloat("ExtraRadius", this.getExtraRadius());
    }

    public float getExtraRadius(){
        return this.extraRadius;
    }

    public void setExtraRadius(float radius){
        this.extraRadius = radius;
    }

    public int getLifeTicks() {
        return this.lifeTicks;
    }

    public void setLifeTicks(int lifeTicks) {
        this.lifeTicks = lifeTicks;
    }

    public int getExtraDuration() {
        return this.extraDuration;
    }

    public void setExtraDuration(int extraDuration) {
        this.extraDuration = extraDuration;
    }

    public @NotNull Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    public void setDeltaMovement(@NotNull Vec3 p_149804_) {
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        --this.lifeTicks;
        if (!this.level().isClientSide) {
            float bbSize = this.getBbWidth();
            if (this.lifeTicks <= 0 && this.level().getRandom().nextInt(8) == 0){
                if (this.getOwner() != null && this.getOwner() instanceof LivingEntity livingEntity){
                    livingEntity.heal(1.0F);
                }
            }

            if (this.lifeTicks > 0){
                --this.lifeTicks;
            } else {
                for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(bbSize, bbSize / 2.0D, bbSize))) {

                    if (livingentity.isAffectedByPotions()) {
                        if (livingentity != this.getOwner() && !MobUtil.areAllies(livingentity, this.getOwner())) {
                            this.detonate();
                        }
                    }
                }
            }
        }
    }

    public void detonate() {
        if (!this.level().isClientSide) {
            double bbSize = 3.0D + this.getExtraRadius();
            float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.BiomineAcidDamage, 1.0F) * WandUtil.damageMultiply();
            damage += this.getExtraDamage();
            for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(bbSize, bbSize / 2.0D, bbSize))) {
                if (livingentity.isAlive() && !livingentity.isInvulnerable()) {
                    if (this.getOwner() != null) {
                        if (!MobUtil.areAllies(this.getOwner(), livingentity) && livingentity != this.getOwner()) {
                            livingentity.hurt(ModDamageSource.acid(this, this.getOwner()), 0.0F);
                            livingentity.addEffect(new MobEffectInstance(GoetyEffects.ACID_VENOM, 100, 1), this);
                        }
                    } else {
                        livingentity.hurt(ModDamageSource.acid(this, this.getOwner()), 0.0F);
                        livingentity.addEffect(new MobEffectInstance(GoetyEffects.ACID_VENOM, 100, 1), this);
                    }
                }
            }
            this.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE.value(), 1.0F, 1.0F);
            AcidPool acidPool = new AcidPool(ModEntityType.ACID_POOL.get(), this.level());
            acidPool.setPos(this.position());
            acidPool.setRadius(2.0F + this.getExtraRadius());
            acidPool.setDamage(damage);
            acidPool.setColor(0x20b33e);
            ColorUtil colorUtil0 = new ColorUtil(0x20b33e);
            acidPool.setParticle(ModParticleTypes.BIG_CULT_SPELL.get());
            acidPool.setParticleSpeed(colorUtil0.red(), colorUtil0.green(), colorUtil0.blue());
            acidPool.setDuration(MathHelper.secondsToTicks(3) + this.getExtraDuration());
            if (this.getOwner() != null) {
                acidPool.setOwner(this.getOwner());
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                ColorUtil colorUtil = ColorUtil.WHITE;
                serverLevel.sendParticles(new VerticalCircleExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, (float) bbSize, 1), this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                serverLevel.sendParticles(new CircleExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, (float) bbSize, 1), this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
            this.playSound(ModSounds.BIOMINE_TRIGGER.get(), 1.4F, 1.0F);
            this.discard();
        }
    }

    public void explodeDamage(LivingEntity livingEntity) {
        if (!this.level().isClientSide) {
            float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.BiomineDamage, 1.0F) * WandUtil.damageMultiply();
            damage += this.getExtraDamage();
            if (this.getOwner() != null){
                livingEntity.hurt(ModDamageSource.acid(this, this.getOwner()), damage);
            } else {
                livingEntity.hurt(livingEntity.damageSources().magic(), damage);
            }
        }
    }

    public boolean hurt(DamageSource p_31050_, float p_31051_) {
        if (this.isInvulnerableTo(p_31050_)) {
            return false;
        } else if (MobUtil.areAllies(this, p_31050_.getEntity())) {
            return false;
        } else {
            if (!this.isRemoved() && !this.level().isClientSide) {
                this.detonate();
                this.remove(Entity.RemovalReason.KILLED);
            }

            return true;
        }
    }
}
