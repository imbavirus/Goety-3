package za.co.infernos.goety.common.blocks;

import za.co.infernos.goety.common.blocks.entities.ModSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWallSignBlock extends WallSignBlock {
    public ModWallSignBlock(WoodType type, Properties properties) {
        super(type, properties);
    }

    public BlockEntity newBlockEntity(BlockPos p_154556_, BlockState p_154557_) {
        return new ModSignBlockEntity(p_154556_, p_154557_);
    }
}