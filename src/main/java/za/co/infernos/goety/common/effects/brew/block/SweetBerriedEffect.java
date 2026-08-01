package za.co.infernos.goety.common.effects.brew.block;

import za.co.infernos.goety.common.effects.brew.BrewEffect;
import za.co.infernos.goety.config.BrewConfig;
import za.co.infernos.goety.utils.BlockFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;

public class SweetBerriedEffect extends BrewEffect {
    public SweetBerriedEffect() {
        super("sweet_thorns", za.co.infernos.goety.utils.ConfigHelper.getInt(BrewConfig.SweetThornsCost, 0), MobEffectCategory.HARMFUL, 0x286240);
    }

    @Override
    public void applyBlockEffect(Level pLevel, BlockPos pPos, LivingEntity pSource, int pAmplifier, int pAreaOfEffect) {
        for (BlockPos blockPos : this.getSpherePos(pPos, pAreaOfEffect + 2)){
            if ((pLevel.getBlockState(blockPos).isAir() || BlockFinder.canBeReplaced(pLevel, blockPos))
                    && Blocks.SWEET_BERRY_BUSH.defaultBlockState().canSurvive(pLevel, blockPos.below())
                    && pLevel.getBlockState(blockPos.below()).isSolidRender(pLevel, blockPos.below())) {
                pLevel.setBlockAndUpdate(blockPos, Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, pAmplifier));
            }
        }
    }
}