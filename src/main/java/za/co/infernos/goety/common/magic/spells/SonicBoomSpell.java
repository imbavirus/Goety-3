package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SonicBoomSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRange(15);
    }

    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SonicBoomCost, 0);
    }

    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SonicBoomDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SonicBoomCoolDown, 0);
    }

    public SoundEvent CastingSound() {
        return SoundEvents.WARDEN_SONIC_CHARGE;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.SonicBoomDamage, 1.0F) * WandUtil.damageMultiply();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)){
            damage += WandUtil.getPotencyLevel(caster);
            range += WandUtil.getRangeLevel(caster);
        }
        damage += spellStat.getPotency();
        LivingEntity livingEntity = this.getTarget(caster, range);
        if (livingEntity != null){
            Vec3 vec3 = caster.position().add(0.0D, (double) 1.6F, 0.0D);
            Vec3 vec31 = livingEntity.getEyePosition().subtract(vec3);
            Vec3 vec32 = vec31.normalize();

            for (int i = 1; i < Mth.floor(vec31.length()) + 7; ++i) {
                Vec3 vec33 = vec3.add(vec32.scale((double) i));
                worldIn.sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(worldIn, caster, SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
            livingEntity.hurt(caster.damageSources().sonicBoom(caster), damage);
            double d1 = 0.5D * (1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            double d0 = 2.5D * (1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            livingEntity.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
        } else {
            Vec3 srcVec = new Vec3(caster.getX(), caster.getEyeY(), caster.getZ());
            Vec3 lookVec = caster.getViewVector(1.0F);
            Vec3 destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
            for(int i = 1; i < Math.floor(destVec.length()) + 7; ++i) {
                Vec3 vector3d2 = srcVec.add(lookVec.scale(i));
                worldIn.sendParticles(ParticleTypes.SONIC_BOOM, vector3d2.x, vector3d2.y, vector3d2.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
            if (MobUtil.getSingleTarget(worldIn, caster, range, 3.0D) instanceof LivingEntity target1){
                target1.hurt(caster.damageSources().sonicBoom(caster), damage);
                double d0 = target1.getX() - caster.getX();
                double d1 = target1.getZ() - caster.getZ();
                double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                MobUtil.push(target1, d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
            }
        }
        this.playSound(worldIn, caster, SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
    }
}