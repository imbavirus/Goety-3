package za.co.infernos.goety.mixin;

import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.VoidFlameBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {

    @Inject(at = @At("HEAD"), method = "getState", cancellable = true)
    private static void getState(BlockGetter reader, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = reader.getBlockState(blockpos);
        if (VoidFlameBlock.canSurviveOnBlock(blockstate)) {
            cir.cancel();
            cir.setReturnValue(ModBlocks.VOID_FLAME.get().defaultBlockState());
        }
    }
}