package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.client.particles.RisingCircleParticleOption;
import za.co.infernos.goety.client.particles.SoulShockwaveParticleOption;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.RandomUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class SoulHealSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setPotency(1);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulHealCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulHealDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulHealCoolDown, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster) {
        if (caster instanceof Mob mob){
            return mob.getHealth() < mob.getMaxHealth();
        }
        return super.conditionsMet(worldIn, caster);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        int potency = spellStat.getPotency();
        double radius = spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }
        float heal = RandomUtil.nextInt(worldIn.getRandom(), za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SoulHealAmount, 0) * Math.max(1, potency)) + 1.0F;
        caster.heal(heal);
        if (radius > 0) {
            for (LivingEntity livingEntity : worldIn.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().inflate(8.0D * radius))) {
                if (MobUtil.getOwner(livingEntity) != null) {
                    if (MobUtil.getOwner(livingEntity) == caster) {
                        livingEntity.heal(heal);
                        healParticles(livingEntity, worldIn);
                    }
                }
            }
        }
        worldIn.sendParticles(new SoulShockwaveParticleOption(), caster.getX(), caster.getY() + 0.5F, caster.getZ(), 0, 0, 0, 0, 0);
        healParticles(caster, worldIn);
        this.playSound(worldIn, caster, ModSounds.SOUL_HEAL.get());
    }

    public void healParticles(LivingEntity livingEntity, ServerLevel worldIn){
        ColorUtil colorUtil = new ColorUtil(0x2ac9cf);
        worldIn.sendParticles(new RisingCircleParticleOption(0), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, colorUtil.red(), colorUtil.green(), colorUtil.blue(), 1.0F);
        worldIn.sendParticles(new RisingCircleParticleOption(5), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, colorUtil.red(), colorUtil.green(), colorUtil.blue(), 1.0F);
        worldIn.sendParticles(new RisingCircleParticleOption(10), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, colorUtil.red(), colorUtil.green(), colorUtil.blue(), 1.0F);
    }
}