package za.co.infernos.goety.common.entities.neutral;

import za.co.infernos.goety.utils.MobType;

import za.co.infernos.goety.common.entities.ai.CreatureCrossbowAttackGoal;
import za.co.infernos.goety.common.entities.ally.undead.skeleton.AbstractSkeletonServant;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Map;

public class AbstractSkeletonPillager extends AbstractSkeletonServant implements CrossbowAttackMob {
    private final CreatureCrossbowAttackGoal<AbstractSkeletonPillager> crossbowAttackGoal = new CreatureCrossbowAttackGoal<>(this, 1.0D, 8.0F);
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(AbstractSkeletonPillager.class, EntityDataSerializers.BOOLEAN);

    public AbstractSkeletonPillager(EntityType<? extends AbstractSkeletonServant> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonPillagerHealth, 20.0D))
                .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonPillagerArmor, 20.0D))
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonPillagerDamage, 20.0D))
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    public void setConfigurableAttributes(){
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonPillagerHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonPillagerArmor, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonPillagerDamage, 20.0D));
    }

    @Override
    public double getBaseRangeDamage() {
        return za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonPillagerRangeDamage, 20.0D);
    }

    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.crossbowAttackGoal);
            ItemStack itemstack = this.getMainHandItem();
            ItemStack itemstack2 = this.getOffhandItem();
            if (itemstack.getItem() instanceof CrossbowItem) {
                this.goalSelector.addGoal(3, this.crossbowAttackGoal);
            } else {
                this.goalSelector.addGoal(3, this.meleeGoal);
            }

        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CHARGING_CROSSBOW, false);
    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_33280_) {
        return p_33280_ == Items.CROSSBOW;
    }

    @Override
    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean p_33302_) {
        this.entityData.set(IS_CHARGING_CROSSBOW, p_33302_);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219059_, DifficultyInstance p_219060_) {
        if (this.canSpawnArmor()){
            super.populateDefaultEquipmentSlots(p_219059_, p_219060_);
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    protected void enchantSpawnedWeapon(ServerLevelAccessor p_219056_, RandomSource p_219057_, DifficultyInstance p_219058_) {
        super.enchantSpawnedWeapon(p_219056_, p_219057_, p_219058_);
        if (p_219056_.getRandom().nextInt(300) == 0) {
            ItemStack itemstack = this.getMainHandItem();
            if (itemstack.is(Items.CROSSBOW)) {
                // Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack);
                // map.putIfAbsent(Enchantments.PIERCING, 1);
                // EnchantmentHelper.setEnchantments(map, itemstack);
                this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
            }
        }

    }

    public void performRangedAttack(LivingEntity p_33272_, float p_33273_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    public void shootCrossbowProjectile(LivingEntity p_33275_, ItemStack p_33276_, Projectile p_33277_, float p_33278_) {
        if (p_33277_ instanceof AbstractArrow arrow){
            arrow.setBaseDamage(arrow.getBaseDamage() + this.getArrowPower() + this.getBaseRangeDamage());
        }
        this.performCrossbowAttack(this, 1.6F); // Replaced self-call with performCrossbowAttack
    }
}
