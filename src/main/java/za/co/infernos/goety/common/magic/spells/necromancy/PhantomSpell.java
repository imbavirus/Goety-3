package za.co.infernos.goety.common.magic.spells.necromancy;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.undead.PhantomServant;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.magic.SummonSpell;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.SoundUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PhantomSpell extends SummonSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PhantomCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PhantomDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SUMMON.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PhantomCoolDown, 0);
    }

    @Override
    public int SummonDownDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PhantomSummonDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NECROMANCY;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public Predicate<LivingEntity> summonPredicate() {
        return livingEntity -> livingEntity instanceof PhantomServant;
    }

    @Override
    public int summonLimit() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PhantomLimit, 0);
    }

    @Override
    public void commonResultHit(ServerLevel worldIn, LivingEntity caster) {
        for (int i = 0; i < caster.level().random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.POOF, caster.getX(), caster.getEyeY(), caster.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
        this.playSound(worldIn, caster, SoundEvents.EVOKER_CAST_SPELL);
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        this.commonResult(worldIn, caster);
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getPotencyLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
        }
        if (!isShifting(caster)) {
            SpawnGroupData spawngroupdata = null;
            int l = 1;

            if (rightStaff(staff)){
                l += caster.getRandom().nextInt(4);
            }

            for(int i1 = 0; i1 < l; ++i1) {
                PhantomServant phantom = ModEntityType.PHANTOM_SERVANT.get().create(worldIn);
                if (phantom != null) {
                    BlockPos.MutableBlockPos blockpos$mutable = BlockFinder.SummonFlyingRadius(caster.blockPosition(), phantom, worldIn, 15).mutable();

                    while(blockpos$mutable.getY() < caster.getY() + 20 + caster.getRandom().nextInt(15) && !worldIn.getBlockState(blockpos$mutable).blocksMotion()) {
                        blockpos$mutable.move(Direction.UP);
                    }
                    DifficultyInstance difficultyinstance = worldIn.getCurrentDifficultyAt(blockpos$mutable);
                    phantom.setTrueOwner(caster);
                    phantom.moveTo(blockpos$mutable, 0.0F, 0.0F);
                    spawngroupdata = phantom.finalizeSpawn(worldIn, difficultyinstance, MobSpawnType.MOB_SUMMONED, spawngroupdata);
                    phantom.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                    if (potency > 0){
                        phantom.setPhantomSize(potency);
                    }
                    this.SummonSap(caster, phantom);
                    this.setTarget(caster, phantom);
                    worldIn.addFreshEntityWithPassengers(phantom);
                    this.summonAdvancement(caster, phantom);
                }
            }
            this.SummonDown(caster);
            SoundUtil.playNecromancerSummon(caster);
        }
    }
}

