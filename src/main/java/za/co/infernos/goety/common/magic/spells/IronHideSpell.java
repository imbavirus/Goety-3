package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class IronHideSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setDuration(1);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.IronHideCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.IronHideDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.IronHideCoolDown, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster) {
        if (caster instanceof Mob mob){
            return !mob.hasEffect(GoetyEffects.IRON_HIDE);
        }
        return super.conditionsMet(worldIn, caster);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            potency = WandUtil.getPotencyLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }
        caster.addEffect(new MobEffectInstance(GoetyEffects.IRON_HIDE, MathHelper.minutesToTicks(duration), potency, false, false, true));
        this.playSound(worldIn, caster, ModSounds.IRON_HIDE.get());
    }
}
