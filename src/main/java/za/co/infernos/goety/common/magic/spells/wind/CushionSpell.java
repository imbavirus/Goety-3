package za.co.infernos.goety.common.magic.spells.wind;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.util.Cushion;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class CushionSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRange(64);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CushionCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CushionDuration, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.CushionCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WIND;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.DURATION.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int range = spellStat.getRange();
        int duration = spellStat.getDuration();
        int radius = (int) spellStat.getRadius();
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getRangeLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }
        HitResult rayTraceResult = this.rayTrace(worldIn, caster, range, 3.0D);
        LivingEntity target = this.getTarget(caster, range);
        if (target != null){
            Cushion cushion = new Cushion(ModEntityType.CUSHION.get(), worldIn);
            cushion.setRadius(radius);
            cushion.increaseLifeSpan(duration);
            cushion.setPos(target.position());
            worldIn.addFreshEntity(cushion);
        } else if (rayTraceResult instanceof BlockHitResult){
            BlockPos blockPos = ((BlockHitResult) rayTraceResult).getBlockPos();
            Cushion cushion = new Cushion(ModEntityType.CUSHION.get(), worldIn);
            cushion.setRadius(radius);
            cushion.increaseLifeSpan(duration);
            cushion.setPos(blockPos.getX() + 0.5F, blockPos.getY() + 1.0F, blockPos.getZ() + 0.5F);
            worldIn.addFreshEntity(cushion);
        }
        this.playSound(worldIn, caster, SoundEvents.EVOKER_CAST_SPELL);
    }
}