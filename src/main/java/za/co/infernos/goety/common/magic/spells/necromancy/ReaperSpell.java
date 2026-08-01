package za.co.infernos.goety.common.magic.spells.necromancy;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.neutral.AbstractReaper;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.magic.SummonSpell;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.SoundUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ReaperSpell extends SummonSpell {

    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ReaperCost, 0);
    }

    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ReaperDuration, 0);
    }

    public int SummonDownDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ReaperSummonDown, 0);
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SUMMON.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ReaperCoolDown, 0);
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
        return livingEntity -> livingEntity instanceof AbstractReaper;
    }

    @Override
    public int summonLimit() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.ReaperLimit, 0);
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
            int i = 1;
            if (rightStaff(staff)){
                i = 2 + caster.level().random.nextInt(4);
            }
            for (int i1 = 0; i1 < i; ++i1) {
                AbstractReaper summonedentity = new AbstractReaper(ModEntityType.REAPER_SERVANT.get(), worldIn);
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn), 0.0F, 0.0F);
                MobUtil.moveDownToGround(summonedentity);
                summonedentity.setPersistenceRequired();
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.finalizeSpawn(worldIn, caster.level().getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
                this.buffSummon(caster, summonedentity, potency);
                this.SummonSap(caster, summonedentity);
                this.setTarget(caster, summonedentity);
                if (worldIn.addFreshEntity(summonedentity)) {
                    this.summonParticles(worldIn, caster, staff, summonedentity);
                }
                this.summonAdvancement(caster, summonedentity);
            }
            this.SummonDown(caster);
            SoundUtil.playNecromancerSummon(caster);
        }
    }
}

