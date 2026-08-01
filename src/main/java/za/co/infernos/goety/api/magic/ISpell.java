package za.co.infernos.goety.api.magic;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.curios.MagicRobeItem;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModAttributes;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public interface ISpell {

    default SpellStat defaultStats(){
        return new SpellStat(0, 0, 16, 0.0D, 0, 0.0F);
    }

    int defaultSoulCost();

    default int soulCost(LivingEntity caster, ItemStack staff){
        return SoulCalculation(caster);
    }

    default int SoulCalculation(LivingEntity caster){
        float cost = defaultSoulCost() * SoulCostUp(caster);
        BlockPos blockPos = caster.blockPosition();
        Level level = caster.level();
        Holder<Biome> biomeHolder = level.getBiome(blockPos);
        boolean enable = za.co.infernos.goety.utils.ConfigHelper.getBoolean(SpellConfig.EnvironmentalCost, true);
        if (SoulDiscount(caster)){
            cost /= 1.15F;
        }
        if (this.getSpellType() == SpellType.FROST){
            if (FrostSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if (biomeHolder.value().coldEnoughToSnow(blockPos) || biomeHolder.is(ModTags.Biomes.FROST_DISCOUNT) || (level.isRainingAt(blockPos) && biomeHolder.value().coldEnoughToSnow(blockPos))) {
                    cost /= 1.5F;
                } else if (biomeHolder.is(ModTags.Biomes.FROST_MARKUP)) {
                    cost *= 1.5F;
                }
            }
        }
        if (this.getSpellType() == SpellType.WIND){
            if (WindSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if ((blockPos.getY() >= 128 && level.canSeeSky(blockPos)) || (biomeHolder.is(ModTags.Biomes.WIND_DISCOUNT))) {
                    cost /= 1.5F;
                } else if ((blockPos.getY() <= 32 && !level.canSeeSky(blockPos)) || (biomeHolder.is(ModTags.Biomes.WIND_MARKUP))) {
                    cost *= 1.5F;
                }
            }
        }
        if (this.getSpellType() == SpellType.STORM){
            if (StormSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if ((level.canSeeSky(blockPos) && level.isThundering()) || biomeHolder.is(ModTags.Biomes.STORM_DISCOUNT)) {
                    cost /= 1.5F;
                } else if (biomeHolder.is(ModTags.Biomes.STORM_MARKUP)) {
                    cost *= 1.5F;
                }
            }
        }
        if (this.getSpellType() == SpellType.GEOMANCY){
            if (GeoSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if ((blockPos.getY() <= 32 || biomeHolder.is(ModTags.Biomes.GEOMANCY_DISCOUNT))) {
                    cost /= 1.5F;
                } else if (blockPos.getY() >= 128 || biomeHolder.is(ModTags.Biomes.GEOMANCY_MARKUP)) {
                    cost *= 1.5F;
                }
            }
        }
        if (this.getSpellType() == SpellType.NETHER){
            if (NetherSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if (level.dimension() == Level.NETHER || biomeHolder.is(ModTags.Biomes.NETHER_DISCOUNT)) {
                    cost /= 1.5F;
                } else if (biomeHolder.is(ModTags.Biomes.NETHER_MARKUP)) {
                    cost *= 1.5F;
                }
            }
        }
        if (this.getSpellType() == SpellType.NECROMANCY){
            if (NecroSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if (level.getMoonBrightness() > 0.9F || biomeHolder.is(ModTags.Biomes.NECROMANCY_DISCOUNT)
                        || BlockFinder.findStructure(level, blockPos, ModTags.Structures.NECROMANCER_POWER)) {
                    cost /= 1.5F;
                } else if (biomeHolder.is(ModTags.Biomes.NECROMANCY_MARKUP)) {
                    cost *= 1.5F;
                }
            }
        }
        if (this.getSpellType() == SpellType.WILD){
            if (WildSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if (biomeHolder.is(ModTags.Biomes.WILD_DISCOUNT)) {
                    cost /= 1.5F;
                } else if (biomeHolder.is(ModTags.Biomes.WILD_MARKUP)) {
                    cost *= 1.5F;
                }
            }
        }
        if (this.getSpellType() == SpellType.ABYSS){
            if (AbyssSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if (biomeHolder.is(ModTags.Biomes.ABYSS_DISCOUNT)) {
                    cost /= 1.5F;
                } else if (biomeHolder.is(ModTags.Biomes.ABYSS_MARKUP)) {
                    cost *= 1.5F;
                }
            }
        }
        if (this.getSpellType() == SpellType.VOID){
            if (VoidSoulDiscount(caster)){
                cost /= 2;
            }
            if (enable) {
                if (biomeHolder.is(ModTags.Biomes.VOID_DISCOUNT)
                        || level.dimension() == Level.END) {
                    cost /= 1.5F;
                } else if (biomeHolder.is(ModTags.Biomes.VOID_MARKUP)) {
                    cost *= 1.5F;
                }
            }
        }
        cost *= (float) ModAttributes.getSoulDiscount(caster);
        return (int) cost;
    }

    int defaultCastDuration();

    default int castDuration(LivingEntity caster, ItemStack staff){
        double duration = defaultCastDuration();
        if (ReduceCastTime(caster)){
            duration /= 2;
        }
        duration *= ModAttributes.getCastingSpeed(caster);
        return (int) duration;
    }

    @Nullable
    default SoundEvent CastingSound(LivingEntity caster){
        return this.CastingSound();
    }

    @Nullable
    default SoundEvent CastingSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    default float castingVolume(){
        return 0.5F;
    }

    default float castingPitch(){
        return 1.0F;
    }

    int defaultSpellCooldown();

    @Deprecated
    default int spellCooldown(){
        return defaultSpellCooldown();
    }

    default int spellCooldown(LivingEntity caster){
        return (int) (defaultSpellCooldown() * ModAttributes.getCooldownDiscount(caster));
    }

    /**
     * Allows a spell to opt out of the wand's default focus cooldown application.
     * Return true if the spell manages its own cooldown bookkeeping (e.g. via SEHelper directly).
     */
    default boolean hasCustomCooldown(LivingEntity caster, ItemStack staff, ItemStack focus, int ticks){
        return false;
    }

    @OnlyIn(Dist.CLIENT) //Had to use this in case of server issues T~T
    default HumanoidModel.ArmPose getPose(LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        return SpellPoses.SPELL;
    }

    default void startSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
    }

    default void useSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, int castTime, SpellStat spellStat) {
    }

    default void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, ItemStack focus, int castTime, SpellStat spellStat) {
    }

    default void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat){
    }

    SpellType getSpellType();

    default boolean conditionsMet(ServerLevel worldIn, LivingEntity caster){
        return conditionsMet(worldIn, caster, WandUtil.getStats(caster, this));
    }

    default boolean conditionsMet(ServerLevel worldIn, LivingEntity caster, SpellStat spellStat){
        return true;
    }

    List<Enchantment> acceptedEnchantments();

    default SoundEvent loopSound(LivingEntity caster){
        return null;
    }

    default ColorUtil particleColors(LivingEntity caster){
        return new ColorUtil(0.2F, 0.2F, 0.2F);
    }

    @Nullable
    default ParticleOptions getParticle(LivingEntity caster){
        return net.minecraft.core.particles.ColorParticleOption.create(
                ParticleTypes.ENTITY_EFFECT,
                (float) this.particleColors(caster).red(),
                (float) this.particleColors(caster).green(),
                (float) this.particleColors(caster).blue()
        );
    }

    default void useParticle(Level worldIn, LivingEntity caster, ItemStack stack){
        double d0 = this.particleColors(caster).red();
        double d1 = this.particleColors(caster).green();
        double d2 = this.particleColors(caster).blue();
        ParticleOptions particle = this.getParticle(caster);
        if (particle != null){
            if (worldIn instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(particle, caster.getX(), caster.getBoundingBox().maxY + 0.5D, caster.getZ(), 0, d0, d1, d2, 0.5F);
            } else {
                worldIn.addParticle(particle, caster.getX(), caster.getBoundingBox().maxY + 0.5D, caster.getZ(), d0, d1, d2);
            }
        }
    }

    @Nullable
    default LivingEntity getTarget(LivingEntity caster){
        return null;
    }

    default HitResult rayTrace(Level worldIn, LivingEntity caster, int range, double radius) {
        if (this.entityResult(worldIn, caster, range, radius) == null){
            return this.blockResult(worldIn, caster, range);
        } else {
            return this.entityResult(worldIn, caster, range, radius);
        }
    }

    default BlockHitResult blockResult(Level worldIn, LivingEntity caster, double range) {
        float f = caster.getXRot();
        float f1 = caster.getYRot();
        Vec3 vector3d = caster.getEyePosition(1.0F);
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vector3d1 = vector3d.add((double)f6 * range, (double)f5 * range, (double)f7 * range);
        return worldIn.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, caster));
    }

    default EntityHitResult entityResult(Level worldIn, LivingEntity caster, int range, double radius){
        Vec3 srcVec = caster.getEyePosition(1.0F);
        Vec3 lookVec = caster.getViewVector(1.0F);
        Vec3 destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        AABB axisalignedbb = caster.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(radius, radius, radius);
        return ProjectileUtil.getEntityHitResult(worldIn, caster, srcVec, destVec, axisalignedbb, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());
    }

    default boolean ReduceCastTime(LivingEntity caster){
        if (CuriosFinder.hasCastTimeReduce(caster)) {
            return true;
        } else if (this.getSpellType() == SpellType.FROST){
            return CuriosFinder.hasFrostCrown(caster);
        } else if (this.getSpellType() == SpellType.WILD){
            return CuriosFinder.hasWildCrown(caster);
        } else if (this.getSpellType() == SpellType.ABYSS){
            return CuriosFinder.hasAbyssCrown(caster);
        } else if (this.getSpellType() == SpellType.VOID){
            return CuriosFinder.hasVoidCrown(caster);
        } else if (this.getSpellType() == SpellType.NETHER){
            return CuriosFinder.hasNetherCrown(caster);
        } else if (this.getSpellType() == SpellType.NECROMANCY){
            return CuriosFinder.hasUndeadCrown(caster);
        }
        return false;
    }

    @Nullable
    default MobEffectInstance summonDownEffect(LivingEntity caster){
        return caster.getEffect(GoetyEffects.SUMMON_DOWN);
    }

    default int SoulCostUp(LivingEntity caster){
        MobEffectInstance mobEffectInstance = summonDownEffect(caster);
        if (mobEffectInstance != null && this instanceof ISummonSpell){
            return mobEffectInstance.getAmplifier() + 2;
        }
        return 1;
    }

    default boolean SoulDiscount(LivingEntity caster){
        return CuriosFinder.hasCurio(caster, itemStack -> itemStack.getItem() instanceof MagicRobeItem);
    }

    default boolean FrostSoulDiscount(LivingEntity caster){
        return CuriosFinder.hasFrostRobes(caster);
    }

    default boolean WindSoulDiscount(LivingEntity caster){
        return CuriosFinder.hasCurio(caster, ModItems.WIND_ROBE.get());
    }

    default boolean GeoSoulDiscount(LivingEntity caster){
        return CuriosFinder.hasCurio(caster, ModItems.AMETHYST_NECKLACE.get());
    }

    default boolean StormSoulDiscount(LivingEntity caster){
        return CuriosFinder.hasCurio(caster, ModItems.STORM_ROBE.get());
    }

    default boolean WildSoulDiscount(LivingEntity caster){
        return CuriosFinder.hasWildRobe(caster);
    }

    default boolean NetherSoulDiscount(LivingEntity caster){
        return CuriosFinder.hasNetherRobe(caster);
    }

    default boolean AbyssSoulDiscount(LivingEntity caster){
        return CuriosFinder.hasAbyssRobes(caster);
    }

    default boolean NecroSoulDiscount(LivingEntity caster){
        return false;
    }

    default boolean VoidSoulDiscount(LivingEntity caster){
        return CuriosFinder.hasVoidRobe(caster);
    }
}
