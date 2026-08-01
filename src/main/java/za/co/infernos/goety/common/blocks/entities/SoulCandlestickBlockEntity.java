package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.NecroBrazierBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulCandlestickBlockEntity extends BlockEntity{
    private CursedCageBlockEntity cursedCageTile;

    public SoulCandlestickBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.SOUL_CANDLESTICK.get(), p_155229_, p_155230_);
    }

    public void tick(){
        if (this.getLevel() != null){
            boolean flag = this.checkCage() && this.cursedCageTile.getSouls() > 0;
            this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(NecroBrazierBlock.LIT, flag), 3);
        }
    }

    public void drainSouls(int amount, BlockPos blockPos){
        if (this.getLevel() != null){
            if (this.checkCage()) {
                if (this.cursedCageTile.getSouls() > amount) {
                    this.cursedCageTile.decreaseSouls(amount);
                    double d0 = 0.1D * (blockPos.getX() - this.getBlockPos().getX());
                    double d1 = 0.1D * (blockPos.getY() - this.getBlockPos().getY());
                    double d2 = 0.1D * (blockPos.getZ() - this.getBlockPos().getZ());
                    if (this.getLevel() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ModParticleTypes.SOUL_EXPLODE_BITS.get(), this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.75D, this.getBlockPos().getZ() + 0.5D, 0, d0, d1, d2, 0.5D);
                    }
                }
            }
        }
    }

    public int getSouls(){
        if (this.getLevel() != null){
            if (this.checkCage()){
                return this.cursedCageTile.getSouls();
            }
        }
        return 0;
    }

    private boolean checkCage() {
        if (this.getLevel() == null){
            return false;
        }
        BlockPos pos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
        BlockState blockState = this.getLevel().getBlockState(pos);
        if (blockState.is(ModBlocks.CURSED_CAGE_BLOCK.get())){
            BlockEntity tileentity = this.getLevel().getBlockEntity(pos);
            if (tileentity instanceof CursedCageBlockEntity){
                this.cursedCageTile = (CursedCageBlockEntity) tileentity;
                return !cursedCageTile.getItem().isEmpty();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
