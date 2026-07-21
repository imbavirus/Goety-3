package za.co.infernos.goety.common.events;

import za.co.infernos.goety.utils.MobType;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.entities.IHiding;
import za.co.infernos.goety.api.entities.IOwned;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.bus.api.SubscribeEvent;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.blocks.EnchanteableBlock;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.ModChestBlock;
import za.co.infernos.goety.common.capabilities.lichdom.ILichdom;
import za.co.infernos.goety.common.capabilities.lichdom.LichProvider;
import za.co.infernos.goety.common.capabilities.misc.IMisc;
import za.co.infernos.goety.common.capabilities.misc.MiscProvider;
import za.co.infernos.goety.common.capabilities.soulenergy.ISoulEnergy;
import za.co.infernos.goety.common.capabilities.soulenergy.SEProvider;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ai.DefendVillagerGoal;
import za.co.infernos.goety.common.entities.ai.FreePrisonerGoal;
import za.co.infernos.goety.common.entities.ai.TargetHostileOwnedGoal;
import za.co.infernos.goety.common.entities.ai.WitchBarterGoal;
import za.co.infernos.goety.common.entities.ally.golem.IceGolem;
import za.co.infernos.goety.common.entities.ally.illager.*;
import za.co.infernos.goety.common.entities.ally.undead.GraveGolem;
import za.co.infernos.goety.common.entities.boss.Apostle;
import za.co.infernos.goety.common.entities.boss.Vizier;
import za.co.infernos.goety.common.entities.deco.HauntedArmorStand;
import za.co.infernos.goety.common.entities.hostile.WitherNecromancer;
import za.co.infernos.goety.common.entities.hostile.cultists.Cultist;
import za.co.infernos.goety.common.entities.hostile.cultists.Heretic;
import za.co.infernos.goety.common.entities.hostile.cultists.Maverick;
import za.co.infernos.goety.common.entities.hostile.cultists.Warlock;
import za.co.infernos.goety.common.entities.hostile.illagers.*;
import za.co.infernos.goety.common.entities.hostile.servants.Damned;
import za.co.infernos.goety.common.entities.hostile.servants.ObsidianMonolith;
import za.co.infernos.goety.common.entities.neutral.BlazeServant;
import za.co.infernos.goety.common.entities.neutral.Owned;
import za.co.infernos.goety.common.entities.projectiles.Fangs;
import za.co.infernos.goety.common.entities.projectiles.ModDragonFireball;
import za.co.infernos.goety.common.entities.util.DragonBreathCloud;
import za.co.infernos.goety.common.entities.util.StormEntity;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.armor.ModArmorMaterials;
import za.co.infernos.goety.common.items.curios.WarlockGarmentItem;
import za.co.infernos.goety.common.items.equipment.DarkScytheItem;
import za.co.infernos.goety.common.items.equipment.IceAxeItem;
import za.co.infernos.goety.common.items.equipment.PhilosophersMaceItem;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayPlayerSoundPacket;
import za.co.infernos.goety.common.network.server.SPlayWorldSoundPacket;
import za.co.infernos.goety.common.research.Research;
import za.co.infernos.goety.common.research.ResearchList;
import za.co.infernos.goety.common.world.structures.ModStructureTags;
import za.co.infernos.goety.compat.iron.IronAttributes;
import za.co.infernos.goety.compat.iron.IronLoaded;
import za.co.infernos.goety.compat.patchouli.PatchouliLoaded;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.init.RaidAdditions;
import za.co.infernos.goety.utils.*;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.*;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataType;
import static net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent.LivingTargetType.MOB_TARGET;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        Player original = event.getOriginal();

//        original.reviveCaps();

        ILichdom originalLichdom = LichdomHelper.getCapability(original);
        ILichdom playerLichdom = player.getData(za.co.infernos.goety.init.ModAttachments.LICHDOM);
        playerLichdom.setLichdom(originalLichdom.getLichdom());
        playerLichdom.setLichMode(originalLichdom.isLichMode());
        playerLichdom.setNightVision(originalLichdom.nightVision());

        ISoulEnergy originalSE = SEHelper.getCapability(original);
        ISoulEnergy playerSE = player.getData(za.co.infernos.goety.init.ModAttachments.SOUL_ENERGY);
        playerSE.setSEActive(originalSE.getSEActive());
        playerSE.setSoulEnergy(originalSE.getSoulEnergy());
        playerSE.setRecoil(originalSE.getRecoil());
        playerSE.setArcaBlock(originalSE.getArcaBlock());
        playerSE.setArcaBlockDimension(originalSE.getArcaBlockDimension());
        playerSE.setRestPeriod(originalSE.getRestPeriod());
        for (Research research : originalSE.getResearch()) {
            playerSE.addResearch(research);
        }
        for (UUID uuid : originalSE.grudgeList()) {
            playerSE.addGrudge(uuid);
        }
        for (UUID uuid : originalSE.allyList()) {
            playerSE.addAlly(uuid);
        }
        for (EntityType<?> entityType : originalSE.grudgeTypeList()) {
            playerSE.addGrudgeType(entityType);
        }
        for (EntityType<?> entityType : originalSE.allyTypeList()) {
            playerSE.addAllyType(entityType);
        }
        playerSE.setBannerBaseColor(originalSE.bannerBaseColor());
        if (originalSE.bannerPattern() != null) {
            playerSE.setBannerPattern(originalSE.bannerPattern());
        }
        playerSE.setApostleWarned(originalSE.apostleWarned());
        playerSE.setCooldowns(originalSE.cooldowns());
        playerSE.setBottling(originalSE.bottling());
        playerSE.setCameraUUID(null);
        playerSE.setMiningProgress(0);
        playerSE.setMiningPos(null);

        IMisc originalMisc = MiscCapHelper.getCapability(original);
        IMisc playerMisc = player.getData(za.co.infernos.goety.init.ModAttachments.MISC);
        playerMisc.setShields(originalMisc.shieldsLeft());
        playerMisc.setShieldTime(originalMisc.shieldTime());
        playerMisc.setShieldCool(originalMisc.shieldCool());
        playerMisc.setAmbientSoundTime(0);

    }

    private static boolean isVillagerTarget(NearestAttackableTargetGoal<?> goal) {
        try {
            java.lang.reflect.Field field = NearestAttackableTargetGoal.class.getDeclaredField("f_26034_"); // targetType
            field.setAccessible(true);
            Class<?> targetType = (Class<?>) field.get(goal);
            return targetType == AbstractVillager.class;
        } catch (Exception e) {
            return false;
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level world = event.getLevel();
        if (entity instanceof LivingEntity && !world.isClientSide()) {
            if (entity instanceof Player player) {
                SEHelper.sendSEUpdatePacket(player);
                LichdomHelper.sendLichUpdatePacket(player);
            }
            if (entity instanceof Mob mob) {
                if (entity instanceof Witch witch) {
                    witch.goalSelector.addGoal(1, new WitchBarterGoal(witch));
                }
                if (mob.getType().is(ModTags.EntityTypes.VILLAGE_GUARDS)) {
                    mob.goalSelector.addGoal(1, new FreePrisonerGoal(mob));
                    mob.targetSelector.addGoal(3, new TargetHostileOwnedGoal<>(mob, Owned.class));
                    mob.targetSelector.addGoal(3, new DefendVillagerGoal(mob));
                }
                if (entity instanceof PathfinderMob creeper && creeper.getType().is(ModTags.EntityTypes.CREEPERS)) {
                    creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, Player.class,
                            (target) -> target != null && CuriosFinder.hasCurio(target, ModItems.FELINE_AMULET.get()),
                            6.0F, 1.0D, 1.2D, EntitySelector.NO_SPECTATORS::test));
                }
                if (entity instanceof Zombie zombie) {
                    boolean villagerHater = zombie.targetSelector
                            .getAvailableGoals()
                            .stream()
                            .anyMatch(goal -> goal.getGoal() instanceof NearestAttackableTargetGoal<?> targetGoal
                                    && isVillagerTarget(targetGoal));
                    if (villagerHater) {
                        zombie.targetSelector.addGoal(3,
                                new NearestAttackableTargetGoal<>(zombie, Prisoner.class, false));
                    }
                }
            }
        }
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.BetterDragonFireball, false)) {
            if (entity instanceof DragonFireball original) {
                ModDragonFireball dragonFireball;
                if (original.getOwner() instanceof LivingEntity livingEntity) {
                    dragonFireball = new ModDragonFireball(entity.level(), livingEntity, original.getDeltaMovement().x,
                            original.getDeltaMovement().y, original.getDeltaMovement().z);
                } else {
                    dragonFireball = new ModDragonFireball(ModEntityType.MOD_DRAGON_FIREBALL.get(), entity.level());
                }
                dragonFireball.moveTo(original.position());
                if (entity.level().addFreshEntity(dragonFireball)) {
                    original.discard();
                    event.setCanceled(true);
                }
            }
            if (entity instanceof AreaEffectCloud cloud) {
                if (cloud.getOwner() instanceof EnderDragon) {
                    DragonBreathCloud breathCloud = new DragonBreathCloud(entity.level(), cloud.getX(), cloud.getY(),
                            cloud.getZ());
                    breathCloud.setOwner(cloud.getOwner());
                    breathCloud.setRadius(cloud.getRadius());
                    breathCloud.setRadiusOnUse(cloud.getRadiusOnUse());
                    breathCloud.setRadiusPerTick(cloud.getRadiusPerTick());
                    breathCloud.setDuration(cloud.getDuration());
                    breathCloud.setDurationOnUse(cloud.getDurationOnUse());
                    breathCloud.setWaitTime(cloud.getWaitTime());
                    if (entity.level().addFreshEntity(breathCloud)) {
                        cloud.discard();
                        event.setCanceled(true);
                    }
                }
            }
        }
        if (entity instanceof StormEntity) {
            if (!entity.level().isClientSide) {
                ServerLevel serverWorld = (ServerLevel) entity.level();
                serverWorld.setWeatherParameters(0, 6000, true, true);
            }
        }
        if (entity instanceof Raider raider) {
            if (world instanceof ServerLevel) {
                if (raider.hasActiveRaid()) {
                    Raid raid = raider.getCurrentRaid();
                    if (raid != null && raid.isActive() && !raid.isBetweenWaves() && !raid.isOver()
                            && !raid.isStopped()) {
                        Player player = EntityFinder.getNearbyPlayer(world, raid.getCenter());
                        if (player != null) {
                            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.IllagerRaid, false)) {
                                if (SEHelper
                                        .getSoulAmountInt(player) < (za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.IllagerAssaultSEThreshold, 0) * 2)) {
                                    if (raider instanceof HuntingIllagerEntity) {
                                        raid.removeFromRaid(raider, true);
                                        event.setCanceled(true);
                                    }
                                }
                            } else {
                                if (raider instanceof HuntingIllagerEntity) {
                                    raid.removeFromRaid(raider, true);
                                    event.setCanceled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEntersWorld(PlayerEvent.PlayerLoggedInEvent event) {
        CompoundTag playerData = event.getEntity().getPersistentData();
        CompoundTag data;

        if (!playerData.contains(Player.PERSISTED_NBT_TAG)) {
            data = new CompoundTag();
        } else {
            data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
        }
        if (!event.getEntity().level().isClientSide) {
            if (data.getBoolean(ConstantPaths.readScroll())) {
                SEHelper.addResearch(event.getEntity(), ResearchList.FORBIDDEN);
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.StarterTotem, false)) {
                if (!data.getBoolean("goety:gotTotem")) {
                    event.getEntity().addItem(new ItemStack(ModItems.TOTEM_OF_ROOTS.get()));
                    data.putBoolean("goety:gotTotem", true);
                    playerData.put(Player.PERSISTED_NBT_TAG, data);
                }
            }
            // TODO(1.21): Patchouli dependency is currently disabled for 1.21.1; starter
            // book logic will be re-enabled when Patchouli updates.
        }

    }

    private static final Map<ServerLevel, IllagerSpawner> ILLAGER_SPAWN_MAP = new HashMap<>();
    private static final Map<ServerLevel, WightSpawner> WIGHT_SPAWN_MAP = new HashMap<>();
    private static final Map<ServerLevel, NaturalMobSpawner> NATURAL_MOB_SPAWN_MAP = new HashMap<>();

    @SubscribeEvent
    public static void worldLoad(LevelEvent.Load event) {
        RaidAdditions.addRaiders();
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverWorld) {
            ILLAGER_SPAWN_MAP.put(serverWorld, new IllagerSpawner());
            WIGHT_SPAWN_MAP.put(serverWorld, new WightSpawner());
            NATURAL_MOB_SPAWN_MAP.put(serverWorld, new NaturalMobSpawner());
        }
    }

    @SubscribeEvent
    public static void worldUnload(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverWorld) {
            ILLAGER_SPAWN_MAP.remove(serverWorld);
            WIGHT_SPAWN_MAP.remove(serverWorld);
            NATURAL_MOB_SPAWN_MAP.remove(serverWorld);
        }
    }

    @SubscribeEvent
    public static void onServerTick(LevelTickEvent.Post tick) {
        Level level = tick.getLevel();
        if (!level.isClientSide() && level instanceof ServerLevel serverWorld) {
            IllagerSpawner illagerSpawner = ILLAGER_SPAWN_MAP.get(serverWorld);
            if (illagerSpawner != null) {
                illagerSpawner.tick(serverWorld);
            }
            WightSpawner wightSpawner = WIGHT_SPAWN_MAP.get(serverWorld);
            if (wightSpawner != null) {
                wightSpawner.tick(serverWorld);
            }
            NaturalMobSpawner naturalMobSpawner = NATURAL_MOB_SPAWN_MAP.get(serverWorld);
            if (naturalMobSpawner != null) {
                naturalMobSpawner.tick(serverWorld);
            }
        }

    }

    @SubscribeEvent
    public static void CheckSpawnEvents(FinalizeSpawnEvent event) {
        if (event.getEntity() instanceof SpellcasterIllager || event.getEntity() instanceof Witch
                || event.getEntity() instanceof Cultist) {
            if (event.getSpawnType() == MobSpawnType.STRUCTURE) {
                event.getEntity().addTag(ConstantPaths.structureMob());
            }
        }
        if (event.getSpawnType() == MobSpawnType.STRUCTURE) {
            if (event.getEntity().getTags().contains(ConstantPaths.giveAI())) {
                if (event.getEntity().isNoAi()) {
                    event.getEntity().setNoAi(false);
                    event.getEntity().removeTag(ConstantPaths.giveAI());
                }
            }
        }
        if (event.getEntity() instanceof Cultist cultist) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                if (serverLevel.getRaidAt(cultist.blockPosition()) != null) {
                    if (event.getSpawnType() == MobSpawnType.NATURAL
                            || event.getSpawnType() == MobSpawnType.CHUNK_GENERATION) {
                        event.setSpawnCancelled(true);
                    }
                }
            }
        }
        Mob mob = event.getEntity();
        if (IronLoaded.IRON_SPELLBOOKS.isLoaded()) {
            if (!IronAttributes.resistances(mob).isEmpty()) {
                for (AttributeInstance attributeInstance : IronAttributes.resistances(mob)) {
                    if (attributeInstance != null) {
                        if (mob instanceof Inquillager) {
                            attributeInstance.setBaseValue(1.75D);
                        } else {
                            if (attributeInstance.getAttribute() == IronAttributes.EVOCATION_MAGIC_RESIST) {
                                if (mob instanceof Envioker || mob instanceof Minister || mob instanceof Vizier) {
                                    attributeInstance.setBaseValue(1.25D);
                                }
                            }
                            if (attributeInstance.getAttribute() == IronAttributes.NATURE_MAGIC_RESIST) {
                                if (mob instanceof Conquillager) {
                                    attributeInstance.setBaseValue(1.25D);
                                }
                                if (mob instanceof Apostle) {
                                    attributeInstance.setBaseValue(1.5D);
                                }
                                if (MobUtil.getMobType((LivingEntity)mob) == ModMobType.NATURAL) {
                                    attributeInstance.setBaseValue(1.5D);
                                }
                            }
                            if (attributeInstance.getAttribute() == IronAttributes.HOLY_MAGIC_RESIST) {
                                if (mob instanceof Conquillager || mob instanceof Preacher || mob instanceof Minister
                                        || mob instanceof Vizier) {
                                    attributeInstance.setBaseValue(1.25D);
                                }
                                if (mob instanceof Apostle) {
                                    attributeInstance.setBaseValue(0.25D);
                                }
                            }
                            if (attributeInstance.getAttribute() == IronAttributes.ICE_MAGIC_RESIST) {
                                if (mob instanceof Cryologer) {
                                    attributeInstance.setBaseValue(1.5D);
                                }
                                if (mob instanceof IceGolem) {
                                    attributeInstance.setBaseValue(2.0D);
                                }
                                if (mob instanceof BlazeServant) {
                                    attributeInstance.setBaseValue(0.5D);
                                }
                            }
                            if (attributeInstance.getAttribute() == IronAttributes.LIGHTNING_MAGIC_RESIST) {
                                if (mob instanceof StormCaster) {
                                    attributeInstance.setBaseValue(1.5D);
                                }
                            }
                            if (attributeInstance.getAttribute() == IronAttributes.FIRE_MAGIC_RESIST) {
                                if (mob instanceof Apostle) {
                                    attributeInstance.setBaseValue(2.0D);
                                }
                                if (mob instanceof IceGolem) {
                                    attributeInstance.setBaseValue(0.33D);
                                }
                            }
                            if (attributeInstance.getAttribute() == IronAttributes.BLOOD_MAGIC_RESIST) {
                                if (mob instanceof Apostle) {
                                    attributeInstance.setBaseValue(1.75D);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void PlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level world = player.level();
        if (world instanceof ServerLevel serverLevel) {
            if (player.tickCount % 20 == 0) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if (serverPlayer.getServer() != null) {
                        net.minecraft.advancements.AdvancementHolder advancement3 = serverPlayer.getServer().getAdvancements()
                                .get(Goety.location("goety/read_warred_and_haunting_scroll"));
                        if (advancement3 != null) {
                            AdvancementProgress advancementProgress3 = serverPlayer.getAdvancements()
                                    .getOrStartProgress(advancement3);
                            if (!advancementProgress3.isDone()) {
                                net.minecraft.advancements.AdvancementHolder advancement1 = serverPlayer.getServer().getAdvancements()
                                        .get(Goety.location("goety/read_warred_scroll"));
                                net.minecraft.advancements.AdvancementHolder advancement2 = serverPlayer.getServer().getAdvancements()
                                        .get(Goety.location("goety/read_haunting_scroll"));
                                if (advancement1 != null && advancement2 != null) {
                                    AdvancementProgress advancementProgress1 = serverPlayer.getAdvancements()
                                            .getOrStartProgress(advancement1);
                                    AdvancementProgress advancementProgress2 = serverPlayer.getAdvancements()
                                            .getOrStartProgress(advancement2);
                                    if (advancementProgress1.isDone() && advancementProgress2.isDone()) {
                                        for (String s : advancementProgress3.getRemainingCriteria()) {
                                            serverPlayer.getAdvancements().award(advancement3, s);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.ShriekObeliskRaid, false)) {
                Raid raid = serverLevel.getRaidAt(player.blockPosition());
                if (raid != null) {
                    int cost = za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.ShriekObeliskCost, 0) * 1; // TODO: Fix Bad Omen Level retrieval
                    if (BlockFinder.findIllagerWard(serverLevel, player, cost)) {
                        raid.stop();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingEffects(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) {
            return;
        }
        if (livingEntity != null && livingEntity.isAlive()) {
            if (!MobUtil.isSpellCasting(livingEntity)) {
                if (MiscCapHelper.getClientTargetID(livingEntity) != 0) {
                    MiscCapHelper.setClientTargetID(livingEntity, 0);
                }
            }
            if (MiscCapHelper.getShields(livingEntity) > 0) {
                if (MiscCapHelper.getShieldTime(livingEntity) > 0) {
                    MiscCapHelper.decreaseShieldTime(livingEntity);
                } else {
                    MiscCapHelper.setShields(livingEntity, 0);
                    if (!livingEntity.level().isClientSide) {
                        if (livingEntity instanceof Player player) {
                            ModNetwork.sendTo(player,
                                    new SPlayPlayerSoundPacket(ModSounds.WALL_DISAPPEAR.get(), 1.0F, 2.0F));
                        } else {
                            livingEntity.playSound(ModSounds.WALL_DISAPPEAR.get(), 1.0F, 2.0F);
                        }
                    }
                }
            } else {
                if (MiscCapHelper.getShieldTime(livingEntity) > 0) {
                    MiscCapHelper.setShieldTime(livingEntity, 0);
                }
            }
            if (MiscCapHelper.getShieldCool(livingEntity) > 0) {
                MiscCapHelper.decreaseShieldCool(livingEntity);
            }
            if (MiscCapHelper.getShakeTime(livingEntity) > 0) {
                MiscCapHelper.setShakeTime(livingEntity, MiscCapHelper.getShakeTime(livingEntity) - 1);
            }
            if (livingEntity instanceof Mob mob) {
                double followRange = 32.0D;
                if (mob.getAttribute(Attributes.FOLLOW_RANGE) != null) {
                    followRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE) * 2;
                }
                // MiscCapHelper.updateMobTarget(mob); Commented in case it causes lag
                if (mob.getTarget() instanceof Apostle apostle) {
                    if (apostle.obsidianInvul > 5) {
                        for (ObsidianMonolith obsidianMonolith : mob.level().getEntitiesOfClass(ObsidianMonolith.class,
                                mob.getBoundingBox().inflate(followRange, 8.0D, followRange))) {
                            if (obsidianMonolith.getOwner() == apostle) {
                                mob.setTarget(obsidianMonolith);
                            }
                        }
                    }
                }
                if (mob.getTarget() instanceof ObsidianMonolith monolith) {
                    if (monolith.empowered > 5) {
                        for (Heretic heretic : mob.level().getEntitiesOfClass(Heretic.class,
                                mob.getBoundingBox().inflate(followRange, 8.0D, followRange))) {
                            if (heretic.getMonolith() == monolith) {
                                mob.setTarget(heretic);
                            }
                        }
                    }
                }
                if (mob.getTarget() instanceof IHiding hiding) {
                    if (hiding.isHiding()) {
                        mob.setTarget(null);
                    }
                }
            }
            if (livingEntity instanceof Raider raider) {
                if (raider.getTarget() instanceof Player player) {
                    if (SEHelper.getSoulAmountInt(player) > za.co.infernos.goety.utils.ConfigHelper.getInt(MobsConfig.IllagerAssaultSEThreshold, 0) * 2) {
                        if (!raider.isAggressive()) {
                            raider.setAggressive(true);
                        }
                    }
                }
            }
            if (livingEntity instanceof Villager villager) {
                if (!villager.level().isClientSide) {
                    Brain<?> brain = villager.getBrain();
                    Optional<LivingEntity> avoidIllager = Optional.empty();
                    NearestVisibleLivingEntities nearestvisiblelivingentities = brain
                            .getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                            .orElse(NearestVisibleLivingEntities.empty());
                    for (LivingEntity livingentity : nearestvisiblelivingentities.findAll((p_186157_) -> true)) {
                        if (livingentity instanceof HuntingIllagerEntity) {
                            avoidIllager = Optional.of(livingentity);
                        } else if (livingentity instanceof RaiderServant servant) {
                            if (servant.isRaiding() || servant.isHostile()) {
                                avoidIllager = Optional.of(livingentity);
                                if (servant.isRaiding()) {
                                    brain.setMemory(MemoryModuleType.HEARD_BELL_TIME, villager.level().getGameTime());
                                }
                            }
                        }
                    }
                    if (avoidIllager.isPresent()) {
                        brain.setMemory(MemoryModuleType.NEAREST_HOSTILE, avoidIllager);
                        if (avoidIllager.get() instanceof RaiderServant servant && servant.isRaiding()) {
                            if (!villager.isNoAi() && villager.getRandom().nextInt(100) == 0) {
                                villager.level().broadcastEntityEvent(villager, (byte) 42);
                            }
                        }
                    }
                    Player player = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).orElse(null);
                    if (player != null) {
                        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.VillagerHate, false)) {
                            if (CuriosFinder.hasCurio(player, item -> item.is(ModTags.Items.ROBES))) {
                                if (villager.getPlayerReputation(player) > -25
                                        && villager.getPlayerReputation(player) < 25) {
                                    villager.getGossips().add(player.getUUID(), GossipType.MINOR_NEGATIVE, 25);
                                }
                            }
                        }
                        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.VillagerHateRavager, false)) {
                            for (Owned owned : player.level().getEntitiesOfClass(Owned.class,
                                    player.getBoundingBox().inflate(16.0D))) {
                                if (owned instanceof Ravaged || owned instanceof ModRavager) {
                                    if (owned.getTrueOwner() == player || owned.getMasterOwner() == player) {
                                        if (villager.getPlayerReputation(player) > -200) {
                                            villager.getGossips().add(player.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (villager.level() instanceof ServerLevel serverLevel) {
                        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.VillagerConvertWarlock, false)) {
                            if (BlockFinder.getVerticalBlock(serverLevel, villager.blockPosition(),
                                    Blocks.CRYING_OBSIDIAN.defaultBlockState(), 16, true)) {
                                if (villager.getRandom().nextFloat() < 7.5E-4F
                                        && serverLevel.getDifficulty() != Difficulty.PEACEFUL) {
                                    if (net.neoforged.neoforge.event.EventHooks.canLivingConvert(villager,
                                            ModEntityType.WARLOCK.get(), (timer) -> {
                                            })) {
                                        serverLevel.explode(villager, villager.getX(), villager.getY(), villager.getZ(),
                                                0.1F, Level.ExplosionInteraction.NONE);
                                        Warlock warlock = ModEntityType.WARLOCK.get().create(serverLevel);
                                        if (warlock != null) {
                                            warlock.moveTo(villager.getX(), villager.getY(), villager.getZ(),
                                                    villager.getYRot(), villager.getXRot());
                                            warlock.finalizeSpawn(serverLevel,
                                                    serverLevel.getCurrentDifficultyAt(warlock.blockPosition()),
                                                    MobSpawnType.CONVERSION, (SpawnGroupData) null);
                                            warlock.setNoAi(villager.isNoAi());
                                            if (villager.hasCustomName()) {
                                                warlock.setCustomName(villager.getCustomName());
                                                warlock.setCustomNameVisible(villager.isCustomNameVisible());
                                            }

                                            warlock.setPersistenceRequired();
                                            net.neoforged.neoforge.event.EventHooks.onLivingConvert(villager, warlock);
                                            serverLevel.addFreshEntityWithPassengers(warlock);
                                            MobUtil.releaseAllPois(villager);
                                            villager.discard();
                                        }
                                    }
                                }
                            }
                        }
                        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.VillagerConvertHeretic, false)) {
                            if (villager.getRandom().nextFloat() < 7.5E-4F && villager.isSleeping()
                                    && serverLevel.getDifficulty() != Difficulty.PEACEFUL) {
                                if (BlockFinder.findNetherPortal(serverLevel, villager.blockPosition(), 8)
                                        .isPresent()) {
                                    if (net.neoforged.neoforge.event.EventHooks.canLivingConvert(villager,
                                            ModEntityType.HERETIC.get(), (timer) -> {
                                            })) {
                                        serverLevel.explode(villager, villager.getX(), villager.getY(), villager.getZ(),
                                                0.1F, Level.ExplosionInteraction.NONE);
                                        Heretic heretic = ModEntityType.HERETIC.get().create(serverLevel);
                                        if (heretic != null) {
                                            heretic.moveTo(villager.getX(), villager.getY(), villager.getZ(),
                                                    villager.getYRot(), villager.getXRot());
                                            heretic.finalizeSpawn(serverLevel,
                                                    serverLevel.getCurrentDifficultyAt(heretic.blockPosition()),
                                                    MobSpawnType.CONVERSION, (SpawnGroupData) null);
                                            heretic.setNoAi(villager.isNoAi());
                                            if (villager.hasCustomName()) {
                                                heretic.setCustomName(villager.getCustomName());
                                                heretic.setCustomNameVisible(villager.isCustomNameVisible());
                                            }

                                            heretic.setPersistenceRequired();
                                            net.neoforged.neoforge.event.EventHooks.onLivingConvert(villager, heretic);
                                            serverLevel.addFreshEntityWithPassengers(heretic);
                                            MobUtil.releaseAllPois(villager);
                                            villager.discard();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakingBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.getMainHandItem().getItem() instanceof PhilosophersMaceItem) {
            if (event.getState().getBlock().getDescriptionId().contains("nether_gold")) {
                if (!player.level().isClientSide) {
                    Block.dropResources(Blocks.GOLD_ORE.defaultBlockState(), player.level(), event.getPos(), null,
                            player, player.getMainHandItem());
                    event.getState().getBlock().playerWillDestroy(player.level(), event.getPos(), event.getState(),
                            player);
                    player.level().setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
                    event.setCanceled(true);
                }
            }
        }
        if (player.getMainHandItem().getItem() instanceof DarkScytheItem) {
            ItemStack scythe = player.getMainHandItem();
            if (event.getState().getBlock().getDescriptionId().contains("sculk")
                    && event.getState().is(BlockTags.MINEABLE_WITH_HOE)) {
                if (!player.level().isClientSide) {
                    ItemStack fakeItem = new ItemStack(Items.DIAMOND_HOE);
                    fakeItem.enchant(player.registryAccess().holderOrThrow(Enchantments.SILK_TOUCH), 1);
                    net.minecraft.world.item.enchantment.ItemEnchantments itemenchantments = scythe.getEnchantments();
                    if (!itemenchantments.isEmpty()) {
                        for (net.minecraft.core.Holder<Enchantment> enchantment : itemenchantments.keySet()) {
                            if (!enchantment.is(net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH)) {
                                fakeItem.enchant(enchantment, itemenchantments.getLevel(enchantment));
                            }
                        }
                    }
                    if (event.getState().getBlock() instanceof EnchanteableBlock enchanteableBlock) {
                        BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
                        enchanteableBlock.playerDestroy(player.level(), event.getPlayer(), event.getPos(),
                                event.getState(), blockEntity, fakeItem);
                    } else {
                        Block.dropResources(event.getState(), player.level(), event.getPos(), null, player, fakeItem);
                    }
                    player.level().levelEvent(player, 2001, event.getPos(), Block.getId(event.getState()));
                    player.level().setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
                    ItemHelper.hurtAndBreak(player.getMainHandItem(), 1, player);
                    event.setCanceled(true);
                }
            }
        }
        if (player.getMainHandItem().getItem() instanceof IceAxeItem) {
            ItemStack iceAxe = player.getMainHandItem();
            if (event.getState().is(BlockTags.ICE)) {
                if (!player.level().isClientSide) {
                    ItemStack fakeItem = new ItemStack(Items.IRON_PICKAXE);
                    fakeItem.enchant(player.registryAccess().holderOrThrow(Enchantments.SILK_TOUCH), 1);
                    net.minecraft.world.item.enchantment.ItemEnchantments map1 = iceAxe.getEnchantments();
                    if (!map1.isEmpty()) {
                        for (net.minecraft.core.Holder<Enchantment> enchantment : map1.keySet()) {
                            if (!enchantment.is(Enchantments.SILK_TOUCH)) {
                                fakeItem.enchant(enchantment, map1.getLevel(enchantment));
                            }
                        }
                    }
                    if (event.getState().getBlock() instanceof EnchanteableBlock enchanteableBlock) {
                        BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
                        enchanteableBlock.playerDestroy(player.level(), event.getPlayer(), event.getPos(),
                                event.getState(), blockEntity, fakeItem);
                    } else {
                        Block.dropResources(event.getState(), player.level(), event.getPos(), null, player, fakeItem);
                    }
                    event.getState().getBlock().playerWillDestroy(player.level(), event.getPos(), event.getState(),
                            player);
                    player.level().setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
                    ItemHelper.hurtAndBreak(player.getMainHandItem(), 1, player);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void TargetEvents(LivingChangeTargetEvent event) {
        LivingEntity attacker = event.getEntity();
        /* TODO: Fix LivingChangeTargetEvent accessors
        LivingEntity target = event.getOriginalTarget();
        if (attacker instanceof Mob mobAttacker) {
            if (target != null) {
                if (attacker instanceof IOwned) {
                    if (target instanceof ArmorStand || target instanceof HauntedArmorStand) {
                        if (event.getTargetType() == MOB_TARGET) {
                            event.setNewTarget(null);
                        } else {
                            event.setCanceled(true);
                        }
                    }
                }
                if ((MobUtil.getMobType(mobAttacker) == MobType.UNDEAD && !(mobAttacker instanceof IOwned)
                        && mobAttacker.getMaxHealth() < 100.0F) || mobAttacker instanceof Creeper) {
                    if (event.getNewTarget() instanceof Apostle) {
                        event.setCanceled(true);
                    }
                }
                if (mobAttacker.getType().is(ModTags.EntityTypes.CREEPERS)
                        && CuriosFinder.hasCurio(target, ModItems.FELINE_AMULET.get())) {
                    if (event.getTargetType() == MOB_TARGET) {
                        event.setNewTarget(null);
                    } else {
                        event.setCanceled(true);
                    }
                }
                if (mobAttacker instanceof Phantom && CuriosFinder.hasCurio(target, ModItems.FELINE_AMULET.get())) {
                    if (event.getTargetType() == MOB_TARGET) {
                        event.setNewTarget(null);
                    } else {
                        event.setCanceled(true);
                    }
                }
            }
        }
    } */
    }

    @SubscribeEvent
    public static void AttackEvent(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        Entity source = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();
        if (!event.getEntity().level().isClientSide) {
            if (MiscCapHelper.getShields(victim) > 0 && !event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                if (MiscCapHelper.getShieldCool(victim) <= 0) {
                    MiscCapHelper.decreaseShields(victim);
                    MiscCapHelper.setShieldCool(victim, 10);
                    if (event.getSource().getEntity() instanceof LivingEntity livingEntity) {
                        MobUtil.knockBack(livingEntity, victim, 1.0D, 0.2D, 1.0D);
                    }
                }
                event.setCanceled(true);
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.GoodwillNoDamage, false)) {
                Player player = null;
                if (source instanceof Player player1) {
                    player = player1;
                } else if (MobUtil.getOwner(source) instanceof Player player1) {
                    player = player1;
                }
                if (player != null) {
                    if (SEHelper.getAllyEntities(player).contains(victim)
                            || SEHelper.getAllyEntityTypes(player).contains(MobUtil.getMobType(victim))) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        if (event.getSource() instanceof NoKnockBackDamageSource damageSource) {
            if (damageSource.getOwner() != null) {
                if (damageSource.getOwner() instanceof LivingEntity && !damageSource.is(DamageTypeTags.NO_ANGER)) {
                    victim.setLastHurtByMob((LivingEntity) damageSource.getOwner());
                }
                if (damageSource.getOwner() instanceof Player player) {
                    victim.setLastHurtByMob(player);
                }
                if (damageSource.getOwner() instanceof ServerPlayer) {
                    CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer) damageSource.getOwner(), victim,
                            event.getSource(), event.getAmount(), event.getAmount(), false);
                }
                if (damageSource.getOwner() instanceof IOwned owned) {
                    if (owned.getMasterOwner() instanceof Player player) {
                        victim.setLastHurtByMob(player);
                    }
                }
            }
        }

        if (source instanceof IOwned owned) {
            if (owned.getMasterOwner() instanceof Player player) {
                victim.setLastHurtByMob(player);
            }
        }

        if (direct instanceof AbstractArrow arrowEntity) {
            if (arrowEntity.getTags().contains(ConstantPaths.rainArrow())
                    || arrowEntity.getOwner() instanceof Apostle) {
                if (arrowEntity.getOwner() != null) {
                    if (victim instanceof IOwned ownedEntity) {
                        if (ownedEntity.getTrueOwner() != null) {
                            if (ownedEntity.getTrueOwner() == arrowEntity.getOwner()) {
                                event.setCanceled(true);
                            }
                        }
                    }
                    if (victim == arrowEntity.getOwner()) {
                        event.setCanceled(true);
                    }
                }
            }
            if (!(arrowEntity.getOwner() instanceof Apostle && victim.level().getDifficulty() == Difficulty.HARD)) {
                if (victim instanceof Player player) {
                    if (MobUtil.starAmuletActive(player)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (ModDamageSource.shockAttacks(event.getSource())) {
            if (victim.level() instanceof ServerLevel serverLevel) {
                ServerParticleUtil.addParticlesAroundSelf(serverLevel, ModParticleTypes.BIG_ELECTRIC.get(), victim);
                ModNetwork
                        .sendToALL(new SPlayWorldSoundPacket(victim.blockPosition(), ModSounds.ZAP.get(), 2.0F, 1.0F));
            }
        }
        if (ModDamageSource.isMagicFire(event.getSource())) {
            float amount = event.getAmount();
            if (victim.fireImmune()) {
                amount /= 2.0F;
            }
            if (victim.level() instanceof ServerLevel serverLevel) {
                float k = EnchantmentHelper.getDamageProtection(serverLevel, victim, victim.damageSources().inFire());
                if (k > 0) {
                    amount = CombatRules.getDamageAfterMagicAbsorb(amount, (float) k);
                }
            }
            event.setAmount(amount);
        }
        if (ModDamageSource.hellfireAttacks(event.getSource())) {
            if (victim.level() instanceof ServerLevel serverLevel) {
                ServerParticleUtil.addParticlesAroundSelf(serverLevel, ModParticleTypes.BIG_FIRE.get(), victim);
                ModNetwork.sendToALL(
                        new SPlayWorldSoundPacket(victim.blockPosition(), SoundEvents.PLAYER_HURT_ON_FIRE, 2.0F, 1.0F));
            }
            float amount = event.getAmount();
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HellfireFireImmune, false)) {
                if (victim.fireImmune()) {
                    amount /= 2.0F;
                }
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HellfireFireProtection, false)) {
                int k = 0;
                for (ItemStack itemStack : victim.getArmorSlots()) {
                    int i = EnchantmentHelper.getItemEnchantmentLevel(victim.registryAccess().holderOrThrow(Enchantments.FIRE_PROTECTION), itemStack);
                    if (i > 0) {
                        k += i * 2; // Approximation as getDamageProtection is data-driven in 1.21
                    }
                }
                if (k > 0) {
                    amount = CombatRules.getDamageAfterMagicAbsorb(amount, (float) k / 2.0F);
                }
            }
            event.setAmount(amount);
        }
        if (victim instanceof BlazeServant) {
            if (event.getSource().getDirectEntity() instanceof Snowball) {
                if (event.getSource().is(DamageTypes.THROWN)) {
                    if (event.getAmount() <= 0.0F) {
                        event.setAmount(3.0F);
                    }
                }
            }
        }
        if (victim instanceof Prisoner) {
            Entity entity = event.getSource().getEntity();
            if (entity instanceof Mob mob) {
                if (mob.getType().is(ModTags.EntityTypes.VILLAGE_GUARDS)) {
                    if (event.getSource().getDirectEntity() == event.getSource().getEntity()) {
                        if (mob.getTarget() != victim) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void DamageEvent(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (target instanceof Player player) {
            if (MobUtil.starAmuletActive(player)) {
                if (event.getSource().getDirectEntity() instanceof AbstractArrow arrow
                        && !(arrow.getOwner() instanceof Apostle && target.level().getDifficulty() == Difficulty.HARD)) {
                    event.setCanceled(true);
                }
            }
        }

        if (event.getSource().getDirectEntity() instanceof Fangs fangEntity) {
            if (fangEntity.getOwner() instanceof Player player) {
                if (fangEntity.isAbsorbing()) {
                    player.heal(event.getAmount());
                }
            }
        }
        if (event.getAmount() > 0.0F) {
            float damageAmount = event.getAmount();
            if (event.getSource().is(ModDamageSource.LIFE_LEECH)
                    && event.getSource() instanceof NoKnockBackDamageSource damageSource
                    && damageSource.getOwner() instanceof LivingEntity livingEntity) {
                float percent = za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LeechingPercent, 0) / 100.0F;
                livingEntity.heal(event.getAmount() * percent);
            }
            if (target.isInWaterOrRain()) {
                if (ModDamageSource.shockAttacks(event.getSource())) {
                    event.setAmount(damageAmount * 1.5F);
                }
            }
            if (ModDamageSource.freezeAttacks(event.getSource())) {
                if (target.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
                    event.setAmount(damageAmount * 0.5F);
                }
            }
            if (ModDamageSource.waterAttacks(event.getSource())) {
                if (target.isSensitiveToWater()) {
                    event.setAmount(damageAmount * 2.0F);
                } else if (MobUtil.getMobType(target) == MobType.WATER) {
                    event.setAmount(damageAmount * 0.5F);
                }
            }
            float totalReduce = 0;
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (equipmentSlot.isArmor()) {
                    ItemStack itemStack = target.getItemBySlot(equipmentSlot);
                    if (itemStack.getItem() instanceof ArmorItem armorItem) {
                        // Compare by registry identity — never rely on a shared leather fallback Holder.
                        if (ModArmorMaterials.isBlackIronOrDark(armorItem.getMaterial())) {
                            float reducedDamage = getReducedDamage(event, armorItem);
                            totalReduce += reducedDamage;
                        }
                    }
                }
            }
            if (totalReduce > 0) {
                damageAmount -= totalReduce;
                damageAmount = Math.max(0, damageAmount);
                event.setAmount(damageAmount);
            }
            /*
             * if (event.getSource().getEntity() instanceof Player attacker) {
             * if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.SOUL_EATER.get(),
             * attacker) > 0) {
             * int level =
             * EnchantmentHelper.getEnchantmentLevel(ModEnchantments.SOUL_EATER.get(),
             * attacker);
             * int percent = ((level - 1) * 5) + 15;
             * float rawPercent = (float) SEHelper.getSoulAmountInt(attacker) /
             * za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxArcaSouls, 0);
             * float totalPercent = rawPercent * percent;
             * if (attacker.level().getRandom().nextFloat() <= totalPercent){
             * event.setAmount(damageAmount * 2.0F);
             * }
             * }
             * }
             */
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            if (victim.getType().is(ModTags.EntityTypes.WITCH_BARTER)) {
                event.setAmount(event.getAmount() * 0.15F);
            }
        } else if (event.getSource().is(DamageTypeTags.IS_FIRE) || event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
            if (victim instanceof AbstractIllager || victim instanceof Witch) {
                float reduction = 0.50F;
                event.setAmount(event.getAmount() * reduction);
            }
        }
    }

    /**
     * Original Goety magic/fire/explosion mitigation for black iron and dark armor pieces.
     * Must never return the full damage amount for every hit (that made leather invulnerable
     * when materials incorrectly shared the leather Holder).
     */
    public static float getReducedDamage(LivingIncomingDamageEvent event, ArmorItem armorItem) {
        float reduction = 0.0F;
        if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            reduction = armorItem.getDefense() / 25.0F;
        } else if (event.getSource().is(DamageTypeTags.IS_FIRE) || event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
            reduction = armorItem.getDefense() / 10.0F;
        }
        return event.getAmount() * reduction;
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if ((event.getEntity().hasEffect(GoetyEffects.BURN_HEX)
                || ModDamageSource.hellfireAttacks(event.getEntity().getLastDamageSource()))
                && event.getAmount() > 0.0F) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void SpecialDeath(LivingDeathEvent event) {
        LivingEntity killed = event.getEntity();
        Entity killer = event.getSource().getEntity();
        Level world = killed.getCommandSenderWorld();
        if (event.getSource() instanceof NoKnockBackDamageSource noKnockBackDamageSource) {
            killer = noKnockBackDamageSource.getOwner();
        }
        if (killed instanceof PathfinderMob) {
            if (killed.hasEffect(GoetyEffects.VOID_TOUCHED)) {
                if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    int amp = Objects.requireNonNull(killed.getEffect(GoetyEffects.VOID_TOUCHED)).getAmplifier()
                            + 1;
                    for (int i = 0; i < (killed.level().random.nextInt(3) + 1) * amp; ++i) {
                        killed.spawnAtLocation(new ItemStack(Items.GOLD_NUGGET));
                    }
                }
            }
        }
        if (world instanceof ServerLevel serverLevel) {
            if (killed instanceof Villager villager) {
                if (villager.hasEffect(GoetyEffects.CLIMBING)) {
                    ZombieVillager zombievillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
                    if (zombievillager != null) {
                        zombievillager.finalizeSpawn(serverLevel,
                                serverLevel.getCurrentDifficultyAt(zombievillager.blockPosition()),
                                MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true));
                        zombievillager.setVillagerData(villager.getVillagerData());
                        zombievillager.setGossips(villager.getGossips().store(NbtOps.INSTANCE));
                        zombievillager.setTradeOffers(villager.getOffers());
                        zombievillager.setVillagerXp(villager.getVillagerXp());
                        net.neoforged.neoforge.event.EventHooks.onLivingConvert(villager, zombievillager);
                        if (!zombievillager.isSilent()) {
                            serverLevel.levelEvent((Player) null, 1026, zombievillager.blockPosition(), 0);
                        }
                    }
                }
            }
            if (killed instanceof AbstractIllager illager) {
                if (!illager.getType().getDescriptionId().contains("magispeller")
                        && !illager.getType().getDescriptionId().contains("faker")
                        && !illager.getType().getDescriptionId().contains("freakager")
                        && !illager.getType().getDescriptionId().contains("spiritcaller")) {
                    for (Apostle apostle : world.getEntitiesOfClass(Apostle.class,
                            illager.getBoundingBox().inflate(32))) {
                        if (apostle.hasLineOfSight(illager)) {
                            Damned damned = new Damned(ModEntityType.DAMNED.get(), world);
                            damned.moveTo(illager.blockPosition().below(2), apostle.getYHeadRot(), apostle.getXRot());
                            damned.setTrueOwner(apostle);
                            damned.finalizeSpawn(serverLevel,
                                    serverLevel.getCurrentDifficultyAt(damned.blockPosition()),
                                    MobSpawnType.MOB_SUMMONED, null);
                            if (illager.hasCustomName()) {
                                damned.setCustomName(illager.getCustomName());
                            }
                            damned.setHuman(false);
                            if (apostle.getTarget() != null) {
                                damned.setTarget(apostle.getTarget());
                            }
                            damned.setLimitedLife(100);
                            ServerParticleUtil.addParticlesAroundSelf(serverLevel, ModParticleTypes.BIG_FIRE.get(),
                                    damned);
                            world.addFreshEntity(damned);
                        }
                    }
                }
            }
        }
        if (killer instanceof Player player) {
            if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                Entity entity = event.getSource().getDirectEntity();
                if (entity instanceof Fangs) {
                    if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()) {
                        int enchantment = EnchantmentHelper.getItemEnchantmentLevel(world.registryAccess().holderOrThrow(ModEnchantments.WANTING.getKey()),
                                CuriosFinder.findRing(player));
                        if (enchantment >= 3) {
                            if (world.random.nextFloat() <= (enchantment / 9.0F)) {
                                if (killed.getType() == EntityType.SKELETON) {
                                    killed.spawnAtLocation(new ItemStack(Items.SKELETON_SKULL));
                                }
                                if (killed.getType() == EntityType.ZOMBIE) {
                                    killed.spawnAtLocation(new ItemStack(Items.ZOMBIE_HEAD));
                                }
                                if (killed.getType() == EntityType.CREEPER) {
                                    killed.spawnAtLocation(new ItemStack(Items.CREEPER_HEAD));
                                }
                                if (killed.getType() == EntityType.WITHER_SKELETON) {
                                    killed.spawnAtLocation(new ItemStack(Items.WITHER_SKELETON_SKULL));
                                }
                                if (killed.getType() == EntityType.PIGLIN) {
                                    killed.spawnAtLocation(new ItemStack(Items.PIGLIN_HEAD));
                                }
                                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.TallSkullDrops, false)) {
                                    if (killed instanceof Villager || killed instanceof AbstractIllager) {
                                        killed.spawnAtLocation(new ItemStack(ModBlocks.TALL_SKULL_ITEM.get()));
                                    }
                                    if (killed instanceof Witch || (killed instanceof Cultist
                                            && killed.getType() != ModEntityType.APOSTLE.get())) {
                                        killed.spawnAtLocation(new ItemStack(ModBlocks.TALL_SKULL_ITEM.get()));
                                    }
                                }
                            }
                            if (killed instanceof Player player1) {
                                CompoundTag tag = new CompoundTag();
                                tag.putString("SkullOwner", player1.getDisplayName().getString());
                                ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                                // head.setTag(tag); // TODO: Fix 1.21 DataComponents
                                killed.spawnAtLocation(head);
                            }
                        }
                    }
                }
                if (killed.getType() == EntityType.SPIDER) {
                    if (CuriosFinder.hasCurio(player, itemStack -> itemStack.getItem() instanceof WarlockGarmentItem)) {
                        if (world.random.nextFloat() <= 0.075F) {
                            for (int i = 0; i < (world.random.nextInt(2) + 1); ++i) {
                                killed.spawnAtLocation(new ItemStack(ModItems.SPIDER_EGG.get()));
                            }
                        }
                    }
                }
            }
        }
        if (killer instanceof WitherNecromancer necromancer) {
            MobUtil.createWitherRose(killed, necromancer);
        }
        /*
         * if (killer instanceof LivingEntity livingEntity){
         * net.minecraft.network.chat.Component deathMessage =
         * killed.getCombatTracker().getDeathMessage();
         * livingEntity.sendSystemMessage(deathMessage);
         * }
         */
        if (!event.isCanceled()) {
            MiscCapHelper.setFreezing(killed, 0);
            MiscCapHelper.setShields(killed, 0);
            MiscCapHelper.setShieldTime(killed, 0);
            MiscCapHelper.setShakeTime(killed, 0);
        }
    }

    @SubscribeEvent
    public static void ExperienceEvents(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        int exp = event.getDroppedExperience();
        if (player != null) {
            if (CuriosFinder.hasCurio(player, ModItems.RING_OF_THIRST.get())) {
                int i = ItemHelper.repairPlayerItems(player, exp);
                if (i > 0) {
                    player.giveExperiencePoints(i);
                }
                event.setCanceled(true);
            }
        }
    }

    // TODO(1.21): LootingLevelEvent no longer exists. If still needed, port this to
    // LivingDropsEvent / loot context hooks.
    // Removed @SubscribeEvent annotation since the event no longer exists
    // public static void SpellLoot(LootingLevelEvent event) {
    // }

    @SubscribeEvent
    public static void DropEvents(LivingDropsEvent event) {
        if (event.getEntity() != null) {
            LivingEntity living = event.getEntity();
            if (living instanceof Player player) {
                if (CuriosFinder.hasWitchSet(player)) {
                    if (living.level().getServer() != null) {
                        LootTable loottable = living.level().getServer().reloadableRegistries().getLootTable(ModLootTables.PLAYER_WITCH);
                        LootParams.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(), living);
                        LootParams ctx = lootcontext$builder.create(LootContextParamSets.ENTITY);
                        loottable.getRandomItems(ctx)
                                .forEach((loot) -> event.getDrops().add(ItemHelper.itemEntityDrop(living, loot)));
                    }
                }
                if (!living.level().isClientSide) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        for (LivingEntity livingEntity : serverPlayer.level().getEntitiesOfClass(LivingEntity.class,
                                serverPlayer.getBoundingBox().inflate(64.0D))) {
                            if (livingEntity instanceof GraveGolem graveGolem) {
                                if (graveGolem.getTrueOwner() == serverPlayer) {
                                    graveGolem.addDrops(event.getDrops());
                                    event.getDrops().clear();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (living instanceof SpellcasterIllager || living instanceof Witch || living instanceof Cultist) {
                if (living.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    if (false) { // FIXME: MobUtil.getMobType(living).contains(ConstantPaths.structureMob())) {
                        float chance = 0.025F;
                        chance += (float) 0 / 100;
                        if (living.level().random.nextFloat() <= chance) {
                            event.getDrops().add(ItemHelper.itemEntityDrop(living,
                                    new ItemStack(ModItems.FORBIDDEN_FRAGMENT.get())));
                        }
                    }
                }
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.TallSkullDrops, false)) {
                if (living.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    if (living instanceof AbstractVillager || living instanceof Prisoner
                            || living instanceof AbstractIllager || living instanceof Witch
                            || living instanceof Cultist) {
                        if (living.level().getServer() != null) {
                            LootTable loottable = living.level().getServer().reloadableRegistries().getLootTable(ModLootTables.TALL_SKULL);
                            LootParams.Builder lootcontext$builder = MobUtil.createLootContext(event.getSource(),
                                    living);
                            LootParams lootparams = lootcontext$builder.create(LootContextParamSets.ENTITY);
                            loottable.getRandomItems(lootparams)
                                    .forEach((loot) -> event.getDrops().add(ItemHelper.itemEntityDrop(living, loot)));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void KnockBackEvents(LivingKnockBackEvent event) {
        LivingEntity knocked = event.getEntity();
        DamageSource lastDamage = knocked.getLastDamageSource();
        if (lastDamage != null) {
            if (lastDamage instanceof NoKnockBackDamageSource) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void addVillagerTrade(VillagerTradesEvent event) {
        ModTradeUtil.addVillagerTrades(event, VillagerProfession.CARTOGRAPHER, 3,
                new ModTradeUtil.TreasureMapForEmeralds(14, ModStructureTags.CRYPT, "filled_map.goety.crypt",
                        net.minecraft.world.level.saveddata.maps.MapDecorationTypes.WOODLAND_MANSION, 12, 10));
    }

    @SubscribeEvent
    public static void addWanderTrade(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();
        genericTrades.add(new ModTradeUtil.ItemsForEmeralds(ModItems.JADE.get(), 1, 64, 16));
        genericTrades.add(new ModTradeUtil.ItemsForEmeralds(ModBlocks.WINDSWEPT_SAPLING.get(), 5, 1, 8));
        genericTrades.add(new ModTradeUtil.ItemsForEmeralds(ModBlocks.PINE_SAPLING.get(), 5, 1, 8));
        rareTrades.add(new ModTradeUtil.TreasureMapForEmeralds(8, ModStructureTags.OMINOUS_BLACKSMITH,
                "filled_map.goety.ominous_blacksmith", net.minecraft.world.level.saveddata.maps.MapDecorationTypes.TARGET_X, 12, 10));
        rareTrades.add(new ModTradeUtil.TreasureMapForEmeralds(8, ModStructureTags.WIND_SHRINE,
                "filled_map.goety.wind_shrine", net.minecraft.world.level.saveddata.maps.MapDecorationTypes.TARGET_X, 12, 10));
        rareTrades.add(new ModTradeUtil.TreasureMapForEmeralds(8, ModStructureTags.BLIGHTED_SHACK,
                "filled_map.goety.blighted_shack", net.minecraft.world.level.saveddata.maps.MapDecorationTypes.WOODLAND_MANSION, 12, 10));
    }

    @SubscribeEvent
    public static void LightningStruckEvent(EntityStruckByLightningEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        if (level instanceof ServerLevel serverLevel) {
            if (entity instanceof WanderingTrader trader) {
                boolean hasConverted = false;
                if (event.getLightning().getCause() != null) {
                    if (CuriosFinder.hasUnholySet(event.getLightning().getCause())) {
                        MaverickServant maverick = ModEntityType.MAVERICK_SERVANT.get().create(serverLevel);
                        if (maverick != null) {
                            maverick.moveTo(trader.getX(), trader.getY(), trader.getZ(), trader.getYRot(),
                                    trader.getXRot());
                            maverick.setTrueOwner(event.getLightning().getCause());
                            maverick.finalizeSpawn(serverLevel,
                                    serverLevel.getCurrentDifficultyAt(maverick.blockPosition()),
                                    MobSpawnType.CONVERSION, null);
                            maverick.setNoAi(trader.isNoAi());
                            if (trader.hasCustomName()) {
                                maverick.setCustomName(trader.getCustomName());
                                maverick.setCustomNameVisible(trader.isCustomNameVisible());
                            }

                            maverick.setPersistenceRequired();
                            net.neoforged.neoforge.event.EventHooks.onLivingConvert(trader,
                                    maverick);
                            serverLevel.addFreshEntityWithPassengers(maverick);
                            hasConverted = true;
                            trader.discard();
                        }
                    }
                }
                if (!hasConverted) {
                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.TraderConvertMaverick, false)) {
                        if (serverLevel.getDifficulty() != Difficulty.PEACEFUL
                                && net.neoforged.neoforge.event.EventHooks.canLivingConvert(trader,
                                        ModEntityType.MAVERICK.get(), (timer) -> {
                                        })) {
                            Maverick maverick = ModEntityType.MAVERICK.get().create(serverLevel);
                            if (maverick != null) {
                                maverick.moveTo(trader.getX(), trader.getY(), trader.getZ(), trader.getYRot(),
                                        trader.getXRot());
                                maverick.finalizeSpawn(serverLevel,
                                        serverLevel.getCurrentDifficultyAt(maverick.blockPosition()),
                                        MobSpawnType.CONVERSION, null);
                                maverick.setNoAi(trader.isNoAi());
                                if (trader.hasCustomName()) {
                                    maverick.setCustomName(trader.getCustomName());
                                    maverick.setCustomNameVisible(trader.isCustomNameVisible());
                                }

                                maverick.setPersistenceRequired();
                                net.neoforged.neoforge.event.EventHooks.onLivingConvert(trader,
                                        maverick);
                                serverLevel.addFreshEntityWithPassengers(maverick);
                                trader.discard();
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void ExplosionStartEvent(ExplosionEvent.Start event) {
        /*
         * Explosion explosion = event.getExplosion();
         * if (explosion != null && !(explosion instanceof LootingExplosion)) {
         * if (explosion.getIndirectSourceEntity() instanceof Player player){
         * if (CuriosFinder.hasWanting(player)){
         * ExplosionUtil.lootExplode(explosion.level(), explosion.getExploder(),
         * explosion.x, explosion.y, explosion.z, explosion.radius, explosion.fire,
         * explosion.blockInteraction, LootingExplosion.Mode.LOOT);
         * event.setCanceled(true);
         * }
         * }
         * }
         */
    }

    @SubscribeEvent
    public static void ExplosionDetonateEvent(ExplosionEvent.Detonate event) {
        if (event.getExplosion() != null) {
            event.getAffectedEntities().removeIf(entity -> (entity instanceof ItemEntity
                    && ((ItemEntity) entity).getItem().getItem() == ModItems.UNHOLY_BLOOD.get()));
            event.getAffectedEntities().removeIf(entity -> (entity instanceof ItemEntity
                    && ((ItemEntity) entity).getItem().getItem() == ModBlocks.NIGHT_BEACON_ITEM.get()));
        }
    }

    @SubscribeEvent
    public static void ProjectileImpactEvent(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof AbstractArrow arrowEntity) {
            if (arrowEntity.getTags().contains(ConstantPaths.rainArrow())) {
                arrowEntity.discard();
            }
        }
    }

    @SubscribeEvent
    public static void SleepEvents(CanPlayerSleepEvent event) {
        if (event.getEntity() != null) {
            if (!event.getEntity().isCreative()) {
                double d0 = 8.0D;
                double d1 = 5.0D;
                Vec3 vec3 = Vec3.atBottomCenterOf(event.getPos());
                List<LivingEntity> list = event.getLevel().getEntitiesOfClass(LivingEntity.class,
                        new AABB(vec3.x() - d0, vec3.y() - d1, vec3.z() - d0, vec3.x() + d0, vec3.y() + d1,
                                vec3.z() + d0),
                        (p_9062_) -> {
                            return p_9062_ instanceof IOwned owned
                                    && owned.preventsSleep(event.getEntity());
                        });
                if (!list.isEmpty()) {
                    event.setProblem(Player.BedSleepingProblem.NOT_SAFE);
                }
            }
        }
    }

    @SubscribeEvent
    public static void FurnaceBurnItems(FurnaceFuelBurnTimeEvent event) {
        if (!event.getItemStack().isEmpty()) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.is(ModBlocks.ROTTEN_BOOKSHELF.get().asItem())
                    || itemStack.is(ModBlocks.WINDSWEPT_BOOKSHELF.get().asItem())
                    || itemStack.is(ModBlocks.PINE_BOOKSHELF.get().asItem())
                    || (itemStack.getItem() instanceof BlockItem blockItem
                            && blockItem.getBlock() instanceof ModChestBlock
                            && blockItem.getBlock().defaultBlockState().ignitedByLava())
                    || itemStack.is(ModBlocks.COMPACTED_WINDSWEPT_PLANKS.get().asItem())
                    || itemStack.is(ModBlocks.COMPACTED_PINE_PLANKS.get().asItem())
                    || itemStack.is(ModBlocks.THATCHED_PINE_PLANKS.get().asItem())
                    || itemStack.is(ModBlocks.SKY_WOOD_PLANKS.get().asItem())
                    || itemStack.is(ModBlocks.OVERGROWN_ROOTS.get().asItem())) {
                event.setBurnTime(300);
            }
            if (itemStack.is(ModBlocks.WITCH_POLE.get().asItem())) {
                event.setBurnTime(200);
            }
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) -> new za.co.infernos.goety.common.items.handler.BrewBagItemHandler(stack), ModItems.BREW_BAG.get());
        event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) -> new za.co.infernos.goety.common.items.handler.WitchStaffItemHandler(stack), ModItems.WITCH_STAFF.get());
        event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) -> new za.co.infernos.goety.common.items.handler.FocusBagItemHandler(stack, 11), ModItems.FOCUS_BAG.get());
    }
}
