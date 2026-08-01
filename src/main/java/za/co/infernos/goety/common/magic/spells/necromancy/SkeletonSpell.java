package za.co.infernos.goety.common.magic.spells.necromancy;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.undead.skeleton.*;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.common.magic.SummonSpell;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.SoundUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SkeletonSpell extends SummonSpell {

    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SkeletonCost, 0);
    }

    public int defaultCastDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SkeletonDuration, 0);
    }

    public int SummonDownDuration() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SkeletonSummonDown, 0);
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SUMMON.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SkeletonCoolDown, 0);
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
        return livingEntity -> livingEntity instanceof AbstractSkeletonServant;
    }

    @Override
    public int summonLimit() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SkeletonLimit, 0);
    }

    public boolean specialStaffs(ItemStack stack){
        return typeStaff(stack, SpellType.FROST)
                || typeStaff(stack, SpellType.WILD)
                || typeStaff(stack, SpellType.NETHER)
                || typeStaff(stack, SpellType.ABYSS)
                || stack.is(ModItems.OMINOUS_STAFF.get());
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        this.commonResult(worldIn, caster);
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)){
            potency += WandUtil.getPotencyLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
        }
        int i = 1;
        if (staff.is(ModItems.NAMELESS_STAFF.get())){
            i = 7;
        } else if (rightStaff(staff)){
            i = 2 + caster.level().random.nextInt(4);
        } else if (specialStaffs(staff)){
            i = 2;
        }
        if (!isShifting(caster)) {
            for (int i1 = 0; i1 < i; ++i1) {
                EntityType<?> entityType;
                AbstractSkeletonServant summonedentity = new SkeletonServant(ModEntityType.SKELETON_SERVANT.get(), worldIn);
                BlockPos blockPos = BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn);
                if (caster.isUnderWater()){
                    blockPos = BlockFinder.SummonWaterRadius(caster, worldIn);
                }
                if (specialStaffs(staff)) {
                    if (typeStaff(staff, SpellType.FROST)) {
                        summonedentity = new StrayServant(ModEntityType.STRAY_SERVANT.get(), worldIn);
                    } else if (typeStaff(staff, SpellType.WILD)) {
                        summonedentity = new MossySkeletonServant(ModEntityType.MOSSY_SKELETON_SERVANT.get(), worldIn);
                    } else if (typeStaff(staff, SpellType.NETHER)) {
                        summonedentity = new WitherSkeletonServant(ModEntityType.WITHER_SKELETON_SERVANT.get(), worldIn);
                    } else if (typeStaff(staff, SpellType.ABYSS)) {
                        summonedentity = new SunkenSkeletonServant(ModEntityType.SUNKEN_SKELETON_SERVANT.get(), worldIn);
                    } else if (staff.is(ModItems.OMINOUS_STAFF.get())) {
                        summonedentity = new SkeletonPillagerServant(ModEntityType.SKELETON_PILLAGER_SERVANT.get(), worldIn);
                    }
                } else {
                    Player player = null;
                    if (caster instanceof Player player1){
                        player = player1;
                    }
                    entityType = summonedentity.getVariant(player, worldIn, blockPos);
                    if (entityType != null){
                        Entity entity = entityType.create(worldIn);
                        if (entity instanceof AbstractSkeletonServant summoned){
                            summonedentity = summoned;
                        }
                    }
                }
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(blockPos, 0.0F, 0.0F);
                if (summonedentity.getType() != ModEntityType.SUNKEN_SKELETON_SERVANT.get()){
                    MobUtil.moveDownToGround(summonedentity);
                }
                summonedentity.setPersistenceRequired();
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.setArrowPower(potency);
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

