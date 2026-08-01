package za.co.infernos.goety.common.entities.hostile.servants;

import za.co.infernos.goety.utils.MobType;

import za.co.infernos.goety.common.entities.ai.BackawayCrossbowGoal;
import za.co.infernos.goety.common.entities.ai.CreatureBowAttackGoal;
import za.co.infernos.goety.common.entities.neutral.Owned;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class SkeletonVillagerServant extends Owned implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData
            .defineId(SkeletonVillagerServant.class, EntityDataSerializers.BOOLEAN);
    private final CreatureBowAttackGoal<SkeletonVillagerServant> bowGoal = new CreatureBowAttackGoal<>(this, 1.0D, 20,
            15.0F);
    private final BackawayCrossbowGoal<SkeletonVillagerServant> crossBowGoal = new BackawayCrossbowGoal<>(this, 1.0D,
            16.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {

        public void stop() {
            super.stop();
            SkeletonVillagerServant.this.setAggressive(false);
        }

        public void start() {
            super.start();
            SkeletonVillagerServant.this.setAggressive(true);
        }
    };

    public SkeletonVillagerServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
        this.reassessWeaponGoal();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(SkeletonVillagerServant.class));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonVillagerServantHealth, 20.0D))
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonVillagerServantDamage, 20.0D))
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH),
                za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonVillagerServantHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE),
                za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonVillagerServantDamage, 20.0D));
    }

    public void reassessWeaponGoal() {
        if (this.level() != null && !this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(
                    ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof ProjectileWeaponItem));
            if (itemstack.getItem() == Items.BOW) {
                int i = 20;

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else if (itemstack.getItem() == Items.CROSSBOW) {
                this.goalSelector.addGoal(4, this.crossBowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }

        }
    }

    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        super.setItemSlot(pSlot, pStack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }

    }

    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHARGING_STATE, false);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessWeaponGoal();
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag) {
            float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                entityIn.igniteForSeconds(2 * (int) f);
            }
        }

        return flag;
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

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        if (this.level().random.nextBoolean()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
            MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
        RandomSource randomsource = pLevel.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        this.populateDefaultEquipmentEnchantments(pLevel, randomsource, pDifficulty);
        this.reassessWeaponGoal();
        for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
            this.setDropChance(equipmentslottype, 0.0F);
        }

        return pSpawnData;
    }

    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        ItemStack itemstack = this.getProjectile(
                this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrow abstractarrowentity = this.getArrow(itemstack, pDistanceFactor,
                this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        if (this.getMainHandItem().getItem() instanceof BowItem bow)
            abstractarrowentity = bow.customArrow(abstractarrowentity, this.getMainHandItem(), itemstack);
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        abstractarrowentity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F,
                (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrowentity);
    }

    protected AbstractArrow getArrow(ItemStack pArrowStack, float pDistanceFactor, ItemStack weapon) {
        return ProjectileUtil.getMobArrow(this, pArrowStack, pDistanceFactor, weapon);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_230280_1_) {
        return p_230280_1_ == Items.BOW || p_230280_1_ == Items.CROSSBOW;
    }

    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ProjectileWeaponItem weaponItem) {
            Predicate<ItemStack> predicate = weaponItem.getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 1.74F;
    }

    @Override
    public boolean isChargingCrossbow() {
        return this.entityData.get(DATA_CHARGING_STATE);
    }

    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(DATA_CHARGING_STATE, isCharging);
    }

    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, Projectile p_230284_3_,
            float p_230284_4_) {
        double d0 = p_230284_1_.getX() - this.getX();
        double d1 = p_230284_1_.getZ() - this.getZ();
        double d2 = Math.sqrt(d0 * d0 + d1 * d1);
        double d3 = p_230284_1_.getY(0.3333333333333333D) - p_230284_3_.getY() + d2 * (double) 0.2F;
        org.joml.Vector3f vector3f = this.getProjectileShotVector(p_230284_1_,
                new net.minecraft.world.phys.Vec3(d0, d3, d1), p_230284_4_);
        p_230284_3_.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), 1.6F,
                (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public org.joml.Vector3f getProjectileShotVector(LivingEntity p_230284_1_, net.minecraft.world.phys.Vec3 p_230284_2_, float p_230284_3_) {
        org.joml.Vector3f vector3f = new org.joml.Vector3f((float)p_230284_2_.x, (float)p_230284_2_.y, (float)p_230284_2_.z);
        vector3f.normalize();
        org.joml.Vector3f vector3f1 = new org.joml.Vector3f(vector3f);
        vector3f1.cross(new org.joml.Vector3f(0.0F, 1.0F, 0.0F));
        if (vector3f1.lengthSquared() <= 1.0E-7F) {
            vector3f1.set(0.0F, 0.0F, 1.0F);
        }

        vector3f1.normalize();
        org.joml.Vector3f vector3f2 = new org.joml.Vector3f(vector3f);
        vector3f2.cross(vector3f1);
        vector3f2.normalize();
        vector3f2.mul(p_230284_3_);
        vector3f1.mul(p_230284_3_);
        vector3f.add(vector3f1.mul(this.getRandom().nextFloat() * 2.0F - 1.0F));
        vector3f.add(vector3f2.mul(this.getRandom().nextFloat() * 2.0F - 1.0F));
        return vector3f;
    }
}
