package za.co.infernos.goety.common.events;

import za.co.infernos.goety.utils.MobType;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.entities.IGolem;
import za.co.infernos.goety.api.entities.ally.IServant;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.SnapWartsBlock;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.illager.AllyIrk;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.world.structures.ModStructureTags;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import static net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent.LivingTargetType.MOB_TARGET;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class RobeEvents {

    @SubscribeEvent
    public static void LivingEffects(EntityTickEvent.Post event){
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) {
            return;
        }
        if (livingEntity != null) {
            if (!livingEntity.level().isClientSide) {
                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.CompatMinionHeal, false)) {
                    if (livingEntity instanceof OwnableEntity ownable && !(livingEntity instanceof IServant)) {
                        if (ownable.getOwnerUUID() != null) {
                            Player owner = livingEntity.level().getPlayerByUUID(ownable.getOwnerUUID());
                            if (owner != null && MiscCapHelper.getNoHealTime(livingEntity) <= 0) {
                                ServantUtil.healServant(owner, livingEntity);
                            }
                        }
                    }
                }
                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.NecroSetDebuff, false) || za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.NamelessSetDebuff, false)) {
                    if (MobUtil.getOwner(livingEntity) != null){
                        if (!livingEntity.getType().is(EntityTypeTags.UNDEAD)
                                && !(livingEntity instanceof AbstractGolem)
                                && !(livingEntity instanceof IGolem)
                                && !livingEntity.getType().is(ModTags.EntityTypes.NECRO_NO_DEBUFF)) {
                            boolean flag = false;
                            int amp = 0;
                            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.NecroSetDebuff, false)) {
                                if (CuriosFinder.hasNecroCrown(MobUtil.getOwner(livingEntity)) || CuriosFinder.hasNecroCape(MobUtil.getOwner(livingEntity))) {
                                    if (CuriosFinder.hasNecroSet(MobUtil.getOwner(livingEntity))) {
                                        amp += 2;
                                    }
                                    flag = true;
                                }
                            }
                            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.NamelessSetDebuff, false)) {
                                if (CuriosFinder.hasNamelessCrown(MobUtil.getOwner(livingEntity)) || CuriosFinder.hasNamelessCrown(MobUtil.getOwner(livingEntity))) {
                                    if (CuriosFinder.hasNamelessSet(MobUtil.getOwner(livingEntity))) {
                                        amp += 2;
                                    }
                                    flag = true;
                                }
                            }
                            if (flag) {
                                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1 + amp));
                                livingEntity.addEffect(new MobEffectInstance(GoetyEffects.SAPPED, 20, 2 + amp));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakingBlock(BlockEvent.BreakEvent event){
        Player player = event.getPlayer();
        if (CuriosFinder.hasWarlockRobe(player)){
            if (event.getState().is(ModBlocks.SNAP_WARTS.get()) && event.getState().getValue(SnapWartsBlock.AGE) >= 2){
                if (!player.level().isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        if (player.getRandom().nextFloat() <= 0.25F) {
                            Block.dropResources(event.getState(), player.level(), event.getPos(), null, player, player.getUseItem());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void HurtEvent(LivingIncomingDamageEvent event){
        LivingEntity victim = event.getEntity();
        if (CuriosFinder.hasFrostRobes(victim)){
            if (ModDamageSource.freezeAttacks(event.getSource()) || event.getSource().is(DamageTypeTags.IS_FREEZING)){
                float resistance = 1.0F - (za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.FrostRobeResistance, 0) / 100.0F);
                event.setAmount(event.getAmount() * resistance);
            }
        }
        if (CuriosFinder.hasAbyssRobes(victim)){
            if (event.getSource().is(DamageTypeTags.IS_DROWNING)){
                if (victim.getAirSupply() <= 0){
                    victim.setAirSupply(20);
                }
                event.setAmount(event.getAmount() * 0.5F);
            }
        }
        if (CuriosFinder.hasNetherRobe(victim)){
            float resistance = 1.0F - (za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.NetherRobeResistance, 0) / 100.0F);
            if (resistance <= 0.0F){
                event.setCanceled(true);
            } else {
                if (event.getSource().is(DamageTypeTags.IS_FIRE) || ModDamageSource.isMagicFire(event.getSource())){
                    event.setAmount(event.getAmount() * resistance);
                }
                if (ModDamageSource.hellfireAttacks(event.getSource())){
                    resistance = Math.max(0.75F, resistance);
                    event.setAmount(event.getAmount() * resistance);
                }
            }
        }
        if (CuriosFinder.hasCurio(victim, ModItems.GRAND_TURBAN.get())){
            if (victim instanceof Player player) {
                if (SEHelper.getSoulsAmount(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.ItemsRepairAmount, 0))) {
                    int irks = victim.level().getEntitiesOfClass(AllyIrk.class, victim.getBoundingBox().inflate(32)).size();
                    if ((victim.level().random.nextBoolean() || victim.getHealth() <= victim.getMaxHealth() / 2) && irks < 16) {
                        AllyIrk irk = new AllyIrk(ModEntityType.IRK_SERVANT.get(), victim.level());
                        irk.setPos(victim.getX(), victim.getY(), victim.getZ());
                        irk.setLimitedLife(MobUtil.getSummonLifespan(victim.level()));
                        irk.setTrueOwner(victim);
                        if (victim.level().addFreshEntity(irk)) {
                            SEHelper.decreaseSouls(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.ItemsRepairAmount, 0));
                        }
                    }
                }
            }
        }
        if (CuriosFinder.hasCurio(victim, ModItems.GRAND_ROBE.get())){
            if (MobUtil.isSpellCasting(victim)){
                event.setAmount(event.getAmount() / 2.0F);
            }
        }
        float damage = event.getAmount();
        if (CuriosFinder.hasUnholyHat(victim)){
            if (victim.level().dimension() == Level.NETHER){
                damage /= 2.0F;
            }
            if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)){
                damage = Math.min(damage, za.co.infernos.goety.utils.ConfigHelper.getFloat(AttributesConfig.ApostleDamageCap, 1.0F));
            }
            event.setAmount(damage);
        }
        if (CuriosFinder.hasUnholyRobe(victim)){
            float resistance = 1.0F - (za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.NetherRobeResistance, 0) / 100.0F);
            if (ModDamageSource.hellfireAttacks(event.getSource())){
                resistance = Math.max(0.75F, resistance);
                damage *= resistance;
            }
            event.setAmount(damage);
        }
        if(CuriosFinder.hasCurio(victim, ModItems.STORM_ROBE.get())){
            if (ModDamageSource.shockAttacks(event.getSource()) || event.getSource().is(DamageTypes.LIGHTNING_BOLT)){
                float resistance = 1.0F - (za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.StormRobeResistance, 0) / 100.0F);
                event.setAmount(event.getAmount() * resistance);
            }
            if (event.getSource().is(DamageTypes.LIGHTNING_BOLT)){
                victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300));
            }
        }
        if (CuriosFinder.hasWitchRobe(victim) || CuriosFinder.hasWarlockRobe(victim)){
            if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)){
                if (!(LichdomHelper.isLich(victim) && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.LichMagicResist, false))){
                    float resistance = 1.0F - (za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.WitchRobeResistance, 0) / 100.0F);
                    if (CuriosFinder.hasWarlockRobe(victim)){
                        resistance = 1.0F - (za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.WarlockRobeResistance, 0) / 100.0F);
                    }
                    event.setAmount(event.getAmount() * resistance);
                }
            }
        }
    }

    @SubscribeEvent
    public static void AttackEvent(LivingIncomingDamageEvent event){
        LivingEntity victim = event.getEntity();
        Entity source = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();
        if (!event.getEntity().level().isClientSide) {
            if (event.getSource().is(DamageTypeTags.IS_FIRE)){
                LivingEntity source1 = null;
                Entity direct1 = direct;
                if (source instanceof LivingEntity living1) {
                    source1 = living1;
                } else if (MobUtil.getOwner(source) != null) {
                    source1 = MobUtil.getOwner(source);
                }
                if (event.getSource() instanceof NoKnockBackDamageSource damageSource){
                    if (damageSource.getOwner() instanceof LivingEntity living1) {
                        source1 = living1;
                    } else if (MobUtil.getOwner(damageSource.getOwner()) != null) {
                        source1 = MobUtil.getOwner(damageSource.getOwner());
                    }
                    direct1 = damageSource.getDirectEntity();
                }
                if (CuriosFinder.hasNetherRobe(source1)){
                    if (victim.isInvulnerableTo(event.getSource()) || victim.hasEffect(MobEffects.FIRE_RESISTANCE)){
                        DamageSource damageSource = ModDamageSource.magicFireBreath(direct1, source1);
                        if (CuriosFinder.hasUnholyRobe(source1)){
                            damageSource = ModDamageSource.hellfire(direct1, source1);
                        }
                        victim.hurt(damageSource, event.getAmount());
                        event.setCanceled(true);
                    }
                }
            }
            if (event.getAmount() > 0.0F) {
                if (za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.VoidRobeTeleportChance, 0) > 0) {
                    if (CuriosFinder.hasVoidRobe(victim)) {
                        if (!victim.isInvulnerableTo(event.getSource()) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(victim)) {
                            float chance = za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.VoidRobeTeleportChance, 0) / 100.0F;
                            if (victim.getRandom().nextFloat() <= chance) {
                                double d0 = victim.getX() + (victim.getRandom().nextDouble() - 0.5D) * za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.VoidRobeTeleportDistance, 0);
                                double d1 = victim.getY();
                                double d2 = victim.getZ() + (victim.getRandom().nextDouble() - 0.5D) * za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.VoidRobeTeleportDistance, 0);
                                if (MobUtil.teleport(victim, d0, d1, d2)) {
                                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(ItemConfig.VoidRobeTeleportDamageCancel, false)) {
                                        event.setCanceled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (source instanceof MagmaCube magmaCube){
            if (CuriosFinder.neutralNetherSet(victim)){
                if (magmaCube.getLastHurtByMob() != victim) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void TargetEvents(LivingChangeTargetEvent event) {
        LivingEntity attacker = event.getEntity();
        LivingEntity target = event.getOriginalAboutToBeSetTarget();
        if (attacker instanceof Mob mobAttacker) {
            if (target != null) {
                if (target instanceof Player) {
                    if (MobUtil.isWitchType(mobAttacker)) {
                        if (CuriosFinder.isWitchFriendly(target)) {
                            if (mobAttacker.getLastHurtByMob() != target) {
                                if (event.getTargetType() == MOB_TARGET) {
                                    event.setNewAboutToBeSetTarget(null);
                                } else {
                                    event.setCanceled(true);
                                }
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                    if (CuriosFinder.neutralNecroSet(target) || CuriosFinder.neutralNamelessSet(target)) {
                        boolean undead = CuriosFinder.validNecroUndead(mobAttacker);
                        if (target.level() instanceof ServerLevel serverLevel) {
                            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HostileCryptUndead, false)) {
                                if (BlockFinder.findStructure(serverLevel, target.blockPosition(), ModStructureTags.NECRO_HOSTILE)
                                        && !CuriosFinder.neutralNamelessSet(target)) {
                                    undead = false;
                                }
                            }
                        }
                        if (undead || (CuriosFinder.neutralNamelessSet(target) && CuriosFinder.validNamelessUndead(mobAttacker))) {
                            if (mobAttacker.getLastHurtByMob() != target) {
                                if (event.getTargetType() == MOB_TARGET) {
                                    event.setNewAboutToBeSetTarget(null);
                                } else {
                                    event.setCanceled(true);
                                }
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                    if (CuriosFinder.neutralAbyssSet(target)) {
                        if (CuriosFinder.validAbyssMob(mobAttacker)) {
                            if (mobAttacker.getLastHurtByMob() != target) {
                                if (event.getTargetType() == MOB_TARGET) {
                                    event.setNewAboutToBeSetTarget(null);
                                } else {
                                    event.setCanceled(true);
                                }
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                    if (CuriosFinder.neutralFrostSet(target)) {
                        if (CuriosFinder.validFrostMob(mobAttacker)) {
                            if (mobAttacker.getLastHurtByMob() != target) {
                                if (event.getTargetType() == MOB_TARGET) {
                                    event.setNewAboutToBeSetTarget(null);
                                } else {
                                    event.setCanceled(true);
                                }
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                    if (CuriosFinder.neutralNetherSet(target)) {
                        if (CuriosFinder.validNetherMob(mobAttacker)) {
                            if (mobAttacker.getLastHurtByMob() != target) {
                                if (event.getTargetType() == MOB_TARGET) {
                                    event.setNewAboutToBeSetTarget(null);
                                } else {
                                    event.setCanceled(true);
                                }
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                    if (CuriosFinder.hasUnholyRobe(target) || CuriosFinder.hasUnholyHat(target)) {
                        if (mobAttacker instanceof Ghast || mobAttacker instanceof Blaze || mobAttacker instanceof MagmaCube) {
                            if (event.getTargetType() == MOB_TARGET) {
                                event.setNewAboutToBeSetTarget(null);
                            } else {
                                event.setCanceled(true);
                            }
                        }
                    }
                    if (CuriosFinder.neutralVoidSet(target)) {
                        boolean ender = CuriosFinder.validVoidMob(mobAttacker);
                        if (target.level() instanceof ServerLevel serverLevel) {
                            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HostileTerminalEnder, false)) {
                                if (BlockFinder.findStructure(serverLevel, target.blockPosition(), ModStructureTags.VOID_HOSTILE)) {
                                    ender = false;
                                }
                            }
                        }
                        if (ender) {
                            if (mobAttacker.getLastHurtByMob() != target) {
                                if (event.getTargetType() == MOB_TARGET) {
                                    event.setNewAboutToBeSetTarget(null);
                                } else {
                                    event.setCanceled(true);
                                }
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                    if (CuriosFinder.neutralWildSet(target)) {
                        if (CuriosFinder.validWildMob(mobAttacker)) {
                            if (mobAttacker.getLastHurtByMob() != target) {
                                if (event.getTargetType() == MOB_TARGET) {
                                    event.setNewAboutToBeSetTarget(null);
                                } else {
                                    event.setCanceled(true);
                                }
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                    if (CuriosFinder.hasWarlockRobe(event.getOriginalAboutToBeSetTarget())) {
                        if (MobUtil.getMobType(mobAttacker) == MobType.ARTHROPOD){
                            if (mobAttacker.getLastHurtByMob() != target) {
                                if (event.getTargetType() == MOB_TARGET) {
                                    event.setNewAboutToBeSetTarget(null);
                                } else {
                                    event.setCanceled(true);
                                }
                            } else {
                                mobAttacker.setLastHurtByMob(target);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void VisibilityEvent(LivingEvent.LivingVisibilityEvent event){
        LivingEntity entity = event.getEntity();
        if (event.getLookingEntity() instanceof LivingEntity && entity instanceof Player) {
            if (entity.isInvisible()){
                if (CuriosFinder.hasCurio(entity, ModItems.ILLUSION_ROBE.get())){
                    event.modifyVisibility(0.0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void OnLivingFall(LivingFallEvent event){
        if (CuriosFinder.hasWindyRobes(event.getEntity())){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void PotionApplicationEvents(MobEffectEvent.Applicable event){
        if (event.getEffectInstance().getEffect() == GoetyEffects.FREEZING.get()){
            if (CuriosFinder.hasFrostRobes(event.getEntity())){
                if (event instanceof net.neoforged.bus.api.ICancellableEvent cancellable) {
                    cancellable.setCanceled(true);
                }
            }
        }
        if (event.getEffectInstance().getEffect() == GoetyEffects.BURN_HEX.get()){
            if (CuriosFinder.hasUnholyHat(event.getEntity()) || CuriosFinder.hasUnholyRobe(event.getEntity())){
                if (event instanceof net.neoforged.bus.api.ICancellableEvent cancellable) {
                    cancellable.setCanceled(true);
                }
            }
        }
        if (event.getEffectInstance().getEffect() == MobEffects.POISON
                || event.getEffectInstance().getEffect() == GoetyEffects.ACID_VENOM.get()) {
            if (CuriosFinder.hasWildRobe(event.getEntity())) {
                if (event instanceof net.neoforged.bus.api.ICancellableEvent cancellable) {
                    cancellable.setCanceled(true);
                }
            }
        }
        if (event.getEffectInstance().getEffect() == GoetyEffects.VOID_TOUCHED.get()) {
            if (CuriosFinder.hasVoidRobe(event.getEntity())) {
                if (event instanceof net.neoforged.bus.api.ICancellableEvent cancellable) {
                    cancellable.setCanceled(true);
                }
            }
        }
        if (event.getEffectInstance().getEffect() == MobEffects.BLINDNESS){
            if (CuriosFinder.hasIllusionRobe(event.getEntity())){
                if (event instanceof net.neoforged.bus.api.ICancellableEvent cancellable) {
                    cancellable.setCanceled(true);
                }
            }
        }
    }
}
