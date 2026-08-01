package za.co.infernos.goety.common.entities.neutral;

import za.co.infernos.goety.utils.MobType;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.Summoned;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.ServerParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CarrionFly extends Summoned {
    private static final EntityDataAccessor<Boolean> NECRO = SynchedEntityData.defineId(CarrionFly.class, EntityDataSerializers.BOOLEAN);

    public CarrionFly(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
        this.setNoGravity(true);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.5D, false));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D, 10, false));
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    public void followGoal(){
        this.goalSelector.addGoal(8, new FollowOwnerGoal<>(this, 1.0D, 20.0F, 2.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyHealth, 20.0D))
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyArmor, 20.0D))
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyDamage, 20.0D));
    }

    @Override
    public void setConfigurableAttributes(){
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyArmor, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyDamage, 20.0D));
    }

    public float getWalkTargetValue(@NotNull BlockPos p_27788_, LevelReader p_27789_) {
        return p_27789_.getBlockState(p_27788_).isAir() ? 10.0F : 0.0F;
    }

    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel) {
            public boolean isStableDestination(BlockPos blockPos) {
                return !this.level.getBlockState(blockPos.below()).isAir();
            }

            public void tick() {
                super.tick();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(NECRO, false);
    }

    public void setNecro(boolean necro) {
        this.entityData.set(NECRO, necro);
    }

    public boolean isNecro() {
        return this.entityData.get(NECRO);
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, @NotNull DamageSource damageSource) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource p_32615_) {
        return ModSounds.FLY_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.FLY_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        if (pReason == MobSpawnType.STRUCTURE){
            if (pLevel.getRandom().nextFloat() <= 0.15F){
                this.setNecro(true);
            }
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    public boolean isNoGravity() {
        return true;
    }

    private void jumpInLiquidInternal() {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.isNecro()) {
                if (this.level().random.nextInt(12) == 0) {
                    this.level().addParticle(ModParticleTypes.NECRO_EFFECT.get(), this.getRandomX(0.2D), this.getRandomY(), this.getRandomZ(0.2D), 0.0D, 0.0D, 0.0D);
                }
            }
        } else {
            if (this.getTrueOwner() != null){
                if (this.getTrueOwner() instanceof Player player){
                    if (!player.isCreative() && !player.isSpectator()){
                        this.level().broadcastEntityEvent(this, (byte) 20);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 21);
                    }
                }
            }
        }
    }

    @Override
    public void lifeSpanDamage() {
        if (!this.level().isClientSide){
            for(int i = 0; i < this.level().random.nextInt(10) + 10; ++i) {
                ServerParticleUtil.smokeParticles(ParticleTypes.SMOKE, this.getX(), this.getEyeY(), this.getZ(), this.level());
            }
        }
        this.discard();
    }

    public void setYBodyRot(float p_32621_) {
        this.setYRot(p_32621_);
        super.setYBodyRot(p_32621_);
    }

    @Override
    public void setUpgraded(boolean upgraded) {
        super.setUpgraded(upgraded);
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (health != null && attack != null) {
            if (upgraded) {
                health.setBaseValue(za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyHealth, 20.0D) * 1.33D);
                attack.setBaseValue(za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyDamage, 20.0D) * 1.1D);
            } else {
                health.setBaseValue(za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyHealth, 20.0D));
                attack.setBaseValue(za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CarrionFlyDamage, 20.0D));
            }
        }
        this.setHealth(this.getMaxHealth());
    }

    @Override
    public void uncreditedKill(LivingEntity target) {
        if (!MobUtil.areAllies(this, target)) {
            int total = this.level().getEntitiesOfClass(CarrionMaggot.class, this.getBoundingBox().inflate(16.0F), maggot -> MobUtil.areAllies(this, maggot)).size();
            if (total < za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CarrionLimit, 16)) {
                int random = this.getRandom().nextIntBetweenInclusive(1, 3);
                if (target.getMaxHealth() < 20.0F) {
                    random = 1;
                }
                for (int i = 0; i < random; ++i) {
                    CarrionMaggot carrionMaggot = new CarrionMaggot(ModEntityType.CARRION_MAGGOT.get(), this.level());
                    carrionMaggot.setTrueOwner(this.getTrueOwner() != null ? this.getTrueOwner() : this);
                    BlockPos blockPos = BlockFinder.SummonRadius(target.blockPosition(), carrionMaggot, this.level(), 3);
                    carrionMaggot.moveTo(blockPos.getCenter());
                    if (this.level() instanceof ServerLevel serverLevel) {
                        carrionMaggot.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(carrionMaggot.blockPosition()), MobSpawnType.BREEDING, null);
                    }
                    carrionMaggot.setNatural(this.isNatural());
                    carrionMaggot.setHostile(this.isHostile());
                    carrionMaggot.setUpgraded(this.isUpgraded());
                    if (this.isLimitedLife()) {
                        carrionMaggot.setLimitedLife(this.getLifespan());
                    }
                    this.level().addFreshEntity(carrionMaggot);
                }
            }
        }
    }

    @Override
    public boolean doHurtTarget(float amount, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            if (livingTarget.getType().is(net.minecraft.tags.EntityTypeTags.UNDEAD)) {
                amount *= 2;
            }
        }
        return super.doHurtTarget(amount, target);
    }

    public boolean isFood(ItemStack p_30440_) {
        return p_30440_.has(DataComponents.FOOD);
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                FoodProperties foodProperties = itemstack.get(DataComponents.FOOD);
                if (foodProperties != null) {
                    this.heal((float) foodProperties.nutrition());
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    this.gameEvent(GameEvent.EAT, this);
                    this.eat(this.level(), itemstack);
                    if (this.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            serverLevel.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                        }
                    }
                    pPlayer.swing(pHand);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }
}