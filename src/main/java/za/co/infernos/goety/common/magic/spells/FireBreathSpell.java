package za.co.infernos.goety.common.magic.spells;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.magic.BreathingSpell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FireBreathSpell extends BreathingSpell {
    private float damageCache = -1.0f; // Cache for lazy evaluation
    
    // Lazy getter to avoid accessing config before it's loaded
    public float getDamage() {
        if (damageCache < 0) {
            damageCache = za.co.infernos.goety.utils.ConfigHelper.getFloat(SpellConfig.FireBreathDamage, 1.0F) * WandUtil.damageMultiply();
        }
        return damageCache;
    }
    
    @Deprecated // Use getDamage() instead
    public float damage = 0; // Will be calculated lazily

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRange(8).setBurning(1);
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FireBreathCost, 0);
    }

    @Override
    public int defaultCastUp() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FireBreathChargeUp, 0);
    }

    @Override
    public int shotsNumber() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FireBreathDuration, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.FireBreathCoolDown, 0);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.FIRE_BREATH_START.get();
    }

    public SoundEvent loopSound(LivingEntity caster) {
        return ModSounds.FIRE_BREATH.get();
    }

    @Override
    public void useParticle(Level worldIn, LivingEntity caster, ItemStack stack) {
        if (worldIn instanceof ServerLevel serverLevel) {
            ServerParticleUtil.addParticlesAroundMiddleSelf(serverLevel, ParticleTypes.SMOKE, caster);
        }
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.BURNING.get());
        return list;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster, SpellStat spellStat) {
        if (caster instanceof Mob mob) {
            if (mob.getTarget() != null) {
                int range = spellStat.getRange();
                if (WandUtil.enchantedFocus(caster)) {
                    range += WandUtil.getRangeLevel(caster);
                }
                return mob.hasLineOfSight(mob.getTarget()) && mob.distanceTo(mob.getTarget()) <= range + 4.0D;
            }
        }
        return super.conditionsMet(worldIn, caster, spellStat);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        float potency = spellStat.getPotency();
        int burning = spellStat.getBurning();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            burning += WandUtil.getLevels(ModEnchantments.BURNING.get(), caster);
            range += WandUtil.getRangeLevel(caster);
        }
        float damage = getDamage() + potency;
        if (!worldIn.isClientSide) {
            if (CuriosFinder.hasCurio(caster, ModItems.RING_OF_THE_DRAGON.get())) {
                damage *= 2.0F;
                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(SpellConfig.DragonFireGriefing, false)) {
                    float flameRange = range * ((float) Math.PI / 180.0F);
                    for (int i = 0; i < 3; i++) {
                        Vec3 cast = caster.getLookAngle().normalize()
                                .xRot(worldIn.random.nextFloat() * flameRange * 2 - flameRange)
                                .yRot(worldIn.random.nextFloat() * flameRange * 2 - flameRange);
                        HitResult hitResult = worldIn.clip(
                                new ClipContext(caster.getEyePosition(), caster.getEyePosition().add(cast.scale(10)),
                                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, caster));
                        if (hitResult.getType() == HitResult.Type.BLOCK) {
                            Vec3 pos = hitResult.getLocation().subtract(cast.scale(0.5D));
                            BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);
                            if (worldIn.getBlockState(blockPos).isAir() ||
                                    (BlockFinder.canBeReplaced(worldIn, blockPos)
                                            && worldIn.getFluidState(blockPos).isEmpty()
                                            && Blocks.FIRE.defaultBlockState().canSurvive(worldIn, blockPos))) {
                                worldIn.setBlockAndUpdate(blockPos, BaseFireBlock.getState(worldIn, blockPos));
                            }
                        }
                    }
                }
            }
            for (Entity target : getBreathTarget(caster, range)) {
                if (target != null) {
                    DamageSource damageSource = ModDamageSource.fireBreath(caster, caster);
                    if (CuriosFinder.hasNetherRobe(caster)) {
                        damageSource = ModDamageSource.magicFireBreath(caster, caster);
                    }
                    if (target.hurt(damageSource, damage)) {
                        target.igniteForSeconds(5 * burning);
                    }
                }
            }
        }
    }

    @Override
    public void showWandBreath(LivingEntity entityLiving, ItemStack staff, SpellStat spellStat) {
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(entityLiving)) {
            range = WandUtil.getRangeLevel(entityLiving);
        }

        if (!CuriosFinder.hasCurio(entityLiving, ModItems.RING_OF_THE_DRAGON.get())) {
            this.dragonBreathAttack(ModParticleTypes.SMALL_DRAGON_FLAME.get(), entityLiving, 10,
                    ((double) range / 10) * 0.5D, 1.0D);
        } else {
            this.dragonBreathAttack(ModParticleTypes.DRAGON_FLAME.get(), entityLiving, ((double) range / 10) * 0.5D);
        }
    }
}