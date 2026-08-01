package za.co.infernos.goetied.common.items.magic;

import za.co.infernos.goetied.common.entities.deco.HauntedArmorStand;
import za.co.infernos.goetied.utils.MobUtil;
import za.co.infernos.goetied.utils.SEHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class GrudgeGrimoire extends Item {
    public GrudgeGrimoire() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.RARE));
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.level().isClientSide) {
            if (pAttacker instanceof Player attacker && !(pTarget instanceof ArmorStand || pTarget instanceof HauntedArmorStand)) {
                if (MobUtil.isShifting(attacker)) {
                    if (SEHelper.addGrudgeEntityType(attacker, pTarget.getType())) {
                        if (SEHelper.getAllyEntityTypes(attacker).contains(pTarget.getType())){
                            SEHelper.removeAllyEntityType(attacker, pTarget.getType());
                            attacker.displayClientMessage(Component.translatable("info.goetied.goodwill.removeType", I18n.get(pTarget.getType().getDescriptionId())), true);
                        }
                        pTarget.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.25F);
                        attacker.displayClientMessage(Component.translatable("info.goetied.grimoire.addType", I18n.get(pTarget.getType().getDescriptionId())), true);
                    }
                } else {
                    if (SEHelper.addGrudgeEntity(attacker, pTarget)) {
                        if (SEHelper.getAllyEntities(attacker).contains(pTarget)){
                            SEHelper.removeAllyEntity(attacker, pTarget);
                            attacker.displayClientMessage(Component.translatable("info.goetied.goodwill.remove", I18n.get(pTarget.getType().getDescriptionId())), true);
                        }
                        pTarget.playSound(SoundEvents.ARROW_HIT_PLAYER);
                        attacker.displayClientMessage(Component.translatable("info.goetied.grimoire.add", pTarget.getDisplayName()), true);
                    }
                }
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand){
        if (!player.level().isClientSide) {
            if (MobUtil.isShifting(player)) {
                if (SEHelper.removeGrudgeEntityType(player, target.getType())) {
                    target.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 0.25F);
                    player.displayClientMessage(Component.translatable("info.goetied.grimoire.removeType", I18n.get(target.getType().getDescriptionId())), true);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (SEHelper.removeGrudgeEntity(player, target)) {
                    target.playSound(SoundEvents.PLAYER_LEVELUP);
                    player.displayClientMessage(Component.translatable("info.goetied.grimoire.remove", target.getDisplayName()), true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (MobUtil.isShifting(player)) {
                if (SEHelper.getGrudgeEntityTypes(player).isEmpty()){
                    player.displayClientMessage(Component.translatable("info.goetied.grimoire.emptyTypes"), false);
                } else {
                    player.displayClientMessage(Component.translatable("info.goetied.grimoire.type"), false);
                }
                for (EntityType<?> entityType : SEHelper.getGrudgeEntityTypes(player)) {
                    player.displayClientMessage(Component.translatable(entityType.getDescriptionId()), false);
                }
            } else {
                if (SEHelper.getGrudgeEntities(player).isEmpty()){
                    player.displayClientMessage(Component.translatable("info.goetied.grimoire.empty"), false);
                } else {
                    player.displayClientMessage(Component.translatable("info.goetied.grimoire.grudges"), false);
                }
                for (Entity entity : SEHelper.getGrudgeEntities(player)) {
                    player.displayClientMessage(entity.getDisplayName(), false);
                }
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }
}
