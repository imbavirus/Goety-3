package za.co.infernos.goety.common.magic.spells.storm;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.projectiles.MonsoonCloud;
import za.co.infernos.goety.common.items.ModItems;
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

public class MonsoonSpell extends Spell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setDuration(100).setRadius(2.0D);
    }

    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MonsoonCost, 0);
    }

    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MonsoonDuration, 0);
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MonsoonCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.STORM;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.DURATION.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        int range = spellStat.getRange();
        int duration = spellStat.getDuration();
        double radius = spellStat.getRadius();
        float potency = spellStat.getPotency();
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getRangeLevel(caster);
            duration *= WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
            potency += WandUtil.getPotencyLevel(caster);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster);
        }
        HitResult rayTraceResult = this.rayTrace(worldIn, caster, range, radius);
        LivingEntity target = this.getTarget(caster, range);
        if (rightStaff(staff)) {
            radius += 1.0D;
        }
        if (target != null){
            MonsoonCloud monsoonCloud = new MonsoonCloud(worldIn, caster, (LivingEntity) target);
            monsoonCloud.setExtraDamage(potency);
            monsoonCloud.setRadius((float) radius);
            monsoonCloud.setLifeSpan(duration);
            monsoonCloud.setStaff(rightStaff(staff));
            if (staff.is(ModItems.NAMELESS_STAFF.get())) {
                monsoonCloud.setLightningColor(0xa7fc3e);
            }
            worldIn.addFreshEntity(monsoonCloud);
            this.playSound(worldIn, caster, SoundEvents.LIGHTNING_BOLT_THUNDER, 0.5F, 1.25F);
        } else if (rayTraceResult instanceof BlockHitResult){
            BlockPos blockPos = ((BlockHitResult) rayTraceResult).getBlockPos();
            MonsoonCloud monsoonCloud = new MonsoonCloud(worldIn, caster, null);
            monsoonCloud.setExtraDamage(potency);
            monsoonCloud.setRadius((float) radius);
            monsoonCloud.setLifeSpan(duration);
            monsoonCloud.setStaff(rightStaff(staff));
            if (staff.is(ModItems.NAMELESS_STAFF.get())) {
                monsoonCloud.setLightningColor(0xa7fc3e);
            }
            monsoonCloud.setPos(blockPos.getX() + 0.5F, blockPos.getY() + 4, blockPos.getZ() + 0.5F);
            worldIn.addFreshEntity(monsoonCloud);
            this.playSound(worldIn, caster, SoundEvents.LIGHTNING_BOLT_THUNDER, 0.5F, 1.25F);
        }
    }
}