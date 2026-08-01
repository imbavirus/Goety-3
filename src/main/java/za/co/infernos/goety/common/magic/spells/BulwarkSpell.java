package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MiscCapHelper;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class BulwarkSpell extends Spell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BulwarkCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BulwarkDuration, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BulwarkCoolDown, 0);
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster) {
        return MiscCapHelper.getShields(caster) <= 0;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int amount = za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BulwarkShieldAmount, 0);
        int duration = za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BulwarkShieldTime, 0);
        if (WandUtil.enchantedFocus(caster)) {
            amount += WandUtil.getPotencyLevel(caster);
            duration *= Math.min(4, WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1);
        }
        amount += spellStat.getPotency();
        if (spellStat.getDuration() > 0){
            duration = spellStat.getDuration();
        }
        LivingEntity target = this.getTarget(caster);
        if (isShifting(caster) && target != null){
            if (MobUtil.areAllies(target, caster)){
                MiscCapHelper.setShields(target, amount);
                MiscCapHelper.setShieldTime(target, duration);
            }
        } else {
            MiscCapHelper.setShields(caster, amount);
            MiscCapHelper.setShieldTime(caster, duration);
        }
        this.playSound(worldIn, caster, ModSounds.SHIELD_UP.get(), 3.0F, caster.getVoicePitch());
    }
}