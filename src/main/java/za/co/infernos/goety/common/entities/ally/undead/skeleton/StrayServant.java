package za.co.infernos.goety.common.entities.ally.undead.skeleton;

import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
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

public class StrayServant extends AbstractSkeletonServant {

    public StrayServant(EntityType<? extends AbstractSkeletonServant> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.StrayServantHealth, 20.0D))
                .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.StrayServantArmor, 20.0D))
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.StrayServantDamage, 20.0D));
    }

    public void setConfigurableAttributes(){
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.StrayServantHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.StrayServantArmor, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.StrayServantDamage, 20.0D));
    }

    public double getBaseRangeDamage(){
        return za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.StrayServantRangeDamage, 20.0D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.STRAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.STRAY_STEP;
    }

    protected AbstractArrow getMobArrow(ItemStack pArrowStack, float pDistanceFactor) {
        AbstractArrow abstractarrowentity = super.getMobArrow(pArrowStack, pDistanceFactor);
        if (abstractarrowentity instanceof Arrow) {
            int amplifier = this.isUpgraded() ? 1 : 0;
            ((Arrow)abstractarrowentity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, amplifier));
        }

        return abstractarrowentity;
    }
}