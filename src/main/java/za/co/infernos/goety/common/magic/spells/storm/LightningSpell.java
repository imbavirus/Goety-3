package za.co.infernos.goety.common.magic.spells.storm;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.projectiles.SpellLightningBolt;
import za.co.infernos.goety.common.magic.Spell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LightningSpell extends Spell {

    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LightningCost, 0);
    }

    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LightningDuration, 0);
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.LightningCoolDown, 0);
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
        return list;
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        double radius = spellStat.getRadius();
        int range = spellStat.getRange();
        float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.LightningDamage, 1.0F) * WandUtil.damageMultiply();
        if (WandUtil.enchantedFocus(caster)) {
            range += WandUtil.getRangeLevel(caster);
            damage += WandUtil.getPotencyLevel(caster);
        }
        damage += spellStat.getPotency();
        HitResult rayTraceResult = this.rayTrace(worldIn, caster, range, radius);
        Optional<BlockPos> lightningRod = BlockFinder.findLightningRod(worldIn, BlockPos.containing(rayTraceResult.getLocation()));
        if (lightningRod.isPresent() && !rightStaff(staff)){
            BlockPos blockPos = lightningRod.get();
            SpellLightningBolt lightningBolt = new SpellLightningBolt(ModEntityType.SPELL_LIGHTNING_BOLT.get(), worldIn);
            lightningBolt.setDamage(damage);
            lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
            lightningBolt.setOwner(caster);
            worldIn.addFreshEntity(lightningBolt);
        } else {
            LivingEntity target = this.getTarget(caster, range);
            if (target != null){
                SpellLightningBolt lightningBolt = new SpellLightningBolt(ModEntityType.SPELL_LIGHTNING_BOLT.get(), worldIn);
                lightningBolt.setDamage(damage);
                lightningBolt.setPos(target.position());
                lightningBolt.setOwner(caster);
                worldIn.addFreshEntity(lightningBolt);
            } else if (rayTraceResult instanceof BlockHitResult blockHitResult){
                BlockPos blockPos = blockHitResult.getBlockPos();
                SpellLightningBolt lightningBolt = new SpellLightningBolt(ModEntityType.SPELL_LIGHTNING_BOLT.get(), worldIn);
                lightningBolt.setDamage(damage);
                lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                lightningBolt.setOwner(caster);
                worldIn.addFreshEntity(lightningBolt);
            }
        }
    }
}