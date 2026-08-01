package za.co.infernos.goety.common.magic.spells.abyss;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.BreathingSpell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ModDamageSource;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BubbleStreamSpell extends BreathingSpell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRange(8);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BubbleStreamCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BubbleStreamChargeUp, 0);
    }

    @Override
    public int shotsNumber() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BubbleStreamDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BubbleStreamCoolDown, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.BUBBLE_STREAM.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ABYSS;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster, SpellStat spellStat) {
        if (caster instanceof Mob mob){
            if (mob.getTarget() != null){
                int range = spellStat.getRange();
                if (WandUtil.enchantedFocus(caster)){
                    range += WandUtil.getRangeLevel(caster);
                }
                return mob.hasLineOfSight(mob.getTarget()) && mob.distanceTo(mob.getTarget()) <= range + 4.0D;
            }
        }
        return super.conditionsMet(worldIn, caster, spellStat);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
        float potency = spellStat.getPotency();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            range += WandUtil.getRangeLevel(caster);
        }
        float damage = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.BubbleStreamDamage, 1.0F) * WandUtil.damageMultiply();
        damage += potency;
        if (!worldIn.isClientSide) {
            if (rightStaff(staff)){
                float flameRange = range * ((float) Math.PI / 180.0F);
                for (int i = 0; i < 3; i++) {
                    Vec3 cast = caster.getLookAngle().normalize().xRot(worldIn.random.nextFloat() * flameRange * 2 - flameRange).yRot(worldIn.random.nextFloat() * flameRange * 2 - flameRange);
                    HitResult hitResult = worldIn.clip(new ClipContext(caster.getEyePosition(), caster.getEyePosition().add(cast.scale(10)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, caster));
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        Vec3 pos = hitResult.getLocation().subtract(cast.scale(0.5D));
                        BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);
                        this.dowseFire(caster, worldIn, blockPos);
                    }
                }
            }
            for (Entity target : getBreathTarget(caster, range)) {
                if (target != null) {
                    DamageSource damageSource = ModDamageSource.bubbleStream(caster, caster);
                    if (target.hurt(damageSource, damage)){
                        int air = Math.min(target.getAirSupply() + 1, target.getMaxAirSupply());
                        target.setAirSupply(air);
                    }
                }
            }
        }
        worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), ModSounds.BUBBLE_STREAM.get(), this.getSoundSource(), worldIn.random.nextFloat() * 0.5F, caster.getVoicePitch());
    }

    private void dowseFire(LivingEntity caster, Level world, BlockPos blockPos) {
        BlockState blockstate = world.getBlockState(blockPos);
        if (blockstate.is(BlockTags.FIRE)) {
            world.levelEvent(null, 1009, blockPos, 0);
            world.removeBlock(blockPos, false);
        } else if (AbstractCandleBlock.isLit(blockstate)) {
            AbstractCandleBlock.extinguish(null, blockstate, world, blockPos);
        } else if (CampfireBlock.isLitCampfire(blockstate)) {
            world.levelEvent(null, 1009, blockPos, 0);
            CampfireBlock.dowse(caster, world, blockPos, blockstate);
            world.setBlockAndUpdate(blockPos, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
        }

    }

    @Override
    public void showWandBreath(LivingEntity entityLiving, ItemStack staff, SpellStat spellStat) {
        int range = 0;
        if (entityLiving instanceof Player player){
            if (WandUtil.enchantedFocus(player)){
                range += WandUtil.getRangeLevel(player);
            }
        }
        this.breathAttack(ModParticleTypes.BUBBLE_STREAM.get(), entityLiving, true, 0.3F + ((double) range / 10), 0);
    }
}