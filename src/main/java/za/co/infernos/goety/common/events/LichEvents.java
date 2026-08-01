package za.co.infernos.goety.common.events;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.client.particles.LichShockwaveParticleOption;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.compat.iron.IronAttributes;
import za.co.infernos.goety.compat.iron.IronLoaded;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import java.util.UUID;

import static net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent.LivingTargetType.MOB_TARGET;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class LichEvents {

    @SubscribeEvent
    public static void onPlayerLichdom(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level world = player.level();
        if (LichdomHelper.isLich(player)) {
            player.getFoodData().setFoodLevel(17);
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            boolean burn = MobUtil.isInSunlight(player) && !world.isRaining();

            if (!player.level().isClientSide) {
                if (burn) {
                    ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                    if (!helmet.isEmpty()) {
                        if (!player.isCreative()) {
                            if (!MobUtil.isFireImmune(player)) {
                                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichDamageHelmet, false)) {
                                    if (helmet.isDamageableItem()) {
                                        ItemHelper.hurtAndBreak(helmet, world.random.nextInt(2), player);
                                    }
                                }
                            }
                        }
                        burn = false;
                    }
                    if (burn) {
                        if (!MobUtil.isFireImmune(player)) {
                            player.igniteForSeconds(8);
                        }
                    }
                }
            }

            player.getActiveEffects().removeIf(effectInstance -> !EffectsUtil.canAffectLich(effectInstance, world));
            if (player.hasEffect(GoetyEffects.SOUL_HUNGER)) {
                if (SEHelper.getSoulsAmount(player, za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.MaxSouls, 0))) {
                    player.removeEffect(GoetyEffects.SOUL_HUNGER);
                }
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichSoulHeal, false)) {
                if (!(player.isOnFire() && !MobUtil.isFireImmune(player)) && LichdomHelper.smited(player) <= 0) {
                    if (player.getHealth() < player.getMaxHealth()) {
                        if (player.tickCount % (MathHelper.secondsToTicks(za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.LichHealSeconds, 0)) + 1) == 0
                                && SEHelper.getSoulsAmount(player, za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.LichHealCost, 0))) {
                            player.heal(za.co.infernos.goety.utils.ConfigHelper.getFloat(MainConfig.LichHealAmount, 1.0F));
                            Vec3 vector3d = player.getDeltaMovement();
                            if (!player.level().isClientSide) {
                                ServerLevel serverWorld = (ServerLevel) player.level();
                                serverWorld.sendParticles(ParticleTypes.SCULK_SOUL, player.getRandomX(0.5D),
                                        player.getRandomY(), player.getRandomZ(0.5D), 0, vector3d.x * -0.2D, 0.1D,
                                        vector3d.z * -0.2D, 0.5F);
                            }
                            SEHelper.decreaseSouls(player, za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.LichHealCost, 0));
                        }
                    }
                }
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichVillagerHate, false) && player.tickCount % 20 == 0) {
                for (Villager villager : player.level().getEntitiesOfClass(Villager.class,
                        player.getBoundingBox().inflate(16.0D))) {
                    if (villager.getPlayerReputation(player) > -200 && villager.getPlayerReputation(player) < 100) {
                        villager.getGossips().add(player.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
                    }
                }
                for (IronGolem ironGolem : player.level().getEntitiesOfClass(IronGolem.class,
                        player.getBoundingBox().inflate(16.0D))) {
                    if (!ironGolem.isPlayerCreated() && ironGolem.getTarget() != player
                            && TargetingConditions.forCombat().range(16.0F).test(ironGolem, player)) {
                        ironGolem.setTarget(player);
                    }
                }
            }
            if (LichdomHelper.isInLichMode(player)) {
                if (player.tickCount % 5 == 0) {
                    if (world.isClientSide) {
                        world.addParticle(ModParticleTypes.LICH.get(), player.getRandomX(0.5D), player.getY(),
                                player.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                    }
                }
                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichModeSounds, false)) {
                    if (player.isAlive()) {
                        MiscCapHelper.doAmbientSoundTime(player);
                        if (MiscCapHelper.getAmbientSoundTime(player) > player.getRandom().nextInt(1000)) {
                            MiscCapHelper.setAmbientSoundTime(player, -MathHelper.secondsToTicks(4));
                            player.playSound(ModSounds.LICH_AMBIENT.get(), 1.0F, player.getVoicePitch());
                        }
                    }
                }
            }
            if (player.isAlive()) {
                if (!player.level().isClientSide) {
                    if (LichdomHelper.nightVision(player) && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichNightVision, false)) {
                        if (!player.hasEffect(MobEffects.NIGHT_VISION)) {
                            player.addEffect(
                                    new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, false, false, false));
                        }
                    } else {
                        if (player.hasEffect(MobEffects.NIGHT_VISION)) {
                            player.removeEffect(MobEffects.NIGHT_VISION);
                        }
                    }
                }
            }
            if (LichdomHelper.smited(player) > 0) {
                LichdomHelper.setSmited(player, LichdomHelper.smited(player) - 1);
            }
        } else {
            if (LichdomHelper.isInLichMode(player)) {
                LichdomHelper.setLichMode(player, false);
            }
            if (LichdomHelper.nightVision(player)) {
                LichdomHelper.setNightVision(player, false);
            }
        }

        if (IronLoaded.IRON_SPELLBOOKS.isLoaded()) {
            AttributeInstance bloodResist = player.getAttribute(IronAttributes.BLOOD_MAGIC_RESIST);
            AttributeModifier attributemodifier = new AttributeModifier(
                    Goety.location("lich_blood_resistance"), 0.5F,
                    AttributeModifier.Operation.ADD_VALUE);
            if (bloodResist != null) {
                if (LichdomHelper.isLich(player)) {
                    if (!bloodResist.hasModifier(attributemodifier.id())) {
                        bloodResist.addPermanentModifier(attributemodifier);
                    }
                } else {
                    if (bloodResist.hasModifier(attributemodifier.id())) {
                        bloodResist.removeModifier(attributemodifier.id());
                    }
                }
            }

            AttributeInstance holyResist = player.getAttribute(IronAttributes.HOLY_MAGIC_RESIST);
            AttributeModifier attributemodifier1 = new AttributeModifier(
                    Goety.location("lich_holy_weakness"), -0.5F,
                    AttributeModifier.Operation.ADD_VALUE);
            if (holyResist != null) {
                if (LichdomHelper.isLich(player)) {
                    if (!holyResist.hasModifier(attributemodifier1.id())) {
                        holyResist.addPermanentModifier(attributemodifier1);
                    }
                } else {
                    if (holyResist.hasModifier(attributemodifier1.id())) {
                        holyResist.removeModifier(attributemodifier1.id());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void SpecialPotionEffects(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player) {
            if (LichdomHelper.isLich(player)) {
                if (!EffectsUtil.canAffectLich(event.getEffectInstance(), player.level())) {
                    if (event instanceof net.neoforged.bus.api.ICancellableEvent cancellable) {
                        cancellable.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void UndeadFriendly(LivingChangeTargetEvent event) {
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichUndeadFriends, false)) {
            if (event.getEntity() instanceof Enemy) {

                if (event.getEntity().getType().is(net.minecraft.tags.EntityTypeTags.UNDEAD)
                        || event.getEntity().getType().is(ModTags.EntityTypes.LICH_NEUTRAL)) {
                    if (event.getNewAboutToBeSetTarget() != null) {
                        if (event.getNewAboutToBeSetTarget() instanceof Player player) {
                            if (LichdomHelper.isLich(player)) {
                                if (event.getTargetType() == LivingChangeTargetEvent.LivingTargetType.MOB_TARGET) {
                                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichPowerfulFoes, false)) {
                                        if (event.getEntity().getMaxHealth() <= za.co.infernos.goety.utils.ConfigHelper.getDouble(MainConfig.LichPowerfulFoesHealth, 1.0D)) {
                                            if (event instanceof LivingChangeTargetEvent changeTargetEvent) {
                                                if (changeTargetEvent.getNewAboutToBeSetTarget() instanceof Player targetPlayer) {
                                                    if (SEHelper.getAllyEntityTypes(targetPlayer).contains(event.getEntity().getType())) {
                                                        changeTargetEvent.setNewAboutToBeSetTarget(null);
                                                    }
                                                    if (event.getEntity() instanceof IOwned owned && owned.getTrueOwner() == targetPlayer) {
                                                        changeTargetEvent.setNewAboutToBeSetTarget(null);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (event instanceof LivingChangeTargetEvent changeTargetEvent) {
                                            if (changeTargetEvent.getNewAboutToBeSetTarget() != null) {
                                                if (changeTargetEvent.getNewAboutToBeSetTarget() instanceof Player targetPlayer) {
                                                    if (SEHelper.getAllyEntityTypes(targetPlayer).contains(event.getEntity().getType())) {
                                                        changeTargetEvent.setNewAboutToBeSetTarget(null);
                                                    }
                                                    if (event.getEntity() instanceof IOwned owned && owned.getTrueOwner() == targetPlayer) {
                                                        changeTargetEvent.setNewAboutToBeSetTarget(null);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (event.getEntity() instanceof NeutralMob) {
                                        if (event instanceof LivingChangeTargetEvent changeTargetEvent) {
                                            if (changeTargetEvent.getNewAboutToBeSetTarget() != null) {
                                                if (changeTargetEvent.getNewAboutToBeSetTarget() instanceof Player targetPlayer) {
                                                    if (SEHelper.getAllyEntityTypes(targetPlayer).contains(event.getEntity().getType())) {
                                                        changeTargetEvent.setNewAboutToBeSetTarget(null);
                                                    }
                                                    if (event.getEntity() instanceof IOwned owned && owned.getTrueOwner() == targetPlayer) {
                                                        changeTargetEvent.setNewAboutToBeSetTarget(null);
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
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (LichdomHelper.isLich(player)) {
                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichMagicResist, false)) {
                    if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
                        event.setAmount(event.getAmount() * 0.15F);
                    }
                }
                if (ModDamageSource.freezeAttacks(event.getSource())
                        || event.getSource().is(DamageTypeTags.IS_FREEZING)) {
                    event.setAmount(event.getAmount() / 2);
                }
                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichUndeadFriends, false)) {
                    if (CuriosFinder.hasUndeadSet(player) && event.getSource().getEntity() != null) {
                        if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker.isAlive()) {
                            for (Mob undead : player.level().getEntitiesOfClass(Mob.class,
                                    player.getBoundingBox().inflate(16))) {
                                if (undead != attacker) {
                                    if (undead.getType().is(EntityTypeTags.UNDEAD)) {
                                        if (undead.getTarget() != player) {
                                            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichPowerfulFoes, false)) {
                                                if (undead.getMaxHealth() <= za.co.infernos.goety.utils.ConfigHelper.getDouble(MainConfig.LichPowerfulFoesHealth, 100.0D)) {
                                                    undead.setLastHurtByMob(attacker);
                                                    undead.setTarget(attacker);
                                                }
                                            } else {
                                                undead.setLastHurtByMob(attacker);
                                                undead.setTarget(attacker);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (LichdomHelper.isInLichMode(player)) {
                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichModeSounds, false)) {
                        if (player.isAlive()) {
                            if (event.getAmount() > 0.0F) {
                                if (!player.level().isClientSide) {
                                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                            ModSounds.LICH_HURT.get(), player.getSoundSource(), 1.0F,
                                            player.getVoicePitch());
                                    MiscCapHelper.setAmbientSoundTime(player, -MathHelper.secondsToTicks(4));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof Player player) {
            if (LichdomHelper.isLich(player) && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichTouch, false)) {
                if (ModDamageSource.physicalAttacks(event.getSource()) && event.getEntity() != player) {
                    if (player.getMainHandItem().isEmpty()) {
                        event.getEntity().addEffect(new MobEffectInstance(GoetyEffects.FREEZING, 900));
                    }
                    if (!event.getEntity().getType().is(EntityTypeTags.UNDEAD)
                            && player.getMainHandItem().is(ModTags.Items.LICH_WITHER_ITEMS)) {
                        event.getEntity()
                                .addEffect(new MobEffectInstance(MobEffects.WITHER, MathHelper.secondsToTicks(5)));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (LichdomHelper.isLich(livingEntity)) {
            if (LichdomHelper.isInLichMode(livingEntity)) {
                if (!event.isCanceled()) {
                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichModeSounds, false)) {
                        Vec3 vec3 = livingEntity.position();
                        livingEntity.level().playSound(null, vec3.x, vec3.y, vec3.z, ModSounds.LICH_DEATH.get(),
                                livingEntity.getSoundSource(), 1.0F, livingEntity.getVoicePitch());
                    }
                    if (livingEntity.level() instanceof ServerLevel serverLevel) {
                        ColorUtil colorUtil = new ColorUtil(0x36e416);
                        serverLevel.sendParticles(new LichShockwaveParticleOption(colorUtil, 40, 20, 1, 100),
                                livingEntity.getX(), livingEntity.getY() + 0.5F, livingEntity.getZ(), 0, 0, 0, 0, 0.5F);
                    }
                }
            }
        }
    }

}
