package za.co.infernos.goety.common.events;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.hostile.cultists.Crone;
import za.co.infernos.goety.common.entities.hostile.cultists.Cultist;
import za.co.infernos.goety.common.entities.hostile.cultists.Maverick;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.WitchBarterHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class WitchBarterEvents {

    @SubscribeEvent
    public static void LivingEffects(EntityTickEvent.Post event){
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) {
            return;
        }
        if (livingEntity != null && livingEntity.isAlive()){
            if (livingEntity instanceof Raider raider) {
                if (raider instanceof Cultist || raider instanceof Witch) {
                    if (WitchBarterHelper.getTimer(raider) > 0) {
                        WitchBarterHelper.decreaseTimer(raider);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void InteractEntityEvent(PlayerInteractEvent.EntityInteractSpecific event){
        Player player = event.getEntity();
        if (!event.getLevel().isClientSide) {
            if (CuriosFinder.isWitchFriendly(player)) {
                if (event.getTarget() instanceof Raider witch) {
                    if (witch instanceof Witch || witch instanceof Cultist cultist && cultist.isBarterable()) {
                        if (!witch.isAggressive()) {
                            if (WitchBarterHelper.getTimer(witch) <= 0) {
                                if (event.getHand() == InteractionHand.MAIN_HAND) {
                                    boolean maverick = witch instanceof Maverick && witch.getOffhandItem().isEmpty();
                                    if ((witch.getMainHandItem().isEmpty() || maverick) && (event.getItemStack().is(ModTags.Items.WITCH_CURRENCY) || event.getItemStack().is(ModTags.Items.WITCH_BETTER_CURRENCY))) {
                                        event.setCanceled(true);
                                        event.setCancellationResult(InteractionResult.SUCCESS);
                                        if (witch instanceof Crone) {
                                            witch.playSound(ModSounds.CRONE_AMBIENT.get());
                                        } else {
                                            witch.playSound(witch.getCelebrateSound());
                                        }
                                        ItemStack itemstack1;
                                        if (player.isCreative()) {
                                            itemstack1 = event.getItemStack();
                                        } else {
                                            itemstack1 = event.getItemStack().split(1);
                                        }
                                        if (witch instanceof Maverick){
                                            witch.setItemSlot(EquipmentSlot.OFFHAND, itemstack1);
                                        } else {
                                            witch.setItemSlot(EquipmentSlot.MAINHAND, itemstack1);
                                        }
                                        WitchBarterHelper.setTrader(witch, player);
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
