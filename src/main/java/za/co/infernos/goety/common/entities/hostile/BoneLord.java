package za.co.infernos.goety.common.entities.hostile;

import za.co.infernos.goety.api.entities.ICustomAttributes;
import za.co.infernos.goety.common.entities.projectiles.HauntedSkullProjectile;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.EntityFinder;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BoneLord extends AbstractSkeleton implements ICustomAttributes {
    private static final EntityDataAccessor<Optional<UUID>> SKULL_LORD = SynchedEntityData.defineId(BoneLord.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> SKULL_LORD_CLIENT_ID = SynchedEntityData.defineId(BoneLord.class, EntityDataSerializers.INT);

    public BoneLord(EntityType<? extends AbstractSkeleton> type, Level p_i48555_2_) {
        super(type, p_i48555_2_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new FollowHeadGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.BoneLordHealth, 20.0D))
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.BoneLordDamage, 20.0D))
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    public void setConfigurableAttributes(){
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.BoneLordHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.BoneLordDamage, 20.0D));
    }

    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        if (pDifficulty.isHarderThan(Difficulty.EASY.ordinal())){
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.FROZEN_BLADE.get()));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        }
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.CURSED_PALADIN_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.CURSED_PALADIN_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.CURSED_PALADIN_BOOTS.get()));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.CURSED_PALADIN_HELMET.get()));
    }

    protected void populateDefaultEquipmentEnchantments(DifficultyInstance pDifficulty) {
        if (pDifficulty.getDifficulty() != Difficulty.PEACEFUL && pDifficulty.getDifficulty() != Difficulty.EASY) {
            for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
                if (equipmentslottype.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    ItemStack itemstack = this.getItemBySlot(equipmentslottype);
                    if (!itemstack.isEmpty()) {
                        net.minecraft.core.Holder<Enchantment> protection = this.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(Enchantments.PROTECTION);
                        net.minecraft.world.item.enchantment.ItemEnchantments.Mutable mutable = new net.minecraft.world.item.enchantment.ItemEnchantments.Mutable(itemstack.getEnchantments());
                        switch (pDifficulty.getDifficulty()) {
                            case NORMAL:
                                if (mutable.getLevel(protection) == 0) mutable.set(protection, 2);
                                break;
                            case HARD:
                                if (mutable.getLevel(protection) == 0) mutable.set(protection, 3);
                                break;
                        }
                        EnchantmentHelper.setEnchantments(itemstack, mutable.toImmutable());
                        this.setItemSlot(equipmentslottype, itemstack);
                    }
                }
            }
            if (pDifficulty.getDifficulty() == Difficulty.HARD){
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
                if (!itemstack.isEmpty()){
                    this.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(this.random, this.getMainHandItem(), 30, this.registryAccess(), java.util.Optional.empty()));
                }
            }
        }
    }

    public boolean causeFallDamage(float p_148711_, float p_148712_, DamageSource p_148713_) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.getSkullLord() == null || this.getSkullLord().isDeadOrDying()) {
                if (this.tickCount % 100 == 0 && this.tickCount > 100) {
                    this.discard();
                }
            } else {
                AttributeInstance knockResist = this.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
                if (this.getSkullLord().isHalfHealth()) {
                    if (knockResist != null) {
                        knockResist.setBaseValue(1.0D);
                    }
                } else {
                    if (knockResist != null) {
                        if (knockResist.getBaseValue() > 0.0D) {
                            knockResist.setBaseValue(0.0D);
                        }
                    }
                }
                if (this.isInWall()) {
                    this.moveTo(this.getSkullLord().position());
                }
                if (this.getSkullLord().getTarget() != null) {
                    this.setTarget(this.getSkullLord().getTarget());
                }
            }
        }
    }

    protected boolean isSunBurnTick() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() == this.getSkullLord() && pSource.getDirectEntity() instanceof HauntedSkullProjectile){
            return false;
        } else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        this.setCanPickUpLoot(false);
        for(EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
            this.setDropChance(equipmentslottype, 0.0F);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof Monster && entityIn.getType().is(net.minecraft.tags.EntityTypeTags.UNDEAD)) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    public void makeStuckInBlock(BlockState pState, Vec3 pMotionMultiplier) {
        if (!pState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }

    }

    public boolean ignoreExplosion() {
        return true;
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKULL_LORD, Optional.empty());
        builder.define(SKULL_LORD_CLIENT_ID, -1);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.hasUUID("skullLord")) {
            this.setSkullLordUUID(pCompound.getUUID("skullLord"));
        }
        if (pCompound.contains("SkullLordClient")){
            this.setSkullLordClientId(pCompound.getInt("SkullLordClient"));
        }
        this.setConfigurableAttributes();
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.getSkullLordUUID() != null) {
            pCompound.putUUID("skullLord", this.getSkullLordUUID());
        }
        if (this.getSkullLordClientId() > -1) {
            pCompound.putInt("SkullLordClient", this.getSkullLordClientId());
        }
    }

    @Nullable
    public SkullLord getSkullLord() {
        if (!this.level().isClientSide){
            UUID uuid = this.getSkullLordUUID();
            return EntityFinder.getLivingEntityByUuiD(uuid) instanceof SkullLord skullLord ? skullLord : null;
        } else {
            int id = this.getSkullLordClientId();
            return id <= -1 ? null : this.level().getEntity(id) instanceof SkullLord skullLord ? skullLord : null;
        }
    }

    @Nullable
    public UUID getSkullLordUUID() {
        return this.entityData.get(SKULL_LORD).orElse(null);
    }

    public void setSkullLordUUID(UUID uuid){
        this.entityData.set(SKULL_LORD, Optional.ofNullable(uuid));
    }

    public int getSkullLordClientId(){
        return this.entityData.get(SKULL_LORD_CLIENT_ID);
    }

    public void setSkullLordClientId(int id){
        this.entityData.set(SKULL_LORD_CLIENT_ID, id);
    }

    public void setSkullLord(SkullLord skullLord){
        this.setSkullLordUUID(skullLord.getUUID());
        this.setSkullLordClientId(skullLord.getId());
    }

    @Override
    public void die(DamageSource p_21014_) {
        if (this.level() instanceof ServerLevel){
            if (this.getSkullLord() != null){
                if (p_21014_.getEntity() instanceof Mob mob && mob.getTarget() == this){
                    mob.setTarget(this.getSkullLord());
                }
            }
        }
        super.die(p_21014_);
    }

    public static class FollowHeadGoal extends Goal {
        private final BoneLord boneLord;
        private LivingEntity owner;
        private final PathNavigation navigation;
        private int timeToRecalcPath;
        private float oldWaterCost;

        public FollowHeadGoal(BoneLord boneLord) {
            this.boneLord = boneLord;
            this.navigation = boneLord.getNavigation();
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.boneLord.getSkullLord();
            if (livingentity == null) {
                return false;
            } else if (this.boneLord.distanceToSqr(livingentity) < (double)(100)) {
                return false;
            } else if (this.boneLord.isAggressive()) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else if (this.boneLord.isAggressive()){
                return false;
            } else {
                return !(this.boneLord.distanceToSqr(this.owner) <= (double)(4));
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.boneLord.getPathfindingMalus(PathType.WATER);
            this.boneLord.setPathfindingMalus(PathType.WATER, 0.0F);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.boneLord.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
        }

        public void tick() {
            this.boneLord.getLookControl().setLookAt(this.owner, 10.0F, (float)this.boneLord.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.boneLord.isPassenger()) {
                    this.navigation.moveTo(this.owner, 1.0F);
                }
            }
        }

    }
}
