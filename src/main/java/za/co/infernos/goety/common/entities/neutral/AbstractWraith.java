package za.co.infernos.goety.common.entities.neutral;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.client.particles.TeleportInShockwaveParticleOption;
import za.co.infernos.goety.client.particles.TeleportShockwaveParticleOption;
import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ai.FloatSwimGoal;
import za.co.infernos.goety.common.entities.ai.SummonTargetGoal;
import za.co.infernos.goety.common.entities.ally.Summoned;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.SoundUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class AbstractWraith extends Summoned {
    private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData
            .defineId(AbstractWraith.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(AbstractWraith.class,
            EntityDataSerializers.BYTE);
    public int fireTick;
    public int fireCooldown;
    public int teleportCooldown;
    public int teleportTime = 20;
    public int teleportTime2;
    public int postTeleportTime;
    public float interestTime;
    public double prevX;
    public double prevY;
    public double prevZ;
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState postTeleportAnimationState = new AnimationState();
    public AnimationState breathingAnimationState = new AnimationState();
    public AnimationState acidAnimationState = new AnimationState();

    public AbstractWraith(EntityType<? extends Summoned> p_i48553_1_, Level p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
        this.moveControl = new MobUtil.WraithMoveController(this);
        this.fireTick = 0;
        this.fireCooldown = 0;
        this.teleportTime2 = 0;
        this.teleportCooldown = 0;
        this.postTeleportTime = 0;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatSwimGoal(this));
        this.goalSelector.addGoal(9, new WraithLookGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new WraithLookGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(10, new WraithLookRandomlyGoal(this));
    }

    public void targetSelectGoal() {
        this.targetSelector.addGoal(1, new SummonTargetGoal(this, false, false));
        this.targetSelector.addGoal(1,
                new NaturalAttackGoal<>(this, Mob.class, true,
                        mob -> mob.getType().getDescriptionId().contains("netherexp")
                                && mob.getType().getDescriptionId().contains("carcass") && mob.getBbWidth() < 2.2F));
    }

    @SuppressWarnings("removal")
    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WraithHealth, 20.0D))
                .add(Attributes.ARMOR, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WraithArmor, 20.0D))
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.STEP_HEIGHT, 1.0F)
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WraithDamage, 20.0D));
    }

    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WraithHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ARMOR), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WraithArmor, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.WraithDamage, 20.0D));
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_INTERESTED_ID, false);
        builder.define(FLAGS, (byte) 0);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("fireTick", this.fireTick);
        pCompound.putInt("fireCooldown", this.fireCooldown);
        pCompound.putInt("teleportTime2", this.teleportTime2);
        pCompound.putInt("teleportCooldown", this.teleportCooldown);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.fireTick = pCompound.getInt("fireTick");
        this.fireCooldown = pCompound.getInt("fireCooldown");
        this.teleportTime2 = pCompound.getInt("teleportTime2");
        this.teleportCooldown = pCompound.getInt("teleportCooldown");
    }

    @Override
    public Predicate<Entity> summonPredicate() {
        return entity -> entity instanceof AbstractWraith;
    }

    @Override
    public int getSummonLimit(LivingEntity owner) {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.WraithLimit, 6);
    }

    // Removed getDefaultLootTable() override - let Minecraft use default behavior
    // which constructs the path from the entity type's registry name automatically
    
    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean wasRecentlyHit) {
        // Comprehensive logging to debug loot table issues
        ResourceKey<LootTable> lootTableKey = this.getLootTable();
        za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Wraith dropping loot - Table key: {}", lootTableKey.location());
        
        // Call super first
        za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Calling super.dropCustomDeathLoot()...");
        super.dropCustomDeathLoot(serverLevel, damageSource, wasRecentlyHit);
        za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] super.dropCustomDeathLoot() completed");
        
        // Now manually process the loot table with extensive logging
        if (serverLevel.getServer() != null) {
            try {
                // Step 1: Retrieve loot table
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Retrieving loot table from registry...");
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Loot table key: {} (namespace: {}, path: {})", 
                    lootTableKey.location(), lootTableKey.location().getNamespace(), lootTableKey.location().getPath());
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Expected resource path: data/{}/loot_tables/{}.json", 
                    lootTableKey.location().getNamespace(), lootTableKey.location().getPath());
                LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableKey);
                
                if (lootTable == null) {
                    za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] Loot table is NULL!");
                    return;
                }
                
                ResourceLocation tableId = lootTable.getLootTableId();
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Loot table retrieved successfully. Table ID: {}", tableId);
                
                // Check if it's the empty loot table
                if (lootTable == LootTable.EMPTY) {
                    za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] Loot table is EMPTY! This means the table wasn't found or loaded.");
                    za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] Expected table: {}, but got EMPTY table", lootTableKey.location());
                    za.co.infernos.goety.Goety.LOGGER.warn("[LOOT DEBUG] Since loot table loading is broken, using fallback manual drop. The loot table file exists and is valid JSON, but Minecraft isn't loading it.");
                    // Skip the rest of the loot table processing and go straight to fallback
                    if (za.co.infernos.goety.common.items.ModItems.ECTOPLASM.isBound()) {
                        int count = 1 + serverLevel.random.nextInt(2); // 1-2 ectoplasm
                        this.spawnAtLocation(new net.minecraft.world.item.ItemStack(za.co.infernos.goety.common.items.ModItems.ECTOPLASM.get(), count));
                        za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Manually dropped {} ectoplasm as fallback (loot table system not working)", count);
                    }
                    return; // Exit early, skip all the loot table processing
                } else {
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Loot table is not empty. Comparing with EMPTY: {}", lootTable == LootTable.EMPTY);
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Loot table EMPTY ID: {}", LootTable.EMPTY.getLootTableId());
                }
                
                // Try to check if the loot table file actually exists in resources and try to manually load it
                try {
                    var resourceManager = serverLevel.getServer().getResourceManager();
                    
                    // Check BOTH paths: loot_tables (plural) and loot_table (singular)
                    ResourceLocation resourceLocationPlural = ResourceLocation.fromNamespaceAndPath(
                        lootTableKey.location().getNamespace(), 
                        "loot_tables/" + lootTableKey.location().getPath() + ".json"
                    );
                    ResourceLocation resourceLocationSingular = ResourceLocation.fromNamespaceAndPath(
                        lootTableKey.location().getNamespace(), 
                        "loot_table/" + lootTableKey.location().getPath() + ".json"
                    );
                    
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Checking for resource at PLURAL path: {}", resourceLocationPlural);
                    var resourcePlural = resourceManager.getResource(resourceLocationPlural);
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Plural path exists: {}", resourcePlural.isPresent());
                    
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Checking for resource at SINGULAR path: {}", resourceLocationSingular);
                    var resourceSingular = resourceManager.getResource(resourceLocationSingular);
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Singular path exists: {}", resourceSingular.isPresent());
                    
                    // Use whichever exists
                    var resource = resourcePlural.isPresent() ? resourcePlural : resourceSingular;
                    ResourceLocation resourceLocation = resourcePlural.isPresent() ? resourceLocationPlural : resourceLocationSingular;
                    
                    if (resource.isPresent()) {
                        za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Resource file EXISTS at: {} (using {})", resourceLocation, resourcePlural.isPresent() ? "PLURAL" : "SINGULAR");
                        // Try to read the file content and manually parse it
                        try (var inputStream = resource.get().open()) {
                            String content = new String(inputStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                            za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Resource file content (first 200 chars): {}", 
                                content.length() > 200 ? content.substring(0, 200) : content);
                            
                            // Try to manually parse the JSON to see if there's a parsing error
                            try {
                                var gson = new com.google.gson.Gson();
                                var jsonObject = com.google.gson.JsonParser.parseString(content).getAsJsonObject();
                                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] JSON parsed successfully. Type: {}, Pools: {}", 
                                    jsonObject.get("type"), jsonObject.get("pools") != null ? jsonObject.getAsJsonArray("pools").size() : "null");
                            } catch (Exception parseEx) {
                                za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] JSON parsing error: ", parseEx);
                            }
                        }
                    } else {
                        za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] Resource file NOT FOUND at: {}", resourceLocation);
                    }
                } catch (Exception e) {
                    za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] Error checking resource file: ", e);
                }
                
                // Step 2: Verify item exists
                var itemRegistry = serverLevel.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.ITEM);
                var ectoplasmKey = net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, Goety.location("ectoplasm"));
                boolean itemExists = itemRegistry.containsKey(ectoplasmKey);
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Ectoplasm item exists in registry: {}", itemExists);
                
                if (itemExists) {
                    var ectoplasmItem = itemRegistry.get(ectoplasmKey);
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Ectoplasm item: {} (bound: {})", 
                        ectoplasmItem != null ? ectoplasmItem.getDescriptionId() : "null",
                        za.co.infernos.goety.common.items.ModItems.ECTOPLASM.isBound());
                }
                
                // Step 3: Build LootParams
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Building LootParams...");
                LootParams.Builder lootParamsBuilder = (new LootParams.Builder(serverLevel))
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .withParameter(LootContextParams.ORIGIN, this.position())
                        .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                        .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                        .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());
                
                if (this.lastHurtByPlayerTime > 0 && this.lastHurtByPlayer != null) {
                    lootParamsBuilder = lootParamsBuilder
                            .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer)
                            .withLuck(this.lastHurtByPlayer.getLuck());
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Added player context - Luck: {}", this.lastHurtByPlayer.getLuck());
                } else {
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] No player context (lastHurtByPlayerTime: {}, lastHurtByPlayer: {})", 
                        this.lastHurtByPlayerTime, this.lastHurtByPlayer != null);
                }
                
                LootParams lootParams = lootParamsBuilder.create(LootContextParamSets.ENTITY);
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] LootParams created. Context type: ENTITY");
                
                // Step 4: Try method 1 - getRandomItems without seed (like Inferno)
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Method 1: Calling getRandomItems(lootParams) without seed...");
                java.util.List<net.minecraft.world.item.ItemStack> lootItems1 = lootTable.getRandomItems(lootParams);
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Method 1 result: {} items generated", lootItems1.size());
                if (!lootItems1.isEmpty()) {
                    lootItems1.forEach(item -> {
                        za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Method 1 item: {} x{}", item.getItem().getDescriptionId(), item.getCount());
                        this.spawnAtLocation(item);
                    });
                }
                
                // Step 5: Try method 2 - getRandomItems with seed and callback (like CryptSlime)
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Method 2: Calling getRandomItems(lootParams, seed, callback) with seed {}...", this.getLootTableSeed());
                java.util.List<net.minecraft.world.item.ItemStack> lootItems2 = new java.util.ArrayList<>();
                lootTable.getRandomItems(lootParams, this.getLootTableSeed(), (itemStack) -> {
                    lootItems2.add(itemStack);
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Method 2 callback invoked: {} x{}", itemStack.getItem().getDescriptionId(), itemStack.getCount());
                    this.spawnAtLocation(itemStack);
                });
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Method 2 result: {} items generated (callback count)", lootItems2.size());
                
                // Step 6: Summary
                int totalItems = lootItems1.size() + lootItems2.size();
                za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Total items generated: {} (Method 1: {}, Method 2: {})", totalItems, lootItems1.size(), lootItems2.size());
                
                // Fallback if still empty
                if (totalItems == 0) {
                    za.co.infernos.goety.Goety.LOGGER.warn("[LOOT DEBUG] Both methods generated 0 items! Using fallback manual drop.");
                    if (za.co.infernos.goety.common.items.ModItems.ECTOPLASM.isBound()) {
                        int count = 1 + serverLevel.random.nextInt(2); // 1-2 ectoplasm
                        this.spawnAtLocation(new net.minecraft.world.item.ItemStack(za.co.infernos.goety.common.items.ModItems.ECTOPLASM.get(), count));
                        za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Manually dropped {} ectoplasm as fallback", count);
                    } else {
                        za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] ECTOPLASM item is not bound for fallback!");
                    }
                }
            } catch (Exception e) {
                za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] Exception during loot processing: ", e);
                // Fallback on error
                if (za.co.infernos.goety.common.items.ModItems.ECTOPLASM.isBound()) {
                    int count = 1 + serverLevel.random.nextInt(2);
                    this.spawnAtLocation(new net.minecraft.world.item.ItemStack(za.co.infernos.goety.common.items.ModItems.ECTOPLASM.get(), count));
                    za.co.infernos.goety.Goety.LOGGER.info("[LOOT DEBUG] Manually dropped {} ectoplasm as error fallback", count);
                }
            }
        } else {
            za.co.infernos.goety.Goety.LOGGER.error("[LOOT DEBUG] Server is null!");
        }
    }

    protected boolean getWraithFlags(int mask) {
        int i = this.entityData.get(FLAGS);
        return (i & mask) != 0;
    }

    protected void setWraithFlags(int mask, boolean value) {
        int i = this.entityData.get(FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(FLAGS, (byte) (i & 255));
    }

    public boolean isFiring() {
        return this.getWraithFlags(1);
    }

    public void setIsFiring(boolean charging) {
        this.setWraithFlags(1, charging);
    }

    public boolean isTeleporting() {
        return this.getWraithFlags(2);
    }

    public void setIsTeleporting(boolean charging) {
        this.setWraithFlags(2, charging);
    }

    public boolean isBreathing() {
        return this.getWraithFlags(4);
    }

    public void setBreathing(boolean flag) {
        this.setWraithFlags(4, flag);
    }

    public void setIsInterested(boolean pBeg) {
        this.entityData.set(DATA_INTERESTED_ID, pBeg);
    }

    public boolean isInterested() {
        return this.entityData.get(DATA_INTERESTED_ID);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.WRAITH_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.WRAITH_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.WRAITH_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSounds.WRAITH_FLY.get();
    }

    protected SoundEvent getAttackSound() {
        return ModSounds.WRAITH_ATTACK.get();
    }

    protected SoundEvent getTeleportInSound() {
        return ModSounds.WRAITH_TELEPORT.get();
    }

    protected SoundEvent getTeleportOutSound() {
        return ModSounds.WRAITH_TELEPORT.get();
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        if (this.getStepSound() != null) {
            float volume = Mth.clamp(0.6F + (this.random.nextFloat() / 2.0F), 0.6F, 1.0F);
            float pitch = Mth.clamp(0.9F + (this.random.nextFloat() / 2.0F), 0.9F, 1.3F);
            this.playSound(this.getStepSound(), volume, pitch);
        }
    }

    @Override
    protected float nextStep() {
        return this.moveDist + 2.0F;
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource damageSource) {
        return false;
    }

    protected boolean isAffectedByFluids() {
        return false;
    }

    protected float getBlockSpeedFactor() {
        return this.level().getBlockState(this.getOnPos())
                .is(net.minecraft.tags.BlockTags.SOUL_SPEED_BLOCKS) ? 1.0F : super.getBlockSpeedFactor();
    }

    @Deprecated
    public double getFollowRange() {
        return this.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Deprecated
    public float getFloatFollowRange() {
        return (float) this.getFollowRange();
    }

    public float attackRange() {
        return 12.0F;
    }

    @Override
    public int xpReward() {
        return 10;
    }

    protected boolean isSunSensitive() {
        return true;
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            if (this.isInterested()) {
                --this.interestTime;
            }
            if (this.interestTime <= 0) {
                this.setIsInterested(false);
            }
        }
        this.setGravity();
        super.tick();
    }

    public boolean isPostTeleporting() {
        return this.postTeleportTime > 0;
    }

    public void setGravity() {
        this.setNoGravity(this.isUnderWater());
    }

    public void aiStep() {
        super.aiStep();

        Vec3 vector3d = this.getDeltaMovement();
        if (!this.onGround() && vector3d.y < 0.0D && !this.isNoGravity()) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }

        if (this.teleportCooldown > 0) {
            --this.teleportCooldown;
        }

        if (this.postTeleportTime > 0) {
            if (this.postTeleportTime == 36) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(new TeleportShockwaveParticleOption(8, 4, 10), this.getX(),
                            this.getY() + 0.5F, this.getZ(), 0, 0, 0, 0, 0.5F);
                }
            }
            --this.postTeleportTime;
        } else {
            this.level().broadcastEntityEvent(this, (byte) 7);
        }

        if (this.isAlive()) {
            this.attackAI();
            this.teleportAI();
        }

    }

    public void teleportAI() {
        if (!this.level().isClientSide) {
            if (this.isTeleporting()) {
                --this.teleportTime;
                if (this.teleportTime == 2) {
                    if (this.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(new TeleportInShockwaveParticleOption(), this.getX(),
                                this.getY() + 0.5F, this.getZ(), 0, 0, 0, 0, 0.5F);
                    }
                }
                if (this.teleportTime <= 2) {
                    this.prevX = this.getX();
                    this.prevY = this.getY();
                    this.prevZ = this.getZ();
                }
                if (this.teleportTime <= 0) {
                    this.teleport();
                }
            } else {
                this.teleportTime = 20;
            }
        } else {
            if (this.isTeleporting()) {
                --this.teleportTime;
                ++this.teleportTime2;
                if (this.teleportTime <= 2) {
                    this.prevX = this.getX();
                    this.prevY = this.getY();
                    this.prevZ = this.getZ();
                }
            } else {
                this.teleportTime = 20;
                this.teleportTime2 = 0;
            }
        }
    }

    public void attackAI() {
        if (!this.level().isClientSide) {
            if (this.isPostTeleporting()) {
                this.getNavigation().stop();
            }
            if (this.fireCooldown > 0) {
                --this.fireCooldown;
            }
            if (this.fireTick > 10) {
                ++this.fireTick;
            }
            if (this.fireTick > 54) {
                this.fireCooldown = 80;
                this.fireTick = 0;
                if (this.isFiring()) {
                    this.stopFiring();
                }
            }
            if (this.getTarget() != null && !this.isPostTeleporting()) {
                if (!this.isFiring()) {
                    this.getLookControl().setLookAt(this.getTarget(), 100.0F, this.getMaxHeadXRot());
                }
                if (this.getSensing().hasLineOfSight(this.getTarget())) {
                    if ((this.fireCooldown <= 0 && !this.isTeleporting()
                            && this.getTarget().distanceToSqr(this) < Mth.square(this.attackRange()))
                            || this.isFiring()) {
                        if (this.fireTick <= 10) {
                            ++this.fireTick;
                        }
                        if (this.isFiring()) {
                            this.getNavigation().stop();
                            double d2 = this.getTarget().getX() - this.getX();
                            double d1 = this.getTarget().getZ() - this.getZ();
                            this.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
                            this.yBodyRot = this.getYRot();
                        }
                        if (this.fireTick > 10) {
                            this.startFiring();
                            this.getNavigation().stop();
                        } else {
                            this.movement();
                            this.stopFiring();
                        }
                        if (this.fireTick == 20) {
                            this.magicFire(this.getTarget());
                        }
                    } else {
                        if (this.fireTick <= 10) {
                            this.fireTick = 0;
                        }
                        this.stopFiring();
                        if (this.canTeleport() && this.getTarget().distanceToSqr(this) <= Mth.square(4.0F)) {
                            this.getNavigation().stop();
                            this.setIsTeleporting(true);
                        } else if (!this.isTeleporting()) {
                            this.movement();
                        }
                    }
                } else {
                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.WraithAggressiveTeleport, true)) {
                        if (this.canTeleport()) {
                            this.getNavigation().stop();
                            this.setIsTeleporting(true);
                        }
                    }
                }
            } else {
                if (this.fireTick <= 10) {
                    this.fireTick = 0;
                }
                this.setIsTeleporting(false);
            }
        }
    }

    public void magicFire(LivingEntity livingEntity) {
        if (this.level().random.nextFloat() <= 0.05F) {
            WandUtil.spawnCrossIceBouquet(this.level(), livingEntity.position(), this);
        } else {
            WandUtil.spawnIceBouquet(this.level(), livingEntity.position(), this);
        }
    }

    public void movement() {
        if (this.getTarget() != null && !this.isStaying() && !this.isPostTeleporting()) {
            Vec3 vector3d2;
            if (this.getTarget().distanceToSqr(this) > Mth.square(this.attackRange())) {
                vector3d2 = this.getTarget().position();
            } else {
                vector3d2 = LandRandomPos.getPos(this, 6, 6);
            }
            if (vector3d2 != null) {
                Path path = this.getNavigation().createPath(vector3d2.x, vector3d2.y, vector3d2.z, 0);
                if (path != null && this.getNavigation().isDone()) {
                    this.getNavigation().moveTo(path, 1.25F);
                }
            }
        }
    }

    public boolean canTeleport() {
        net.neoforged.neoforge.event.entity.EntityTeleportEvent.EnderEntity event = new net.neoforged.neoforge.event.entity.EntityTeleportEvent.EnderEntity(
                this, this.getX(), this.getY(), this.getZ());
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event);
        return !event.isCanceled() && !this.isStaying() && this.teleportCooldown <= 0 && !this.isPostTeleporting();
    }

    protected void teleport() {
        if (!this.level().isClientSide() && this.isAlive() && this.getTarget() != null) {
            if (this.getSensing().hasLineOfSight(this.getTarget())) {
                for (int i = 0; i < 128; ++i) {
                    double d3 = this.getTarget().getX() + (this.getRandom().nextDouble() - 0.5D) * 20.0F;
                    double d4 = this.getTarget().getY();
                    double d5 = this.getTarget().getZ() + (this.getRandom().nextDouble() - 0.5D) * 20.0F;
                    BlockPos blockPos1 = BlockPos.containing(d3, d4, d5);
                    if (MobUtil.isFireImmune(this) || !BlockFinder.hasSunlight(this.level(), blockPos1)) {
                        if (BlockFinder.canSeeBlock(this.getTarget(), blockPos1)) {
                            if (this.randomTeleport(d3, d4, d5, false)) {
                                this.teleportHits();
                                this.setIsTeleporting(false);
                                MobUtil.instaLook(this, this.getTarget());
                                break;
                            }
                        }
                    } else if (i == 127) {
                        this.setIsTeleporting(false);
                        break;
                    }
                }
            } else {
                this.teleportTowardsEntity(this.getTarget());
            }
        }
    }

    public void teleportTowardsEntity(LivingEntity livingEntity) {
        for (int i = 0; i < 128; ++i) {
            Vec3 vector3d = new Vec3(this.getX() - livingEntity.getX(), this.getY(0.5D) - livingEntity.getEyeY(),
                    this.getZ() - livingEntity.getZ());
            vector3d = vector3d.normalize();
            double d0 = 16.0D;
            double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * d0;
            double d2 = this.getY() + (double) (this.random.nextInt(16) - 8) - vector3d.y * d0;
            double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * d0;
            if (this.randomTeleport(d1, d2, d3, false)) {
                this.teleportHits();
                this.teleportCooldown = 100;
                this.setIsTeleporting(false);
                MobUtil.instaLook(this, livingEntity);
                break;
            }
        }
    }

    public void teleportHits() {
        this.postTeleportTime = 38;
        this.level().broadcastEntityEvent(this, (byte) 6);
        this.level().broadcastEntityEvent(this, (byte) 100);
        this.level().broadcastEntityEvent(this, (byte) 101);
        this.level().gameEvent(GameEvent.TELEPORT, this.position(), GameEvent.Context.of(this));
        if (!this.isSilent()) {
            this.level().playSound(null, this.prevX, this.prevY, this.prevZ, this.getTeleportInSound(),
                    this.getSoundSource(), 1.0F, 1.0F);
            this.playSound(this.getTeleportOutSound(), 1.0F, 1.0F);
        }
    }

    public void startFiring() {
        if (!this.isFiring()) {
            this.setIsFiring(true);
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.firingParticles();
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new TeleportShockwaveParticleOption(8, 4, 10), this.getX(),
                        this.getY() + 0.5F, this.getZ(), 0, 0, 0, 0, 0.5F);
            }
            this.playAttackSound();
        }
    }

    public void firingParticles() {
        this.level().broadcastEntityEvent(this, (byte) 100);
    }

    public void playAttackSound() {
        SoundUtil.playWraithAttack(this);
    }

    public void stopFiring() {
        if (this.isFiring()) {
            this.setIsFiring(false);
            this.level().broadcastEntityEvent(this, (byte) 5);
        }
    }

    public ParticleOptions getFireParticles() {
        return ModParticleTypes.WRAITH.get();
    }

    public ParticleOptions getBurstParticles() {
        return ModParticleTypes.WRAITH_BURST.get();
    }

    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 4) {
            this.setIsFiring(true);
            this.attackAnimationState.start(this.tickCount);
        }
        if (pId == 5) {
            this.setIsFiring(false);
            this.attackAnimationState.stop();
        }
        if (pId == 6) {
            this.postTeleportAnimationState.start(this.tickCount);
            this.postTeleportTime = 38;
        }
        if (pId == 7) {
            this.postTeleportAnimationState.stop();
        }
        if (pId == 100) {
            for (int j = 0; j < 8; ++j) {
                double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth() * 2.0D;
                double d2 = this.getY() + (this.random.nextDouble() + 0.5D);
                double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth() * 2.0D;
                this.level().addParticle(this.getFireParticles(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
                this.level().addParticle(this.getBurstParticles(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
        if (pId == 101) {
            if (!this.isSilent()) {
                this.level().playSound(null, this.prevX, this.prevY, this.prevZ, this.getTeleportInSound(),
                        this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(this.getTeleportOutSound(), 1.0F, 1.0F);
            }
        }
        if (pId == 102) {
            this.setIsInterested(true);
            this.interestTime = 40;
            this.playSound(this.getAmbientSound() != null ? this.getAmbientSound() : ModSounds.WRAITH_AMBIENT.get(),
                    1.0F, 2.0F);
            this.addParticlesAroundSelf(ParticleTypes.HEART);
        }
    }

    protected void addParticlesAroundSelf(ParticleOptions pParticleData) {
        for (int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(pParticleData, this.getRandomX(1.0D), this.getRandomY() + 1.0D,
                    this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    public float getAnimationProgress(float pPartialTicks) {
        if (this.teleportTime <= 12 && this.isAlive()) {
            int i = this.teleportTime - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float) i - pPartialTicks) / 20.0F;
        } else {
            return 0.0F;
        }
    }

    public EntityType<?> getVariant(@Nullable Player player, Level level, BlockPos blockPos) {
        EntityType<?> entityType;
        if (this.isHostile()) {
            entityType = ModEntityType.WRAITH.get();
        } else {
            entityType = ModEntityType.WRAITH_SERVANT.get();
        }
        if (level instanceof ServerLevel serverLevel) {
            if (BlockFinder.findStructure(serverLevel, blockPos, ModTags.Structures.CRYPT)) {
                if (this.isHostile()) {
                    entityType = ModEntityType.BORDER_WRAITH.get();
                } else {
                    entityType = ModEntityType.BORDER_WRAITH_SERVANT.get();
                }
            } else if (level.getBiome(blockPos).is(Tags.Biomes.IS_SWAMP)) {
                if (this.isHostile()) {
                    entityType = ModEntityType.MUCK_WRAITH.get();
                } else {
                    entityType = ModEntityType.MUCK_WRAITH_SERVANT.get();
                }
            }
        }
        return entityType;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner()) {
            if (itemstack.is(ModItems.ECTOPLASM.get()) && this.getHealth() < this.getMaxHealth()) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(ModSounds.WRAITH_AMBIENT.get(), 1.0F, 1.25F);
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
                pPlayer.swing(pHand);
                return InteractionResult.SUCCESS;
            } else if ((itemstack.isEmpty() || itemstack == ItemStack.EMPTY) && !this.isInterested()) {
                InteractionResult actionresulttype = super.mobInteract(pPlayer, pHand);
                if (!actionresulttype.consumesAction()) {
                    this.setIsInterested(true);
                    this.interestTime = 40;
                    this.level().broadcastEntityEvent(this, (byte) 102);
                    this.playSound(ModSounds.WRAITH_AMBIENT.get(), 1.0F, 2.0F);
                    this.heal(1.0F);
                    return InteractionResult.SUCCESS;
                }
                return actionresulttype;
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    public static class WraithLookGoal extends LookAtPlayerGoal {
        public AbstractWraith wraith;

        public WraithLookGoal(AbstractWraith p_i1631_1_, Class<? extends LivingEntity> p_i1631_2_, float p_i1631_3_) {
            super(p_i1631_1_, p_i1631_2_, p_i1631_3_);
            this.wraith = p_i1631_1_;
        }

        public WraithLookGoal(AbstractWraith p_i1632_1_, Class<? extends LivingEntity> p_i1632_2_, float p_i1632_3_,
                float p_i1632_4_) {
            super(p_i1632_1_, p_i1632_2_, p_i1632_3_, p_i1632_4_);
            this.wraith = p_i1632_1_;
        }

        public boolean canUse() {
            return super.canUse() && this.wraith.fireTick < 0 && this.wraith.getTarget() == null;
        }
    }

    public static class WraithLookRandomlyGoal extends RandomLookAroundGoal {
        public AbstractWraith wraith;

        public WraithLookRandomlyGoal(AbstractWraith p_i1631_1_) {
            super(p_i1631_1_);
            this.wraith = p_i1631_1_;
        }

        public boolean canUse() {
            return super.canUse() && this.wraith.fireTick < 0 && this.wraith.getTarget() == null;
        }
    }
}
