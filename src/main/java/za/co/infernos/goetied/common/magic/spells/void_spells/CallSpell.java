package za.co.infernos.goetied.common.magic.spells.void_spells;

import za.co.infernos.goetied.api.magic.SpellType;
import za.co.infernos.goetied.common.items.magic.CallFocus;
import za.co.infernos.goetied.common.magic.Spell;
import za.co.infernos.goetied.common.magic.SpellStat;
import za.co.infernos.goetied.config.SpellConfig;
import za.co.infernos.goetied.init.ModSounds;
import za.co.infernos.goetied.utils.WandUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CallSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.CallCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.CallDuration, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goetied.utils.ConfigHelper.getInt(SpellConfig.CallCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }

    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster){
        if (caster instanceof ServerPlayer player) {
            if (!CallFocus.hasSummon(WandUtil.findFocus(player))){
                player.displayClientMessage(Component.translatable("info.goetied.focus.noSummon"), true);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        if (caster instanceof ServerPlayer player) {
            if (!CallFocus.hasSummon(WandUtil.findFocus(player))){
                player.displayClientMessage(Component.translatable("info.goetied.focus.noSummon"), true);
            } else {
                CallFocus.call(player, WandUtil.findFocus(player));
            }
        }
    }
}