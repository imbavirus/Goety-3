package za.co.infernos.goety.common.magic.spells.wild;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.CryptSlimeServant;
import za.co.infernos.goety.common.entities.ally.MagmaCubeServant;
import za.co.infernos.goety.common.entities.ally.SlimeServant;
import za.co.infernos.goety.common.entities.ally.TropicalSlimeServant;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.magic.SummonSpell;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SlimySpell extends SummonSpell {

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SlimyCost, 0);
    }

    @Override
    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SlimyDuration, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.WILD_PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SlimyCoolDown, 0);
    }

    @Override
    public int SummonDownDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SlimySummonDown, 0);
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
        return livingEntity -> livingEntity instanceof SlimeServant;
    }

    @Override
    public int summonLimit() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SlimyLimit, 0);
    }

    public boolean specialStaffs(ItemStack stack){
        return typeStaff(stack, SpellType.NECROMANCY)
                || typeStaff(stack, SpellType.NETHER)
                || typeStaff(stack, SpellType.ABYSS);
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
                i = 2 + caster.level().random.nextInt(2);
            } else if (specialStaffs(staff)){
                i = 2;
            }
            for (int i1 = 0; i1 < i; ++i1) {
                SlimeServant slimeServant = new SlimeServant(ModEntityType.SLIME_SERVANT.get(), worldIn);
                BlockPos blockPos = BlockFinder.SummonRadius(caster.blockPosition(), slimeServant, worldIn);
                if (caster.isUnderWater()){
                    blockPos = BlockFinder.SummonWaterRadius(caster, worldIn);
                }
                if (specialStaffs(staff)) {
                    if (typeStaff(staff, SpellType.ABYSS)) {
                        slimeServant = new TropicalSlimeServant(ModEntityType.TROPICAL_SLIME_SERVANT.get(), worldIn);
                    } else if (typeStaff(staff, SpellType.NECROMANCY)) {
                        slimeServant = new CryptSlimeServant(ModEntityType.CRYPT_SLIME_SERVANT.get(), worldIn);
                    } else if (typeStaff(staff, SpellType.NETHER)) {
                        slimeServant = new MagmaCubeServant(ModEntityType.MAGMA_CUBE_SERVANT.get(), worldIn);
                    }
                } else {
                    if (worldIn.isWaterAt(blockPos)) {
                        slimeServant = new TropicalSlimeServant(ModEntityType.TROPICAL_SLIME_SERVANT.get(), worldIn);
                    } else if (worldIn.dimension() == Level.NETHER) {
                        slimeServant = new MagmaCubeServant(ModEntityType.MAGMA_CUBE_SERVANT.get(), worldIn);
                    } else if (BlockFinder.findStructure(worldIn, blockPos, ModTags.Structures.CRYPT)) {
                        slimeServant = new CryptSlimeServant(ModEntityType.CRYPT_SLIME_SERVANT.get(), worldIn);
                    }
                }
                slimeServant.setTrueOwner(caster);
                slimeServant.moveTo(blockPos, 0.0F, 0.0F);
                if (slimeServant.getType() != ModEntityType.TROPICAL_SLIME_SERVANT.get()){
                    MobUtil.moveDownToGround(slimeServant);
                }
                slimeServant.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                slimeServant.setPersistenceRequired();
                slimeServant.finalizeSpawn(worldIn, caster.level().getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED,null);
                slimeServant.setSize(2, true);
                this.buffSummon(caster, slimeServant, potency);
                this.SummonSap(caster, slimeServant);
                this.setTarget(caster, slimeServant);
                worldIn.addFreshEntity(slimeServant);
                this.summonAdvancement(caster, slimeServant);
            }
            this.SummonDown(caster);
            this.playSound(worldIn, caster, ModSounds.SUMMON_SPELL.get());
        }
    }
}

