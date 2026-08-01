package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.blocks.entities.IOwnedBlock;
import za.co.infernos.goety.utils.EntityFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;
import net.minecraft.core.HolderLookup;

public abstract class OwnedBlockEntity extends BlockEntity implements IOwnedBlock {
    private UUID ownerUUID;
    private int ownerID;

    public OwnedBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.writeNetwork(super.getUpdateTag(registries), registries);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.readNetwork(nbt, registries);
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        this.writeNetwork(compound, registries);
    }

    public void readNetwork(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.hasUUID("Owner")) {
            this.setOwnerUUID(tag.getUUID("Owner"));
        }
        if (tag.contains("OwnerID")){
            this.setOwnerId(tag.getInt("OwnerID"));
        }
    }

    public CompoundTag writeNetwork(CompoundTag tag, HolderLookup.Provider registries) {
        if (this.getOwnerUUID() != null) {
            tag.putUUID("Owner", this.getOwnerUUID());
        }
        tag.putInt("OwnerID", this.getOwnerId());
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void setOwnerUUID(@Nullable UUID p_184754_1_) {
        this.ownerUUID = p_184754_1_;
    }

    public int getOwnerId() {
        return this.ownerID;
    }

    public void setOwnerId(int p_184754_1_) {
        this.ownerID = p_184754_1_;
    }

    public void setOwner(LivingEntity livingEntity){
        this.setOwnerUUID(livingEntity.getUUID());
        this.setOwnerId(livingEntity.getId());
    }

    @Nullable
    public LivingEntity getTrueOwner() {
        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide){
                UUID uuid = this.getOwnerUUID();
                return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
            } else {
                int id = this.getOwnerId();
                return id <= -1 ? null : this.getLevel().getEntity(this.getOwnerId()) instanceof LivingEntity living ? living : null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public Player getPlayer(){
        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                if (this.getTrueOwner() instanceof Player player) {
                    return player;
                }
            } else {
                if (this.getOwnerUUID() != null) {
                    return this.getLevel().getPlayerByUUID(this.getOwnerUUID());
                }
            }
        }
        return null;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        if (pkt.getTag() != null) {
            this.readNetwork(pkt.getTag(), registries);
        }
    }

    public boolean screenView(){
        return true;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.readNetwork(tag, registries);
    }
}
