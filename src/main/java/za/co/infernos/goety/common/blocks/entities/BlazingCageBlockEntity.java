package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.neutral.BlazeServant;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlazingCageBlockEntity extends TrainingBlockEntity {

    public BlazingCageBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.BLAZING_CAGE.get(), p_155229_, p_155230_);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, TrainingBlockEntity blockEntity) {
        super.tick(level, blockPos, blockState, blockEntity);
        if (blockEntity.isTraining()){
            if (blockEntity.trainTime != blockEntity.getMaxTrainTime()){
                if (level instanceof ServerLevel serverLevel) {
                    double d0 = (double)blockPos.getX() + level.random.nextDouble();
                    double d1 = (double)blockPos.getY() + level.random.nextDouble();
                    double d2 = (double)blockPos.getZ() + level.random.nextDouble();
                    serverLevel.sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    serverLevel.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
                if (this.trainTime == 20){
                    level.playSound(null, blockPos, ModSounds.BLAZING_CAGE_TRAIN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            } else {
                if (level instanceof ServerLevel serverLevel) {
                    double d0 = (double)blockPos.getX() + level.random.nextDouble();
                    double d1 = (double)blockPos.getY() + level.random.nextDouble();
                    double d2 = (double)blockPos.getZ() + level.random.nextDouble();
                    serverLevel.sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    serverLevel.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public void setVariant(ItemStack itemStack, Level level, BlockPos blockPos) {
        if (level instanceof ServerLevel) {
            if (this.getTrainMob() != ModEntityType.BLAZE_SERVANT.get()) {
                this.setEntityType(ModEntityType.BLAZE_SERVANT.get());
                this.markUpdated();
            }
        }
    }

    public void startTraining(int amount, ItemStack itemStack){
        super.startTraining(amount, itemStack);
        if (this.getLevel() != null) {
            this.getLevel().playSound(null, this.getBlockPos(), ModSounds.BLAZING_CAGE_START.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void playSpawnSound() {
        if (this.getLevel() != null) {
            this.getLevel().playSound(null, this.getBlockPos(), ModSounds.SUMMON_SPELL_FIERY.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public int maxTrainAmount() {
        return 5;
    }

    @Override
    public boolean summonLimit() {
        int count = 0;
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            for (Entity entity : serverLevel.getAllEntities()) {
                if (entity instanceof BlazeServant servant) {
                    if (this.getTrueOwner() != null && servant.getTrueOwner() == this.getTrueOwner() && servant.isAlive()) {
                        ++count;
                    }
                }
            }
        }
        return count >= za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.BlazeLimit, 0);
    }

    @Override
    public boolean isFuel(ItemStack itemStack) {
        return itemStack.is(Items.MAGMA_BLOCK) || itemStack.is(Items.LAVA_BUCKET);
    }
}
