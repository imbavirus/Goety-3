package za.co.infernos.goety.common.magic.spells.wind;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.RazorWind;
import za.co.infernos.goety.common.entities.projectiles.SlashProjectile;
import za.co.infernos.goety.common.entities.projectiles.VoidSlash;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class RazorWindSpell extends Spell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RazorWindCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RazorWindDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.RazorWindCoolDown, 0);
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WIND;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        float radius = (float) spellStat.getRadius();
        float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.RazorWindDamage, 1.0F) * WandUtil.damageMultiply();
        if (rightStaff(staff)){
            radius += 0.5F;
        }
        if (WandUtil.enchantedFocus(caster)) {
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 4.0F;
            damage += WandUtil.getPotencyLevel(caster);
        }
        damage += spellStat.getPotency();
        SlashProjectile razorWind = new RazorWind(worldIn, caster);
        if (this.typeStaff(staff, SpellType.VOID)) {
            razorWind = new VoidSlash(worldIn, caster);
        }
        razorWind.setPos(caster.getEyePosition());
        razorWind.slash(caster.getLookAngle(), 2.0F);
        razorWind.setRadius(0.3F + radius);
        razorWind.setDamage(damage);
        razorWind.setMaxLifeSpan(40);
        worldIn.addFreshEntity(razorWind);
        this.playSound(worldIn, caster, ModSounds.WIND.get(), 2.0F, 1.5F);
    }
}