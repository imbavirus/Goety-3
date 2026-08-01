package za.co.infernos.goety.common.magic.spells.wild;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.BearServant;
import za.co.infernos.goety.common.entities.ally.Gnasher;
import za.co.infernos.goety.common.entities.ally.HoglinServant;
import za.co.infernos.goety.common.entities.ally.Summoned;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.magic.SummonSpell;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MaulingSpell extends SummonSpell {
    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaulingCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaulingDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.WILD_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaulingCoolDown, 0);
    }

    @Override
    public int SummonDownDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaulingSummonDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WILD;
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
        return livingEntity -> livingEntity instanceof BearServant || livingEntity instanceof Gnasher || livingEntity instanceof HoglinServant;
    }

    @Override
    public int summonLimit() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaulingLimit, 0);
    }

    @Override
    public void commonResult(ServerLevel worldIn, LivingEntity caster) {
        if (isShifting(caster)) {
            for (Entity entity : worldIn.getAllEntities()) {
                if (entity instanceof LivingEntity livingEntity && summonPredicate().test(livingEntity)) {
                    if (livingEntity instanceof Gnasher){
                        if (caster.isUnderWater()){
                            this.teleportServants(caster, entity);
                        }
                    } else {
                        this.teleportServants(caster, entity);
                    }
                }
            }
            this.commonResultHit(worldIn, caster);
        }
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
                Summoned summonedentity = new BearServant(ModEntityType.BEAR_SERVANT.get(), worldIn);
                BlockPos blockPos = BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn);
                if (caster.isUnderWater()){
                    blockPos = BlockFinder.SummonWaterRadius(caster, worldIn);
                }
                if (typeStaff(staff, SpellType.NETHER) || worldIn.dimension() == Level.NETHER){
                    summonedentity = new HoglinServant(ModEntityType.HOGLIN_SERVANT.get(), worldIn);
                } else if (worldIn.isWaterAt(blockPos) || typeStaff(staff, SpellType.ABYSS)){
                    summonedentity = new Gnasher(ModEntityType.GNASHER.get(), worldIn);
                } else if (typeStaff(staff, SpellType.FROST) || worldIn.getBiome(blockPos).is(Tags.Biomes.IS_COLD_OVERWORLD)) {
                    summonedentity = new BearServant(ModEntityType.POLAR_BEAR_SERVANT.get(), worldIn);
                } else if (blockPos.getY() <= 64 && !worldIn.canSeeSky(blockPos) && summonedentity instanceof BearServant bearServant){
                    bearServant.setBearCave();
                }
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(blockPos, 0.0F, 0.0F);
                if (!caster.isUnderWater()){
                    MobUtil.moveDownToGround(summonedentity);
                }
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
            this.playSound(worldIn, caster, ModSounds.SUMMON_SPELL.get());
        }
    }
}
