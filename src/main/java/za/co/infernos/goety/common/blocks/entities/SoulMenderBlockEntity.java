package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.items.magic.ITotem;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.SoulMenderBlock;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SoulMenderBlockEntity extends ModBlockEntity implements Clearable, WorldlyContainer {
    private static final int[] SLOTS = new int[]{0};
    private ItemStack itemStack = ItemStack.EMPTY;
    private CursedCageBlockEntity cursedCageTile;

    public SoulMenderBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.SOUL_MENDER.get(), p_155229_, p_155230_);
    }

    public void tick() {
        boolean flag = this.checkCage();
        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                if (flag) {
                    if (!this.itemStack.isEmpty()) {
                        int i = 1;
                        if (!net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(this.itemStack).isEmpty()) {
                            i += net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(this.itemStack).size();
                        }
                        if (this.cursedCageTile.getSouls() > (za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SoulMenderCost, 0) * i)) {
                            this.makeWorkParticles();
                        }
                    }
                    this.work();
                }
            }
            this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SoulMenderBlock.LIT, flag), 3);
        }
    }

    private void work() {
        if (this.getLevel() != null) {
            if (!this.itemStack.isEmpty()) {
                int i = 1;
                if (!net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(this.itemStack).isEmpty()) {
                    i += net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(this.itemStack).size();
                }
                if (this.cursedCageTile.getSouls() > (za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SoulMenderCost, 0) * i)) {
                    if (this.itemStack.getItem() instanceof ITotem){
                        if (!ITotem.isFull(this.itemStack)) {
                            if (this.getLevel().getGameTime() % (MathHelper.secondsToTicks(za.co.infernos.goety.utils.ConfigHelper.getFloat(MainConfig.SoulMenderSeconds, 1.0F)) + 1) == 0) {
                                ITotem.increaseSouls(this.itemStack, 1);
                                this.cursedCageTile.decreaseSouls(1);
                            }
                            if (this.getLevel().random.nextInt(24) == 0) {
                                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + this.getLevel().random.nextFloat(), this.getLevel().random.nextFloat() * 0.7F + 0.3F);
                            }
                        } else {
                            BlockPos blockpos = this.getBlockPos();
                            Containers.dropItemStack(this.getLevel(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.itemStack);
                            this.itemStack.shrink(1);
                            this.finishParticles();
                            this.markUpdated();
                        }
                    } else if (this.itemStack.isDamaged()) {
                        if (this.getLevel().getGameTime() % (MathHelper.secondsToTicks(za.co.infernos.goety.utils.ConfigHelper.getFloat(MainConfig.SoulMenderSeconds, 1.0F)) + 1) == 0) {
                            this.itemStack.setDamageValue(this.itemStack.getDamageValue() - 1);
                            this.cursedCageTile.decreaseSouls(za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SoulMenderCost, 0) * i);
                        }
                        if (this.getLevel().random.nextInt(24) == 0) {
                            this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + this.getLevel().random.nextFloat(), this.getLevel().random.nextFloat() * 0.7F + 0.3F);
                        }
                    } else {
                        BlockPos blockpos = this.getBlockPos();
                        Containers.dropItemStack(this.getLevel(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.itemStack);
                        this.itemStack.shrink(1);
                        this.finishParticles();
                        this.markUpdated();
                    }
                }
            }
        }
    }

    public boolean isEmpty() {
        return this.itemStack.isEmpty();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.itemStack;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        if (pStack.isDamaged() && pStack.isRepairable()){
            this.placeItem(pStack);
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (this.getLevel() == null || this.getLevel().getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return pPlayer.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    private void finishParticles() {
        BlockPos blockpos = this.getBlockPos();

        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                ServerLevel serverWorld = (ServerLevel) this.getLevel();
                serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, 1, 0, 0, 0, 0);
                for (int p = 0; p < 6; ++p) {
                    double d0 = (double) blockpos.getX() + serverWorld.random.nextDouble();
                    double d1 = (double) blockpos.getY() + serverWorld.random.nextDouble();
                    double d2 = (double) blockpos.getZ() + serverWorld.random.nextDouble();
                    serverWorld.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, 0, 0, 0, 0);
                    serverWorld.sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    private void makeWorkParticles() {
        BlockPos blockpos = this.getBlockPos();
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            long t = serverLevel.getGameTime();
            if (t % 20 == 0) {
                for (int p = 0; p < 6; ++p) {
                    double d0 = (double) blockpos.getX() + serverLevel.random.nextDouble();
                    double d1 = (double) blockpos.getY() + serverLevel.random.nextDouble();
                    double d2 = (double) blockpos.getZ() + serverLevel.random.nextDouble();
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    public boolean placeItem(ItemStack pStack) {
        if (this.getLevel() != null) {
            if (this.itemStack.isEmpty()) {
                this.itemStack = pStack.split(1);
                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    private boolean checkCage() {
        if (this.getLevel() != null) {
            BlockPos pos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
            BlockState blockState = this.getLevel().getBlockState(pos);
            if (blockState.is(ModBlocks.CURSED_CAGE_BLOCK.get())) {
                BlockEntity tileentity = this.getLevel().getBlockEntity(pos);
                if (tileentity instanceof CursedCageBlockEntity) {
                    this.cursedCageTile = (CursedCageBlockEntity) tileentity;
                    return !cursedCageTile.getItem().isEmpty();
                }
            }
        }
        return false;
    }

    public void readNetwork(CompoundTag compoundNBT, net.minecraft.core.HolderLookup.Provider pRegistries) {
        if (compoundNBT.contains("Item")) {
            this.itemStack = ItemStack.parse(pRegistries, compoundNBT.getCompound("Item")).orElse(ItemStack.EMPTY);
        }
    }

    public CompoundTag writeNetwork(CompoundTag pCompound, net.minecraft.core.HolderLookup.Provider pRegistries) {
        this.saveMetadataAndItems(pCompound, pRegistries);
        return pCompound;
    }

    private CompoundTag saveMetadataAndItems(CompoundTag pCompound, net.minecraft.core.HolderLookup.Provider pRegistries) {
        if (!this.itemStack.isEmpty()) {
            pCompound.put("Item", this.itemStack.save(pRegistries, new CompoundTag()));
        }
        return pCompound;
    }

    @Override
    public void clearContent() {
        this.itemStack.shrink(1);
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        if (!((pItemStack.isDamaged() && pItemStack.isRepairable()) || pItemStack.getItem() instanceof ITotem)) {
            return false;
        }
        if (this.cursedCageTile == null) {
            return false;
        }
        return this.getLevel() != null && !this.getLevel().isClientSide && this.placeItem(pItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }
}
