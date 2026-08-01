package za.co.infernos.goety.common.magic.spells.necromancy;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.client.particles.AbsorbTrailParticleOption;
import za.co.infernos.goety.client.particles.GatherTrailParticle;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.EverChargeSpell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.ModDamageSource;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LeechingSpell extends EverChargeSpell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRange(8);
    }

    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LeechingCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LeechingChargeUp, 0);
    }

    @Override
    public int shotsNumber() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LeechingDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LeechingCoolDown, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return null;
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NECROMANCY;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        float potency = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.LeechingDamage, 1.0F) * WandUtil.damageMultiply();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getPotencyLevel(caster) / 2.0F;
            range += WandUtil.getRangeLevel(caster);
        }
        potency += spellStat.getPotency();
        LivingEntity target = this.getTarget(caster, range);
        if (target != null){
            ColorUtil colorUtil = new ColorUtil(ChatFormatting.DARK_RED);
            Vec3 targetVec = new Vec3(target.getX(), target.getY() + (target.getBbHeight() / 2.0F), target.getZ());
            Vec3 casterVec = new Vec3(caster.getRandomX(1.0F), caster.getEyeY(), caster.getRandomZ(1.0F));
            worldIn.sendParticles(new GatherTrailParticle.Option(colorUtil, casterVec), targetVec.x, targetVec.y, targetVec.z, 0, 0.0F, 0.0F, 0.0F, 0.5F);
            for (int i = 0; i < 8; ++i) {
                targetVec = new Vec3(target.getRandomX(1.0F), target.getRandomY(), target.getRandomZ(1.0F));
                worldIn.sendParticles(new AbsorbTrailParticleOption(casterVec, 11141120, 10), targetVec.x, targetVec.y, targetVec.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
            if (target.hurt(ModDamageSource.lifeLeech(caster, caster), potency)) {
                if (this.rightStaff(staff)){
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                }
                this.playSound(worldIn, caster, ModSounds.SOUL_EAT.get());
            }
        }
    }
}