package za.co.infernos.goety.common.magic.spells.void_spells;

import za.co.infernos.goety.api.entities.ally.IServant;
import za.co.infernos.goety.api.magic.ITouchSpell;
import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.items.magic.RecallFocus;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RecallSpell extends Spell implements ITouchSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RecallCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RecallDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RecallCoolDown, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return null;
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }

    @Override
    public SoundEvent loopSound(LivingEntity caster) {
        return SoundEvents.PORTAL_TRIGGER;
    }

    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster){
        if (caster instanceof ServerPlayer player) {
            if (RecallFocus.isValid(worldIn, WandUtil.findFocus(player))) {
                return true;
            } else if (!RecallFocus.hasRecall(WandUtil.findFocus(player))){
                player.displayClientMessage(Component.translatable("info.goety.focus.noPos"), true);
            } else {
                player.displayClientMessage(Component.translatable("info.goety.focus.PosInvalid"), true);
            }
        }
        return false;
    }

    @Override
    public void useParticle(Level worldIn, LivingEntity caster, ItemStack stack) {
        for(int i = 0; i < 2; ++i) {
            worldIn.addParticle(ParticleTypes.PORTAL, caster.getRandomX(0.5D), caster.getRandomY() - 0.25D, caster.getRandomZ(0.5D), (worldIn.random.nextDouble() - 0.5D) * 2.0D, -worldIn.random.nextDouble(), (worldIn.random.nextDouble() - 0.5D) * 2.0D);
        }
    }

    @Override
    public void touchResult(ServerLevel worldIn, LivingEntity caster, LivingEntity target, ItemStack staff, SpellStat spellStat) {
        if (caster instanceof ServerPlayer player) {
            if (RecallFocus.isValid(worldIn, WandUtil.findFocus(player))) {
                if (MobUtil.getOwner(target) != null){
                    if (MobUtil.getOwner(target) == caster){
                        if (target instanceof IServant servant) {
                            BlockPos blockPos = RecallFocus.getRecallBlockPos(WandUtil.findFocus(player));
                            servant.setWandering(false);
                            if (blockPos != null) {
                                servant.setStaying(false);
                                servant.setBoundPos(blockPos);
                            } else {
                                servant.setStaying(true);
                                servant.setBoundPos(null);
                            }
                        }
                        RecallFocus.recall(target, WandUtil.findFocus(player));
                    }
                }
            }
        }
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        if (caster instanceof ServerPlayer player) {
            if (RecallFocus.isValid(worldIn, WandUtil.findFocus(player))) {
                RecallFocus.recall(player, WandUtil.findFocus(player));
            }
        }
    }
}