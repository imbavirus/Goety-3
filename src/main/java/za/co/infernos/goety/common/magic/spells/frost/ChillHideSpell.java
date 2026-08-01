package za.co.infernos.goety.common.magic.spells.frost;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChillHideSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setDuration(1);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ChillingCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ChillingDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.FROST_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ChillingCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.FROST;
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
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)){
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
            potency += WandUtil.getPotencyLevel(caster);
        }
        LivingEntity target = this.getTarget(caster);
        AABB aabb = caster.getBoundingBox().inflate(4.0D);
        if (isShifting(caster) && target != null){
            if (MobUtil.areAllies(target, caster)){
                target.addEffect(new MobEffectInstance(GoetyEffects.CHILL_HIDE, MathHelper.secondsToTicks(45 * duration), potency));
                aabb = target.getBoundingBox().inflate(4.0D);
            }
        } else {
            caster.addEffect(new MobEffectInstance(GoetyEffects.CHILL_HIDE, MathHelper.secondsToTicks(45 * duration), potency));
        }
        if (this.rightStaff(staff)){
            for (LivingEntity livingEntity : worldIn.getEntitiesOfClass(LivingEntity.class, aabb)) {
                if (MobUtil.areAllies(livingEntity, caster) && livingEntity != caster) {
                    livingEntity.addEffect(new MobEffectInstance(GoetyEffects.CHILL_HIDE, MathHelper.secondsToTicks(45 * duration), potency));
                }
            }
        }
        this.playSound(worldIn, caster, ModSounds.ICE_SPIKE_HIT.get(), 1.0F, 0.5F);
    }
}
