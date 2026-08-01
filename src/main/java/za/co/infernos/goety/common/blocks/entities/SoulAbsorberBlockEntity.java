package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.items.magic.ITotem;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.SoulAbsorberBlock;
import za.co.infernos.goety.common.crafting.ModRecipeSerializer;
import za.co.infernos.goety.common.crafting.SoulAbsorberRecipes;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class SoulAbsorberBlockEntity extends ModBlockEntity implements Clearable, WorldlyContainer {
    private static final int[] SLOTS = new int[]{0};
    private ItemStack itemStack = ItemStack.EMPTY;
    private int cookingProgress;
    private int cookingTime;

    public SoulAbsorberBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SOUL_ABSORBER.get(), blockPos, blockState);
    }

    public void tick() {
        boolean flag = this.getArcaOwner() != null;
        if (this.getLevel() == null){
            return;
        }
        if (!this.getLevel().isClientSide) {
            if (flag) {
                if (!this.itemStack.isEmpty()) {
                    this.makeWorkParticles();
                }
                this.work();
            } else {
                if (this.itemStack != ItemStack.EMPTY){
                    if (this.cookingProgress > 0) {
                        this.cookingProgress = Mth.clamp(this.cookingProgress - 2, 0, this.cookingTime);
                    }
                }
            }
        }
        this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SoulAbsorberBlock.LIT, this.getArcaOwner() != null), 3);
    }

    private void work() {
        if (this.getLevel() == null){
            return;
        }
        if (!this.itemStack.isEmpty()) {
            if (this.itemStack.getItem() instanceof ITotem){
                if (ITotem.currentSouls(this.itemStack) != 0){
                    this.cookingProgress++;
                    if (this.getArcaOwner() != null){
                        SEHelper.increaseSouls(this.getArcaOwner(), 1);
                        ITotem.decreaseSouls(this.itemStack, 1);
                    }
                } else {
                    this.itemStack.shrink(1);
                    this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.finishParticles();
                    this.markUpdated();
                    this.cookingProgress = 0;
                }
            } else {
                Container iinventory = new SimpleContainer(this.itemStack);
                int soulIncrease = this.getLevel().getRecipeManager()
                        .getRecipeFor(ModRecipeSerializer.SOUL_ABSORBER.get(), new net.minecraft.world.item.crafting.SingleRecipeInput(this.itemStack), this.getLevel())
                        .map(net.minecraft.world.item.crafting.RecipeHolder::value)
                        .map(SoulAbsorberRecipes::getSoulIncrease).orElse(25);
                if (soulIncrease > 0){
                    this.cookingProgress++;
                }
                if (this.cookingProgress >= this.cookingTime) {
                    if (this.getArcaOwner() != null){
                        SEHelper.increaseSouls(this.getArcaOwner(), soulIncrease);
                    }
                    this.itemStack.shrink(1);
                    this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.finishParticles();
                    this.markUpdated();
                    this.cookingProgress = 0;
                }
            }
        }

    }

    public boolean placeItem(ItemStack pStack, int pCookTime) {
        if (this.getLevel() == null){
            return false;
        }
        if (this.itemStack.isEmpty()) {
            this.cookingTime = pCookTime;
            this.cookingProgress = 0;
            this.itemStack = pStack.split(1);
            this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.BLOCKS, 1.0F, 0.5F);
            this.markUpdated();
            return true;
        }

        return false;
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
        Optional<SoulAbsorberRecipes> optional = this.getRecipes(pStack);
        optional.ifPresent(soulAbsorberRecipes -> this.placeItem(pStack, soulAbsorberRecipes.getCookingTime()));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (this.getLevel().getBlockEntity(this.worldPosition) != this) {
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
        ServerLevel serverLevel = (ServerLevel) this.getLevel();

        if (serverLevel != null) {
            long t = serverLevel.getGameTime();
            if (t % 20 == 0) {
                for (int p = 0; p < 6; ++p) {
                    double d0 = (double)blockpos.getX() + serverLevel.random.nextDouble();
                    double d1 = (double)blockpos.getY() + serverLevel.random.nextDouble();
                    double d2 = (double)blockpos.getZ() + serverLevel.random.nextDouble();
                    serverLevel.sendParticles(ParticleTypes.ENCHANT, d0, d1, d2, 1, 0, 0, 0, 0);
                    serverLevel.sendParticles(ParticleTypes.PORTAL, d0, d1, d2, 1, 0.0D, 0, 0.0D, 0);
                }
            }
        }
    }

    public void readNetwork(CompoundTag compoundNBT, net.minecraft.core.HolderLookup.Provider pRegistries) {
        if (compoundNBT.contains("Item")) {
            this.itemStack = ItemStack.parse(pRegistries, compoundNBT.getCompound("Item")).orElse(ItemStack.EMPTY);
        }
        this.cookingProgress = compoundNBT.getInt("CookingTime");
        this.cookingTime = compoundNBT.getInt("CookingTotalTime");
    }

    public CompoundTag writeNetwork(CompoundTag pCompound, net.minecraft.core.HolderLookup.Provider pRegistries) {
        this.saveMetadataAndItems(pCompound, pRegistries);
        pCompound.putInt("CookingTime", this.cookingProgress);
        pCompound.putInt("CookingTotalTime", this.cookingTime);
        return pCompound;
    }

    private CompoundTag saveMetadataAndItems(CompoundTag pCompound, net.minecraft.core.HolderLookup.Provider pRegistries) {
        if (!this.itemStack.isEmpty()) {
            pCompound.put("Item", this.itemStack.save(pRegistries, new CompoundTag()));
        }
        return pCompound;
    }

    private boolean checkArca() {
        if (this.getLevel() == null){
            return false;
        }
        return this.getLevel().getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ())).is(ModBlocks.ARCA_BLOCK.get());
    }

    private Player getArcaOwner(){
        if (this.checkArca()){
            BlockPos blockPos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
            ArcaBlockEntity arcaTileEntity = (ArcaBlockEntity) this.getLevel().getBlockEntity(blockPos);
            if (arcaTileEntity != null && arcaTileEntity.getPlayer() != null){
                return arcaTileEntity.getPlayer();
            }
        }
        return null;
    }

    public Optional<SoulAbsorberRecipes> getRecipes(ItemStack pStack) {
        return this.getLevel().getRecipeManager().getRecipeFor(ModRecipeSerializer.SOUL_ABSORBER.get(), new net.minecraft.world.item.crafting.SingleRecipeInput(pStack), this.getLevel())
                .map(net.minecraft.world.item.crafting.RecipeHolder::value);
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
        if (this.getLevel() == null){
            return false;
        }
        Optional<SoulAbsorberRecipes> optional = this.getRecipes(pItemStack);
        if (!optional.isPresent()) return false;
        if (this.getArcaOwner() == null) return false;
        return !this.getLevel().isClientSide && this.placeItem(pItemStack, optional.get().getCookingTime());
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }
}
