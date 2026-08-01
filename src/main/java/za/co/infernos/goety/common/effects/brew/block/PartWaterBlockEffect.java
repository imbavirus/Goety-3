package za.co.infernos.goety.common.effects.brew.block;

import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.entities.PartLiquidBlockEntity;
import za.co.infernos.goety.common.effects.brew.BrewEffect;
import za.co.infernos.goety.config.BrewConfig;
import za.co.infernos.goety.utils.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PartWaterBlockEffect extends BrewEffect {
    public PartWaterBlockEffect() {
        super("part_water", za.co.infernos.goety.utils.ConfigHelper.getInt(BrewConfig.PartWaterCost, 0), MobEffectCategory.NEUTRAL, 0x405ce2);
        this.duration = MathHelper.secondsToTicks(30);
    }

    @Override
    public void applyBlockEffect(Level pLevel, BlockPos pPos, LivingEntity pSource, int pDuration, int pAmplifier, int pAreaOfEffect) {
        for (BlockPos blockPos : this.getSpherePos(pPos, pAreaOfEffect + 5)){
            if (pLevel.getFluidState(blockPos).is(FluidTags.WATER)){
                BlockState blockState = pLevel.getBlockState(blockPos);
                if (pLevel.setBlockAndUpdate(blockPos, ModBlocks.PART_LIQUID.get().defaultBlockState())) {
                    PartLiquidBlockEntity blockEntity = (PartLiquidBlockEntity)pLevel.getBlockEntity(blockPos);
                    if (blockEntity != null) {
                        blockEntity.setStats(blockState, 0, pDuration);
                    }
                }
            }
        }
    }
}