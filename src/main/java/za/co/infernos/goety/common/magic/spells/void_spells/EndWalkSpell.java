package za.co.infernos.goety.common.magic.spells.void_spells;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayPlayerSoundPacket;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.SEHelper;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EndWalkSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.EndWalkCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.EndWalkDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.EndWalkCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
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
            potency += WandUtil.getPotencyLevel(caster);
            duration = MathHelper.secondsToTicks(5) * WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }
        if (caster instanceof Player player){
            player.addEffect(new MobEffectInstance(GoetyEffects.SHADOW_WALK, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.EndWalkEffectDuration, 0) + duration, potency, false, false, true));
            for(int i = 0; i < 16; ++i) {
                double d0 = MathHelper.rgbToSpeed(96.0D);
                double d1 = MathHelper.rgbToSpeed(62.0D);
                double d2 = MathHelper.rgbToSpeed(92.0D);
                worldIn.sendParticles(ModParticleTypes.CULT_SPELL.get(), caster.getRandomX(1.0D), caster.getRandomY(), caster.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
            }
            SEHelper.setEndWalk(player, player.blockPosition(), player.level().dimension());
            ModNetwork.sendTo(player, new SPlayPlayerSoundPacket(ModSounds.END_WALK.get(), 1.0F, 1.0F));
        }
    }
}

