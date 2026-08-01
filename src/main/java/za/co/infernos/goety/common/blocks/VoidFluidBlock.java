package za.co.infernos.goety.common.blocks;

import za.co.infernos.goety.common.blocks.fluids.ModFluids;
import za.co.infernos.goety.utils.BlockFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class VoidFluidBlock extends LiquidBlock {

    public VoidFluidBlock(FlowingFluid fluid) {
        super(fluid, Properties.of().mapColor(MapColor.COLOR_PURPLE)
                .noCollission()
                .strength(100.0F)
                .emissiveRendering((state, world, pos) -> true)
                .lightLevel((state) -> 1)
                .noLootTable()
                .replaceable()
                .liquid()
                .pushReaction(PushReaction.DESTROY));
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity livingEntity) {
            BlockFinder.voidedEffect(pLevel, livingEntity);
        }
    }
}