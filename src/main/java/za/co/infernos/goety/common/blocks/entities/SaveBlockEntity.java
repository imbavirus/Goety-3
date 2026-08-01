package za.co.infernos.goety.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SaveBlockEntity extends BlockEntity {
    public BlockState oldBlock;
    public BlockEntity oldBlockEntity;

    public SaveBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public void recreateBlockEntity(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (this.getLevel() != null) {
            if (this.oldBlockEntity != null && this.getLevel().getBlockEntity(this.getBlockPos()) != null) {
                this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.oldBlock);
                if (this.getLevel().getBlockEntity(this.getBlockPos()) != null){
                    BlockEntity blockEntity = this.getLevel().getBlockEntity(this.getBlockPos());
                    if (blockEntity != null) {
                        blockEntity.loadWithComponents(tag, pRegistries);
                    }
                }
            }
        }
        this.setChanged();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.writeNetwork(super.getUpdateTag(pRegistries), pRegistries);
    }

    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        this.readNetwork(nbt, pRegistries);
        super.loadAdditional(nbt, pRegistries);
    }

    public void saveAdditional(CompoundTag compound, HolderLookup.Provider pRegistries) {
        this.writeNetwork(compound, pRegistries);
        super.saveAdditional(compound, pRegistries);
    }

    public void readNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (tag.contains("BlockState")) {
            this.oldBlock = NbtUtils.readBlockState(pRegistries.lookupOrThrow(Registries.BLOCK), tag.getCompound("BlockState"));
        }
        if (tag.contains("BlockEntity")){
            this.recreateBlockEntity(tag.getCompound("BlockEntity"), pRegistries);
        }
    }

    public CompoundTag writeNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (this.oldBlock != null) {
            tag.put("BlockState", NbtUtils.writeBlockState(this.oldBlock));
        }
        if (this.oldBlockEntity != null){
            tag.put("BlockEntity", this.oldBlockEntity.saveWithFullMetadata(pRegistries));
        }
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
