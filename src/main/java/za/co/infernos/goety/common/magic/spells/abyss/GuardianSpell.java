package za.co.infernos.goety.common.magic.spells.abyss;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.GuardianServant;
import za.co.infernos.goety.common.entities.ally.Summoned;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.magic.SummonSpell;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GuardianSpell extends SummonSpell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.GuardianCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.GuardianDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.ABYSS_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.GuardianCoolDown, 0);
    }

    @Override
    public int SummonDownDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.GuardianSummonDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ABYSS;
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
        return livingEntity -> livingEntity instanceof GuardianServant;
    }

    @Override
    public int summonLimit() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.GuardianLimit, 0);
    }

    @Override
    public void commonResultHit(ServerLevel worldIn, LivingEntity caster) {
        for (int i = 0; i < caster.level().random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.POOF, caster.getX(), caster.getEyeY(), caster.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
        this.playSound(worldIn, caster, ModSounds.DROWNED_NECROMANCER_SUMMON.get());
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        this.commonResult(worldIn, caster);
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getPotencyLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
        }
        if (!isShifting(caster)) {
            int i = 1;
            if (rightStaff(staff)){
                i = 2;
            }
            for (int i1 = 0; i1 < i; ++i1) {
                Summoned summonedentity = new GuardianServant(ModEntityType.GUARDIAN_SERVANT.get(), worldIn);
                BlockPos blockPos = BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn);
                if (caster.isUnderWater()){
                    blockPos = BlockFinder.SummonWaterRadius(caster, worldIn);
                }
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(blockPos, 0.0F, 0.0F);
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.setPersistenceRequired();
                summonedentity.finalizeSpawn(worldIn, caster.level().getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED,null);
                this.buffSummon(caster, summonedentity, potency);
                this.SummonSap(caster, summonedentity);
                this.setTarget(caster, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                this.summonAdvancement(caster, summonedentity);
            }
            this.SummonDown(caster);
            this.playSound(worldIn, caster, ModSounds.DROWNED_NECROMANCER_SUMMON.get());
        }
    }
}

