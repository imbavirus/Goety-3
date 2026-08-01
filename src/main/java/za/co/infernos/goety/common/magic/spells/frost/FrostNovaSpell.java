package za.co.infernos.goety.common.magic.spells.frost;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.client.particles.ShockwaveParticleOption;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FrostNovaSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return new SpellStat(0, 1, 0, 2.5D, 0, 0.0F);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FrostNovaCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FrostNovaDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.FROST_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FrostNovaCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.FROST;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RADIUS.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        float potency = spellStat.getPotency();
        float radius = (float) spellStat.getRadius();
        int duration = spellStat.getDuration();
        float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.FrostNovaDamage, 1.0F) * WandUtil.damageMultiply();
        float maxDamage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.FrostNovaMaxDamage, 1.0F) * WandUtil.damageMultiply();
        if (WandUtil.enchantedFocus(caster)){
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
            potency += WandUtil.getPotencyLevel(caster) / 2.0F;
        }
        damage += potency;
        maxDamage += potency;
        LivingEntity spellTarget = caster;
        LivingEntity target = this.getTarget(caster);
        if (isShifting(caster) && target != null){
            spellTarget = target;
        }
        this.createParticleBall(worldIn, spellTarget, (int) radius);
        worldIn.sendParticles(new ShockwaveParticleOption(0, (float) (radius * 2), 1), spellTarget.getX(), spellTarget.getY() + 0.5F, spellTarget.getZ(), 0, 0, 0, 0, 0);
        float trueDamage = Mth.clamp(damage + RandomUtil.nextInt(worldIn.getRandom(), (int) (maxDamage - damage)), damage, maxDamage);
        int finalDuration = duration;
        int amp = 0;
        if (rightStaff(staff)) {
            amp += 1;
            radius += 0.5F;
        }
        int finalAmp = amp;
        new SpellExplosion(worldIn, caster, ModDamageSource.directFreeze(caster), spellTarget.blockPosition(), (float) radius, trueDamage){
            @Override
            public void explodeHurt(Entity target, DamageSource damageSource, double x, double y, double z, double seen, float actualDamage) {
                if (target instanceof LivingEntity target1 && !MobUtil.areAllies(caster, target1) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target1)){
                    super.explodeHurt(target, damageSource, x, y, z, seen, actualDamage);
                    target1.addEffect(new MobEffectInstance(GoetyEffects.FREEZING, MathHelper.secondsToTicks(5) * finalDuration, finalAmp));
                }
            }
        };
        this.playSound(worldIn, spellTarget, ModSounds.ICE_CHUNK_HIT.get(), 1.0F, 0.5F);
    }

    private void createParticleBall(ServerLevel serverLevel, LivingEntity livingEntity, int radius) {
        double d0 = livingEntity.getX();
        double d1 = livingEntity.getY();
        double d2 = livingEntity.getZ();

        for(int i = -radius; i <= radius; ++i) {
            for(int j = -radius; j <= radius; ++j) {
                for(int k = -radius; k <= radius; ++k) {
                    double d3 = (double)j + (livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble()) * 0.5D;
                    double d4 = (double)i + (livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble()) * 0.5D;
                    double d5 = (double)k + (livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble()) * 0.5D;
                    double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / 0.5 + livingEntity.getRandom().nextGaussian() * 0.05D;
                    serverLevel.sendParticles(ModParticleTypes.FROST_NOVA.get(), d0, d1, d2, 0, d3 / d6, d4 / d6, d5 / d6, 0.5F);
                    if (i != -radius && i != radius && j != -radius && j != radius) {
                        k += radius * 2 - 1;
                    }
                }
            }
        }
    }
}
