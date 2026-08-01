package za.co.infernos.goety.common.magic.spells.wind;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.util.UpdraftBlast;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class UpdraftSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRadius(1.0D);
    }

    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.UpdraftCost, 0);
    }

    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.UpdraftDuration, 0);
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.UpdraftCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WIND;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        int range = spellStat.getRange();
        double radius = spellStat.getRadius();
        if (rightStaff(staff)){
            range *= 2;
            radius += 0.5D;
        }
        float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.UpdraftBlastDamage, 1.0F) * WandUtil.damageMultiply();
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getRangeLevel(caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
            damage += WandUtil.getPotencyLevel(caster);
        }
        damage += spellStat.getPotency();
        HitResult rayTraceResult = this.rayTrace(worldIn, caster, range, radius);
        LivingEntity target = this.getTarget(caster, range);
        if (target != null){
            UpdraftBlast updraftBlast = new UpdraftBlast(ModEntityType.UPDRAFT_BLAST.get(), worldIn);
            updraftBlast.setOwner(caster);
            updraftBlast.setDamage(damage);
            updraftBlast.setAreaOfEffect((float) radius);
            updraftBlast.setPos(target.position());
            worldIn.addFreshEntity(updraftBlast);
        } else if (rayTraceResult instanceof BlockHitResult){
            BlockPos blockPos = ((BlockHitResult) rayTraceResult).getBlockPos();
            UpdraftBlast updraftBlast = new UpdraftBlast(ModEntityType.UPDRAFT_BLAST.get(), worldIn);
            updraftBlast.setOwner(caster);
            updraftBlast.setDamage(damage);
            updraftBlast.setAreaOfEffect((float) radius);
            updraftBlast.setPos(blockPos.getX() + 0.5F, blockPos.getY() + 1.0F, blockPos.getZ() + 0.5F);
            worldIn.addFreshEntity(updraftBlast);
        }
    }
}