package za.co.infernos.goety.mixin;

import za.co.infernos.goety.api.entities.IAutoRideable;
import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.LichdomHelper;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.NoKnockBackDamageSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean hasEffect(net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> p_21024_);

    @Shadow public abstract float getMaxHealth();

    @Shadow public abstract boolean wasExperienceConsumed();

    @Shadow @Nullable private LivingEntity lastHurtByMob;

    @Shadow public abstract int getExperienceReward(ServerLevel serverLevel, @Nullable Entity attacker);

    @Shadow protected int lastHurtByPlayerTime;

    @Shadow protected abstract boolean isAlwaysExperienceDropper();

    protected LivingEntityMixin(EntityType<? extends Entity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "dropExperience", at = @At("HEAD"))
    public void dropExperience(CallbackInfo callbackInfo) {
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.lastHurtByPlayerTime <= 0 && !this.isAlwaysExperienceDropper()) {
                if (this.lastHurtByMob instanceof IOwned owned && !this.wasExperienceConsumed() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    if (owned.getMasterOwner() instanceof Player) {
                        int reward = this.getExperienceReward(serverLevel, this.lastHurtByMob);
                        ExperienceOrb.award(serverLevel, this.position(), reward);
                    }
                }
            }
        }
    }

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    public void canAttack(LivingEntity target, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichUndeadFriends, false)) {
            if (this.getType().is(ModTags.EntityTypes.LICH_NEUTRAL)) {
                if (LichdomHelper.isLich(target)) {
                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichPowerfulFoes, false)) {
                        if (this.getMaxHealth() <= za.co.infernos.goety.utils.ConfigHelper.getDouble(MainConfig.LichPowerfulFoesHealth, 1.0D)){
                            callbackInfoReturnable.setReturnValue(false);
                        }
                    } else {
                        callbackInfoReturnable.setReturnValue(false);
                    }
                }
            }
        }
    }

    @Inject(method = "isSensitiveToWater", at = @At("HEAD"), cancellable = true)
    public void isSensitiveToWater(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (this.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.SNOW_SKIN.get()))) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "randomTeleport", at = @At("HEAD"), cancellable = true)
    public void randomTeleport(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (this.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.ENDER_GROUND.get()))) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    public void jumpFromGround(CallbackInfo callbackInfo) {
        if (this.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.STUNNED.get())) || this.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.TANGLED.get()))) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "travelRidden", at = @At("HEAD"), cancellable = true)
    public void travelRidden(Player player, Vec3 vec3, CallbackInfo callbackInfo) {
        if (this instanceof IAutoRideable rideable && rideable.isAutonomous()){
            callbackInfo.cancel();
        }
    }

    @Inject(method = "updateInvisibilityStatus", at = @At(value = "TAIL"))
    public void updateInvisibilityStatus(CallbackInfo callbackInfo) {
        if (this.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.SHADOW_WALK.get()))) {
            this.setInvisible(true);
        }
    }

    @Inject(method = "isDamageSourceBlocked", at = @At("HEAD"), cancellable = true)
    public void isDamageSourceBlocked(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (damageSource instanceof NoKnockBackDamageSource damageSource1){
            if (!damageSource1.is(DamageTypeTags.BYPASSES_SHIELD) && livingEntity.isBlocking()) {
                Vec3 vec32 = damageSource1.getSourcePosition();
                if (vec32 != null) {
                    Vec3 vec3 = livingEntity.getViewVector(1.0F);
                    Vec3 vec31 = vec32.vectorTo(livingEntity.position()).normalize();
                    vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
                    if (vec31.dot(vec3) < 0.0D) {
                        callbackInfoReturnable.setReturnValue(true);
                    }
                }
            }
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    public void addEffect(MobEffectInstance instance, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity != null) {
            if (entity instanceof IOwned) {
                if (!za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.ServantsHarmEffectApply, false)) {
                    if (instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                        if (MobUtil.areAllies(this, entity)) {
                            cir.setReturnValue(false);
                        }
                    }
                }
            }
        }
    }
}