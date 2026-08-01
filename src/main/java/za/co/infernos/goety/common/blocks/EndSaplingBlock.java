package za.co.infernos.goety.common.blocks;

import za.co.infernos.goety.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import za.co.infernos.goety.compat.legacy.neoforge.common.IPlantable;

public class EndSaplingBlock extends SaplingBlock {
    public EndSaplingBlock(TreeGrower p_55978_, Properties p_55979_) {
        super(p_55978_, p_55979_);
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return state.is(ModTags.Blocks.CHORUS_SAPLING_GROW);
    }

    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return state.is(ModTags.Blocks.CHORUS_SAPLING_GROW);
    }
}