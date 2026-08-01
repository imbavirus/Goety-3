package za.co.infernos.goety.common.magic.spells.void_spells;

import za.co.infernos.goety.api.magic.SpellType;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.entities.HoleBlockEntity;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.magic.BlockSpell;
import za.co.infernos.goety.common.magic.SpellStat;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TunnelSpell extends BlockSpell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRange(za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.TunnelDefaultDistance, 0));
    }

    @Override
    public int defaultSoulCost() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.TunnelCost, 0);
    }

    @Nullable
    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.TunnelCoolDown, 0);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.VOID;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public boolean rightBlock(ServerLevel worldIn, LivingEntity caster, BlockPos target, Direction direction, SpellStat spellStat) {
        BlockState blockState = worldIn.getBlockState(target);
        BlockState blockState2 = worldIn.getBlockState(target.relative(direction));
        return !blockState.hasBlockEntity() && blockState2.getBlock() != ModBlocks.HOLE.get() && !blockState.is(ModTags.Blocks.TUNNEL_BLACKLIST) && blockState.getDestroySpeed(worldIn, target) != -1.0F;
    }

    @Override
    public void blockResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, BlockPos target, Direction direction, SpellStat spellStat) {
        BlockHitResult blockHitResult = MobUtil.rayTrace(caster, 8, false);
        BlockPos blockPos = new BlockPos(blockHitResult.getBlockPos());
        int totalDistance = spellStat.getRange();
        int extraLife = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            totalDistance += WandUtil.getRangeLevel(caster);
            extraLife += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }
        for (int distance = 0; distance < totalDistance; ++distance) {
            BlockState blockState = worldIn.getBlockState(blockPos);
            if (blockState.is(ModTags.Blocks.TUNNEL_BLACKLIST) || !blockState.getFluidState().isEmpty() || blockState.getBlock() == ModBlocks.HOLE.get() || blockState.isAir()) {
                break;
            }
            if (blockState.getDestroySpeed(worldIn, blockPos) == -1.0F) {
                break;
            }
            blockPos = blockPos.relative(direction.getOpposite());
        }
        createHole(worldIn, blockHitResult.getBlockPos(), direction, (byte)Math.round((float)(totalDistance + 1)), this.rightStaff(staff), za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.TunnelDefaultLifespan, 0) + (extraLife * 20));
        this.playSound(worldIn, caster, ModSounds.CAST_SPELL.get());
    }

    public static boolean createHole(Level world, BlockPos blockPos, Direction direction, int count, boolean staff, int lifespan) {
        BlockState blockState = world.getBlockState(blockPos);
        if (!world.isClientSide && world.getBlockEntity(blockPos) == null
                && !blockState.is(ModTags.Blocks.TUNNEL_BLACKLIST)
                && blockState.getFluidState().isEmpty()
                && blockState.getBlock() != ModBlocks.HOLE.get()
                && blockState.getDestroySpeed(world, blockPos) != -1.0F) {
            if (world.setBlockAndUpdate(blockPos, ModBlocks.HOLE.get().defaultBlockState())) {
                HoleBlockEntity newHole = (HoleBlockEntity)world.getBlockEntity(blockPos);
                if (newHole != null) {
                    newHole.setStats(blockState, lifespan, count, staff ? 5 : 3, direction);
                    return true;
                }
            }
        }
        return false;
    }
}