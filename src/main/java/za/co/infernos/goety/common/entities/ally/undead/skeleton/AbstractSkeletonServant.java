package za.co.infernos.goety.common.entities.ally.undead.skeleton;

import za.co.infernos.goety.api.items.magic.IWand;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ai.CreatureBowAttackGoal;
import za.co.infernos.goety.common.entities.ai.ModMeleeAttackGoal;
import za.co.infernos.goety.common.entities.ally.Summoned;
import za.co.infernos.goety.common.entities.projectiles.GhostArrow;
import za.co.infernos.goety.common.research.ResearchList;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.function.Predicate;

public abstract class AbstractSkeletonServant extends Summoned implements RangedAttackMob {
    private final CreatureBowAttackGoal<AbstractSkeletonServant> bowGoal = new CreatureBowAttackGoal<>(this, 1.0D, 20,
            15.0F);
    public final ModMeleeAttackGoal meleeGoal = new ModMeleeAttackGoal(this, 1.2D, false) {

        public void stop() {
            super.stop();
            AbstractSkeletonServant.this.setAggressive(false);
        }

        public void start() {
            super.start();
            AbstractSkeletonServant.this.setAggressive(true);
        }
    };
    public double arrowPower;

    public AbstractSkeletonServant(EntityType<? extends Summoned> type, Level worldIn) {
        super(type, worldIn);
        this.arrowPower = 0.0D;
        this.reassessWeaponGoal();
    }

    @Override
    protected boolean isSunSensitive() {
        return true;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(8, new WanderGoal<>(this, 1.0D, 10));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonServantHealth, 20.0D))
                .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonServantArmor, 20.0D))
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonServantDamage, 20.0D));
    }

    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH),
                za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonServantHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonServantArmor, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE),
                za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonServantDamage, 20.0D));
    }

    public double getBaseRangeDamage() {
        return za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.SkeletonServantRangeDamage, 20.0D);
    }

    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getMainHandItem();
            ItemStack itemstack2 = this.getOffhandItem();
            if (itemstack.getItem() instanceof BowItem || itemstack2.getItem() instanceof BowItem) {
                int i = 20;
                if (!this.isUpgraded()) {
                    i = 40;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }

        }
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.arrowPower = pCompound.getInt("arrowPower");
        this.reassessWeaponGoal();
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (pCompound.contains("arrowPower")) {
            pCompound.putDouble("arrowPower", this.arrowPower);
        }
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof AbstractSkeletonServant;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SkeletonLimit, 32);
    }

    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        super.setItemSlot(pSlot, pStack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }

    }

    public void rideTick() {
        super.rideTick();
        if (this.getVehicle() instanceof PathfinderMob) {
            PathfinderMob pathfindermob = (PathfinderMob) this.getVehicle();
            this.yBodyRot = pathfindermob.yBodyRot;
        }

    }

    protected abstract SoundEvent getStepSound();

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(randomSource, difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    public double getArrowPower() {
        return arrowPower;
    }

    public void setArrowPower(int arrowPower) {
        this.arrowPower += arrowPower;
    }

    public EntityType<?> getVariant(@Nullable Player player, Level level, BlockPos blockPos) {
        EntityType<?> entityType = ModEntityType.SKELETON_SERVANT.get();
        if (level instanceof ServerLevel serverLevel) {
            if (level.getBiome(blockPos).is(Tags.Biomes.IS_COLD_OVERWORLD) && level.canSeeSky(blockPos)) {
                entityType = ModEntityType.STRAY_SERVANT.get();
            } else if (BlockFinder.findStructure(serverLevel, blockPos, BuiltinStructures.PILLAGER_OUTPOST)) {
                entityType = ModEntityType.SKELETON_PILLAGER_SERVANT.get();
            } else if (player != null
                    && BlockFinder.findStructure(serverLevel, blockPos, ModTags.Structures.CAN_SUMMON_WITHER_SKELETONS)
                    && SEHelper.hasResearch(player, ResearchList.BYGONE)) {
                entityType = ModEntityType.WITHER_SKELETON_SERVANT.get();
            } else if (level.getBiome(blockPos).is(BiomeTags.IS_JUNGLE) && level.random.nextBoolean()) {
                entityType = ModEntityType.MOSSY_SKELETON_SERVANT.get();
            } else if (level.isWaterAt(blockPos)) {
                entityType = ModEntityType.SUNKEN_SKELETON_SERVANT.get();
            }
            if (level.getBiome(blockPos).is(Tags.Biomes.IS_SNOWY)) {
                entityType = ModEntityType.STRAY_SERVANT.get();
            }
        }
        return entityType;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn,
            MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
        this.reassessWeaponGoal();
        this.populateDefaultEquipmentSlots(worldIn.getRandom(), difficultyIn);
        this.populateDefaultEquipmentEnchantments(worldIn, worldIn.getRandom(), difficultyIn);
        if (this.isNatural()) {
            this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0D, 1.2D));
        }
        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD,
                        new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }
        return spawnDataIn;
    }

    public SoundEvent getShootSound() {
        return SoundEvents.SKELETON_SHOOT;
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.getProjectile(
                this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrow abstractarrowentity = this.getMobArrow(itemstack, distanceFactor);
        if (this.getMainHandItem().getItem() instanceof BowItem) {
            abstractarrowentity = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrowentity, itemstack, this.getMainHandItem());
            ItemHelper.hurtAndBreak(this.getMainHandItem(), 1, this);
        }
        abstractarrowentity
                .setBaseDamage(abstractarrowentity.getBaseDamage() + this.getArrowPower() + this.getBaseRangeDamage());
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        abstractarrowentity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F,
                (float) (14 - this.level().getDifficulty().getId() * 4));
        if (this.getShootSound() != null) {
            this.playSound(this.getShootSound(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        }
        this.level().addFreshEntity(abstractarrowentity);
    }

    protected AbstractArrow getMobArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrow abstractarrowentity = ProjectileUtil.getMobArrow(this, arrowStack, distanceFactor, this.getMainHandItem());
        if (this.isUpgraded()) {
            abstractarrowentity = getGhostArrow(this, arrowStack, distanceFactor);
        }

        return abstractarrowentity;
    }

    public static AbstractArrow getGhostArrow(LivingEntity living, ItemStack stack, float distanceFactor) {
        AbstractArrow arrow = new GhostArrow(living.level(), living);
        double d0 = arrow.getBaseDamage();
        var registryAccess = living.level().registryAccess();
        var enchantmentRegistry = registryAccess.registryOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
        
        var powerHolder = enchantmentRegistry.getHolder(net.minecraft.world.item.enchantment.Enchantments.POWER);
        if (powerHolder.isPresent()) {
            int i = stack.getEnchantmentLevel(powerHolder.get());
            if (i > 0) {
                arrow.setBaseDamage(d0 + (double) i * 0.5D + 0.5D);
            }
        }

        var punchHolder = enchantmentRegistry.getHolder(net.minecraft.world.item.enchantment.Enchantments.PUNCH);
        if (punchHolder.isPresent()) {
            int j = stack.getEnchantmentLevel(punchHolder.get());
            if (j > 0) {
                // Arrow.setKnockback removed in 1.21 - knockback handled differently via enchantments
                arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            }
        }

        var flameHolder = enchantmentRegistry.getHolder(net.minecraft.world.item.enchantment.Enchantments.FLAME);
        if (flameHolder.isPresent() && stack.getEnchantmentLevel(flameHolder.get()) > 0) {
            arrow.igniteForSeconds(100);
        }
        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        return arrow;
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_230280_1_) {
        return p_230280_1_ == Items.BOW;
    }

    public AbstractArrow customArrow(AbstractArrow abstractarrow, ItemStack itemstack) {
        if (abstractarrow instanceof AbstractArrow arrow && this.isUpgraded()) {
            arrow = new GhostArrow(this.level(), this);
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            return arrow;
        }
        return abstractarrow;
    }
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.74F;
    }

    public double getMyRidingOffset() {
        return -0.6D;
    }

    public boolean isShaking() {
        return this.isFullyFrozen();
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        ItemStack itemstack2 = this.getMainHandItem();
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (item == Items.BONE && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.SKELETON_STEP, 1.0F, 1.25F);
                this.heal(2.0F);
                if (this.level() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        serverLevel.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D),
                                this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                    }
                }
                return InteractionResult.SUCCESS;
            }
            if (!(pPlayer.getOffhandItem().getItem() instanceof IWand)) {
                if (item instanceof SwordItem) {
                    this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1.0F, 1.0F);
                    this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.copyWithCount(1));
                    this.dropEquipment(EquipmentSlot.MAINHAND, itemstack2);
                    this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D),
                                this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (item instanceof BowItem) {
                    this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1.0F, 1.0F);
                    this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.copyWithCount(1));
                    this.dropEquipment(EquipmentSlot.MAINHAND, itemstack2);
                    this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D),
                                this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (this instanceof CrossbowAttackMob) {
                    if (item instanceof CrossbowItem) {
                        this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1.0F, 1.0F);
                        this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.copyWithCount(1));
                        this.dropEquipment(EquipmentSlot.MAINHAND, itemstack2);
                        this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D),
                                    this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                        }
                        if (!pPlayer.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return ServantUtil.equipServantArmor(pPlayer, this, itemstack, super.mobInteract(pPlayer, pHand));
        }
        return super.mobInteract(pPlayer, pHand);
    }
}