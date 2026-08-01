package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.items.magic.ITotem;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class CursedCageBlockEntity extends BlockEntity implements Clearable {
    private ItemStack item = ItemStack.EMPTY;
    private int spinning;

    public CursedCageBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CURSED_CAGE.get(), blockPos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        this.readNetwork(compound, provider);
        super.loadAdditional(compound, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        this.writeNetwork(compound, provider);
        super.saveAdditional(compound, provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack stack) {
        this.item = stack;
        this.setChanged();
    }

    public Player getOwner(){
        if (this.getLevel() != null && this.item.getItem() == ModItems.SOUL_TRANSFER.get()) {
            CustomData data = this.item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            if (!data.isEmpty() && data.contains("owner")) {
                UUID owner = data.copyTag().getUUID("owner");
                return this.getLevel().getPlayerByUUID(owner);
            }
        }
        return null;
    }

    public int getSouls(){
        if (this.getLevel() != null) {
            Player player = this.getOwner();
            if (player != null) {
                if (SEHelper.getSEActive(player)) {
                    return SEHelper.getSESouls(player);
                }
            }
            if (this.item.getItem() instanceof ITotem) {
                return ITotem.currentSouls(this.item);
            }
        }
        return 0;
    }

    public void decreaseSouls(int souls) {
        if (this.item.getItem() instanceof ITotem) {
            ITotem.decreaseSouls(this.item, souls);
            this.generateParticles();
        }
        if (this.getLevel() != null) {
            Player player = this.getOwner();
            if (player != null) {
                if (SEHelper.getSEActive(player)) {
                    int Soulcount = SEHelper.getSESouls(player);
                    if (Soulcount > 0) {
                        SEHelper.decreaseSESouls(player, souls);
                        SEHelper.sendSEUpdatePacket(player);
                        ArcaBlockEntity arcaTile = (ArcaBlockEntity) this.getLevel().getBlockEntity(SEHelper.getArcaBlock(player));
                        if (arcaTile != null) {
                            arcaTile.generateParticles();
                            this.generateParticles();
                        }
                    }
                }
            }
        }
        this.markUpdated();

    }

    public int getSpinning(){
        return this.spinning;
    }

    public void generateParticles() {
        if (this.getSouls() <= 0){
            return;
        }
        BlockPos blockpos = this.getBlockPos();

        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                ServerLevel serverWorld = (ServerLevel) this.getLevel();
                double d0 = (double) blockpos.getX() + this.getLevel().random.nextDouble();
                double d1 = (double) blockpos.getY() + this.getLevel().random.nextDouble();
                double d2 = (double) blockpos.getZ() + this.getLevel().random.nextDouble();
                for (int p = 0; p < 4; ++p) {
                    serverWorld.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, 0, 0, 0, 0);
                    serverWorld.sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0.0D, 5.0E-4D, 0.0D, 5.0E-4D);
                }
            }
            this.spinning = 20;
        }
    }

    public void generateManyParticles(){
        BlockPos blockpos = this.getBlockPos();
        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                ServerLevel serverWorld = (ServerLevel) this.getLevel();
                for(int k = 0; k < 20; ++k) {
                    double d9 = (double)blockpos.getX() + 0.5D + (this.getLevel().random.nextDouble() - 0.5D) * 2.0D;
                    double d13 = (double)blockpos.getY() + 0.5D + (this.getLevel().random.nextDouble() - 0.5D) * 2.0D;
                    double d19 = (double)blockpos.getZ() + 0.5D + (this.getLevel().random.nextDouble() - 0.5D) * 2.0D;
                    serverWorld.sendParticles(ParticleTypes.SMOKE, d9, d13, d19, 1, 0.0D, 0.0D, 0.0D, 0);
                    serverWorld.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d9, d13, d19, 1, 0.0D, 0.0D, 0.0D, 0);
                }
            }
        }

    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.writeNetwork(super.getUpdateTag(provider), provider);
    }

    public void readNetwork(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries) {
        item = ItemStack.parseOptional(pRegistries, tag.getCompound("item"));
    }

    public CompoundTag writeNetwork(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries) {
        if (!item.isEmpty()) {
            tag.put("item", item.save(pRegistries, new CompoundTag()));
        }
        return tag;
    }

    public void clearContent() {
        this.setItem(ItemStack.EMPTY);
    }

    public void tick() {
        if (this.spinning > 0){
            --this.spinning;
        }
    }

    public void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
