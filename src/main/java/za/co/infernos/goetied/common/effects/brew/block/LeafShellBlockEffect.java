package za.co.infernos.goetied.common.effects.brew.block;

import za.co.infernos.goetied.common.blocks.ModBlocks;
import za.co.infernos.goetied.common.effects.brew.BrewEffect;
import za.co.infernos.goetied.config.BrewConfig;
import za.co.infernos.goetied.utils.BlockFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;

public class LeafShellBlockEffect extends BrewEffect {
    public LeafShellBlockEffect() {
        super("leaf_shell", za.co.infernos.goetied.utils.ConfigHelper.getInt(BrewConfig.LeafShellCost, 0), MobEffectCategory.NEUTRAL, 0x286240);
    }

    @Override
    public void applyBlockEffect(Level pLevel, BlockPos pPos, LivingEntity pSource, int pAmplifier, int pAreaOfEffect) {
        for (BlockPos blockPos : this.getHollowSphere(pPos, pAreaOfEffect + 3)){
            if (pLevel.getBlockState(blockPos).isAir() || BlockFinder.canBeReplaced(pLevel, blockPos)){
                pLevel.setBlockAndUpdate(blockPos, ModBlocks.HARDENED_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, Boolean.TRUE));
            }
        }
    }
}