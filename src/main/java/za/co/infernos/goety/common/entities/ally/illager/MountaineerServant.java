package za.co.infernos.goety.common.entities.ally.illager;

import za.co.infernos.goety.api.items.magic.IWand;
import za.co.infernos.goety.common.entities.ai.ModMeleeAttackGoal;
import za.co.infernos.goety.common.entities.ai.path.ModClimberNavigation;
import za.co.infernos.goety.common.entities.neutral.Owned;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.equipment.IceAxeItem;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MountaineerServant extends AbstractIllagerServant {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(MountaineerServant.class, EntityDataSerializers.BYTE);

    public MountaineerServant(EntityType<? extends Owned> type, Level worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MountaineerBreakDoorGoal(this));
        this.goalSelector.addGoal(4, new MountaineerMeleeAttackGoal(this));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MountaineerServantHealth, 20.0D))
                .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MountaineerServantArmor, 20.0D))
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MountaineerServantDamage, 20.0D));
    }

    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MountaineerServantHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MountaineerServantArmor, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.MountaineerServantDamage, 20.0D));
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte) 0);
    }

    protected PathNavigation createNavigation(Level level) {
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.MountaineerClimb, true)) {
            return new ModClimberNavigation(this, level);
        }
        return super.createNavigation(level);
    }

    @Override
    public boolean canOpenDoors() {
        return true;
    }

    public IllagerServantArmPose getArmPose() {
        if (this.isAggressive()) {
            return IllagerServantArmPose.ATTACKING;
        } else {
            return this.isCelebrating() ? IllagerServantArmPose.CELEBRATING : IllagerServantArmPose.NEUTRAL;
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @Nullable SpawnGroupData p_34091_, @Nullable CompoundTag p_34092_) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_);
        RandomSource randomsource = p_34088_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_34089_);
        this.populateDefaultEquipmentEnchantments(p_34088_, randomsource, p_34089_);
        return spawngroupdata;
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219149_, DifficultyInstance p_219150_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_ICE_AXE.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.MOUNTAINEER_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource p_34103_) {
        return ModSounds.MOUNTAINEER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.MOUNTAINEER_DEATH.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSounds.MOUNTAINEER_CELEBRATE.get();
    }

    /*@Override
    public void swing(InteractionHand pHand) {
        super.swing(pHand);
        this.playSound(ModSounds.MOUNTAINEER_ATTACK.get());
    }*/

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean p_33820_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_33820_) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public boolean isClimbableBlock(BlockPos blockPos) {
        BlockState blockState = this.level().getBlockState(blockPos);
        return (blockState.is(BlockTags.ICE)
                || blockState.is(BlockTags.BASE_STONE_OVERWORLD)
                // || blockState.is(Tags.Blocks.COBBLESTONE)
                || blockState.is(BlockTags.DIRT)
                || blockState.is(BlockTags.SNOW))
                && blockState.isSolidRender(this.level(), blockPos);
    }

    public boolean onClimbable() {
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.MountaineerClimb, true)) {
            return this.isClimbing();
        }
        return super.onClimbable();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            boolean shouldClimb = this.horizontalCollision
                    /*&& this.canClimbAtCurrentPosition()*/
                    && !this.isStuckAtCeiling()
                    && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.MountaineerClimb, true);
            this.setClimbing(shouldClimb);
        }
    }

    private boolean canClimbAtCurrentPosition() {
        BlockPos pos = this.blockPosition();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx != 0 || dz != 0) {
                    if (this.isClimbableBlock(pos.offset(dx, 0, dz))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isStuckAtCeiling() {
        BlockPos above = this.blockPosition().above(2);
        return this.level().getBlockState(above).isSolidRender(this.level(), above) && this.getDeltaMovement().y <= 0.01D;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        ItemStack itemstack2 = this.getMainHandItem();
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (!(pPlayer.getOffhandItem().getItem() instanceof IWand)) {
                if (item instanceof IceAxeItem) {
                    this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1.0F, 1.0F);
                    this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.copy());
                    this.dropEquipment(EquipmentSlot.MAINHAND, itemstack2);
                    this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    static class MountaineerBreakDoorGoal extends BreakDoorGoal {
        public MountaineerBreakDoorGoal(Mob p_34112_) {
            super(p_34112_, 6, VindicatorServant.DOOR_BREAKING_PREDICATE);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canContinueToUse() {
            MountaineerServant vindicator = (MountaineerServant)this.mob;
            return vindicator.isRaiding() && super.canContinueToUse();
        }

        public boolean canUse() {
            MountaineerServant vindicator = (MountaineerServant)this.mob;
            return vindicator.isRaiding() && vindicator.random.nextInt(reducedTickDelay(10)) == 0 && super.canUse();
        }

        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }

    static class MountaineerMeleeAttackGoal extends ModMeleeAttackGoal {
        public MountaineerMeleeAttackGoal(MountaineerServant p_34123_) {
            super(p_34123_, 1.0D, false);
        }
    }
}