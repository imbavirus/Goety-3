package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.common.entities.ally.spider.SpiderServant;
import za.co.infernos.goety.common.ritual.RitualRequirements;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.ServerParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

public class SpiderNestBlockEntity extends TrainingBlockEntity {

    public SpiderNestBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.SPIDER_NEST.get(), p_155229_, p_155230_);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, TrainingBlockEntity blockEntity) {
        super.tick(level, blockPos, blockState, blockEntity);
        if (this.isTraining()){
            if (this.trainTime != this.getMaxTrainTime()){
                if (level instanceof ServerLevel serverLevel) {
                    if (level.random.nextInt(10) == 0) {
                        ServerParticleUtil.blockBreakParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), blockPos, blockState, serverLevel);
                    }
                }
                if (this.trainTime == 20){
                    level.playSound(null, this.getBlockPos(), ModSounds.SPIDER_NEST_TRAIN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            } else {
                if (level instanceof ServerLevel serverLevel) {
                    if (level.random.nextInt(10) == 0) {
                        double d0 = (double)this.worldPosition.getX() + level.random.nextDouble();
                        double d1 = (double)this.worldPosition.getY() + level.random.nextDouble();
                        double d2 = (double)this.worldPosition.getZ() + level.random.nextDouble();
                        serverLevel.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
    }

    @Override
    public void setVariant(ItemStack itemStack, Level level, BlockPos blockPos) {
        if (level instanceof ServerLevel serverLevel) {
            if (BlockFinder.findStructure(serverLevel, blockPos, ModTags.Structures.CRYPT)
                    || (getBlocks(blockState -> blockState.is(Blocks.BONE_BLOCK), 16)
                    && getBlocks(blockState -> blockState.is(ModBlocks.SKULL_PILE.get()), 4))) {
                if (this.getTrainMob() != ModEntityType.BONE_SPIDER_SERVANT.get()) {
                    this.setEntityType(ModEntityType.BONE_SPIDER_SERVANT.get());
                    this.markUpdated();
                }
            } else if (RitualRequirements.frostRitual(null, blockPos, level)){
                if (this.getTrainMob() != ModEntityType.ICY_SPIDER_SERVANT.get()){
                    this.setEntityType(ModEntityType.ICY_SPIDER_SERVANT.get());
                    this.markUpdated();
                }
            } else if (BlockFinder.findStructure(serverLevel, blockPos, StructureTags.MINESHAFT)
                    || (getBlocks(blockState -> blockState.is(Blocks.PODZOL), 8)
                    && getBlocks(blockState -> blockState.getBlock() instanceof MushroomBlock, 8)
                    && getBlocks(blockState -> blockState.is(Tags.Blocks.STONES), 8))){
                if (this.getTrainMob() != ModEntityType.CAVE_SPIDER_SERVANT.get()){
                    this.setEntityType(ModEntityType.CAVE_SPIDER_SERVANT.get());
                    this.markUpdated();
                }
            } else if (getBlocks(blockState -> blockState.is(BlockTags.LEAVES), 16)
                    && getBlocks(blockState -> blockState.getBlock() instanceof WebBlock, 8)) {
                if (this.getTrainMob() != ModEntityType.WEB_SPIDER_SERVANT.get()) {
                    this.setEntityType(ModEntityType.WEB_SPIDER_SERVANT.get());
                    this.markUpdated();
                }
            } else {
                if (this.getTrainMob() != ModEntityType.SPIDER_SERVANT.get()) {
                    this.setEntityType(ModEntityType.SPIDER_SERVANT.get());
                    this.markUpdated();
                }
            }
        }
    }

    public void startTraining(int amount, ItemStack itemStack){
        super.startTraining(amount, itemStack);
        if (this.getLevel() != null) {
            this.getLevel().playSound(null, this.getBlockPos(), ModSounds.SPIDER_NEST_START.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void playSpawnSound() {
        if (this.getLevel() != null) {
            this.getLevel().playSound(null, this.getBlockPos(), ModSounds.SPIDER_NEST_SPAWN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
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
                if (entity instanceof SpiderServant servant) {
                    if (this.getTrueOwner() != null && servant.getTrueOwner() == this.getTrueOwner() && servant.isAlive()) {
                        ++count;
                    }
                }
            }
        }
        return count >= za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.SpiderLimit, 0);
    }

    @Override
    public boolean isFuel(ItemStack itemStack) {
        return itemStack.has(net.minecraft.core.component.DataComponents.FOOD) && this.getPlayer() != null && itemStack.getItem().getFoodProperties(itemStack, this.getPlayer()).nutrition() > 0;
    }
}
