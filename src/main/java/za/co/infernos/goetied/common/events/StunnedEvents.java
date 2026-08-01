package za.co.infernos.goetied.common.events;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.effects.GoetiedEffects;
import za.co.infernos.goetied.common.network.ModNetwork;
import za.co.infernos.goetied.common.network.server.SRemoveEffectPacket;
import za.co.infernos.goetied.init.ModTags;
import za.co.infernos.goetied.utils.SEHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.ICancellableEvent;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;

import static net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent.LivingTargetType.MOB_TARGET;

@EventBusSubscriber(modid = Goetied.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class StunnedEvents {

    private static boolean isStunned(@Nullable LivingEntity entity) {
        return entity != null && entity.isAlive() && (entity.hasEffect(GoetiedEffects.STUNNED)
                || (entity instanceof Player player && SEHelper.hasCamera(player)));
    }

    public static void cancelEvent(LivingEvent event){
        if (event instanceof ICancellableEvent cancellableEvent && isStunned(event.getEntity())) {
            cancellableEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void cancelPlayerAttack(AttackEntityEvent event) {
        cancelEvent(event);
    }

    @SubscribeEvent
    public static void cancelBreakSpeed(PlayerEvent.BreakSpeed event) {
        cancelEvent(event);
    }

    @SubscribeEvent
    public static void cancelActivateBlock(PlayerInteractEvent.RightClickBlock event) {
        cancelEvent(event);
    }

    @SubscribeEvent
    public static void cancelInteract(PlayerInteractEvent.EntityInteract event) {
        cancelEvent(event);
    }

    @SubscribeEvent
    public static void cancelUsingItem(LivingEntityUseItemEvent.Start event) {
        cancelEvent(event);
    }

    @SubscribeEvent
    public static void cancelTickUsingItem(LivingEntityUseItemEvent.Tick event) {
        cancelEvent(event);
    }

    @SubscribeEvent
    public static void cancelPlayerUseItem(PlayerInteractEvent.RightClickItem event) {
        cancelEvent(event);
    }

    @SubscribeEvent
    public static void onLivingTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob mob && isStunned(mob)) {
            if (event.getTargetType() == MOB_TARGET) {
                event.setNewAboutToBeSetTarget(null);
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        if (event.getEntity().hasEffect(GoetiedEffects.TANGLED)){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void PotionApplicationEvents(MobEffectEvent.Applicable event){
        if (event.getEffectInstance().getEffect() == GoetiedEffects.STUNNED
                || event.getEffectInstance().getEffect().value().getDescriptionId().contains("born_in_chaos_v1:stun")){
            if (event.getEntity().getType().is(ModTags.EntityTypes.UNSTUNNABLE)){
                if (event instanceof net.neoforged.bus.api.ICancellableEvent cancellable) {
                    cancellable.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if (!event.getEntity().level().isClientSide) {
            if (isStunned(event.getEntity())) {
                event.getEntity().removeEffect(GoetiedEffects.STUNNED);
                event.getEntity().removeEffect(GoetiedEffects.TANGLED);
                ModNetwork.sendToALL(new SRemoveEffectPacket(event.getEntity().getId(), BuiltInRegistries.MOB_EFFECT.getId(GoetiedEffects.STUNNED.value())));
                ModNetwork.sendToALL(new SRemoveEffectPacket(event.getEntity().getId(), BuiltInRegistries.MOB_EFFECT.getId(GoetiedEffects.TANGLED.value())));
            }
        }
    }
}
