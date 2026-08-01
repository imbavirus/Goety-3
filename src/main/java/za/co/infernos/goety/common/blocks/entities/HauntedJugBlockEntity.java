package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.client.particles.ModParticleTypes;
import za.co.infernos.goety.common.blocks.BrewCauldronBlock;
import za.co.infernos.goety.compat.botania.BotaniaLoaded;
import za.co.infernos.goety.utils.BlockFinder;
import za.co.infernos.goety.utils.BrewUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import za.co.infernos.goety.compat.legacy.neoforge.capabilities.Capability;
import net.neoforged.neoforge.capabilities.Capabilities;
import za.co.infernos.goety.compat.legacy.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HauntedJugBlockEntity extends ModBlockEntity {
    private final FluidTank fluidTank = new FluidTank(Integer.MAX_VALUE) {

        @Override
        public @NotNull FluidStack getFluid() {
            return new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(Fluids.WATER), this.getCapacity());
        }

        @Override
        public int getFluidAmount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getCapacity() {
            return Integer.MAX_VALUE;
        }

        public CompoundTag writeToNBT(net.minecraft.core.HolderLookup.Provider provider, CompoundTag nbt) {
            this.getFluid().save(provider, nbt);
            return nbt;
        }

        @NotNull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return this.drain(resource.getAmount(), action);
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return new FluidStack(this.getFluid().getFluid(), maxDrain);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return resource.getAmount();
        }
    };
    LazyOptional<FluidTank> fluidCap = LazyOptional.of(() -> this.fluidTank);
    public int search = 0;

    public HauntedJugBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.HAUNTED_JUG.get(), p_155229_, p_155230_);
    }

    public void tick() {
        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                if (BlockFinder.isPassableBlock(this.getLevel(), this.getBlockPos().above())){
                    ++this.search;
                    int radius = 5;
                    int x = this.search / radius % radius;
                    int y = this.search / radius / radius % (radius - 2);
                    int z = this.search % radius;
                    if (this.search > 1 && x == 0 && y == 0 && z == 0){
                        this.search = 0;
                    }
                    BlockPos blockPos = this.getBlockPos().offset(x - 2, y - 1, z - 2);
                    BlockState blockState = this.getLevel().getBlockState(blockPos);
                    BlockEntity blockEntity = this.getLevel().getBlockEntity(blockPos);
                    boolean fluidHandler0 = blockEntity != null && !(blockEntity instanceof HauntedJugBlockEntity) && (this.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, blockPos, Direction.UP) != null) && !blockEntity.getBlockState().getBlock().getDescriptionId().contains("pipe");
                    boolean water = blockState.getBlock() == Blocks.WATER_CAULDRON && blockState.getValue(LayeredCauldronBlock.LEVEL) < 3;
                    boolean vanillaCauldron = blockState.getBlock() == Blocks.CAULDRON || water;
                    boolean brewCauldron = blockState.getBlock() instanceof BrewCauldronBlock && blockEntity instanceof BrewCauldronBlockEntity cauldronEntity && blockState.getValue(BrewCauldronBlock.LEVEL) < 3 && BrewUtils.isEmpty(cauldronEntity.getBrew());
                    AABB aabb = new AABB(blockPos);
                    List<LivingEntity> list = this.getLevel().getEntitiesOfClass(LivingEntity.class, aabb, livingEntity -> livingEntity.isAlive() && (livingEntity.getRemainingFireTicks() > 0 || livingEntity instanceof Axolotl));
                    if (!list.isEmpty()){
                        LivingEntity livingEntity = list.get(this.getLevel().random.nextInt(list.size()));
                        this.streamWater(blockPos);
                        livingEntity.extinguishFire();
                        if (livingEntity.isSensitiveToWater()) {
                            livingEntity.hurt(livingEntity.damageSources().drown(), 1.0F);
                        }
                        if (livingEntity instanceof Axolotl axolotl){
                            axolotl.rehydrate();
                        }
                    } else if (fluidHandler0) {
                        IFluidHandler fluidHandler = this.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, blockPos, Direction.UP);
                        if (fluidHandler != null) {
                            int filled = fluidHandler.fill(new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(Fluids.WATER), 250), IFluidHandler.FluidAction.EXECUTE);
                            if (filled > 0) {
                                this.streamWater(blockPos);
                                this.fluidTank.drain(new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(Fluids.WATER), filled), IFluidHandler.FluidAction.EXECUTE);
                                this.markUpdated();
                            }
                        }
                    } else if (brewCauldron) {
                        this.getLevel().setBlock(blockPos, blockState.cycle(BrewCauldronBlock.LEVEL), 2);
                        this.getLevel().updateNeighborsAt(blockPos, blockState.getBlock());
                        this.streamWater(blockPos);
                        this.fluidTank.drain(new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(Fluids.WATER), 333), IFluidHandler.FluidAction.EXECUTE);
                        this.markUpdated();
                    } else if (vanillaCauldron) {
                        if (water) {
                            this.getLevel().setBlock(blockPos, blockState.cycle(LayeredCauldronBlock.LEVEL), 2);
                        } else if (blockState.getBlock() == Blocks.CAULDRON) {
                            this.getLevel().setBlock(blockPos, Blocks.WATER_CAULDRON.defaultBlockState(), 2);
                        }
                        this.getLevel().updateNeighborsAt(blockPos, blockState.getBlock());
                        this.streamWater(blockPos);
                        this.fluidTank.drain(new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(Fluids.WATER), 333), IFluidHandler.FluidAction.EXECUTE);
                        this.markUpdated();
                    } else if (BotaniaLoaded.BOTANIA.isLoaded()){
                        // TODO(1.21): Botania integration is disabled until Botania updates for 1.21.1.
                    }
                } else {
                    IFluidHandler fluidHandler = this.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, this.getBlockPos().above(), Direction.DOWN);
                    if (fluidHandler != null) {
                        int filled = fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                        if (filled > 0) {
                            this.fluidTank.drain(new FluidStack(BuiltInRegistries.FLUID.wrapAsHolder(Fluids.WATER), filled), IFluidHandler.FluidAction.EXECUTE);
                            this.markUpdated();
                        }
                    }
                }
            }
        }
    }

    public void streamWater(BlockPos target){
        if (this.getLevel() != null) {
            this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.33F, 1.0F);
            if (this.getLevel() instanceof ServerLevel serverLevel) {
                Vec3 vec3 = Vec3.atBottomCenterOf(this.getBlockPos().above());
                Vec3 vec31 = Vec3.atBottomCenterOf(target);
                serverLevel.sendParticles(ModParticleTypes.WATER_STREAM.get(), vec3.x, vec3.y, vec3.z, 0, vec31.x, vec31.y, vec31.z, 1.0F);
            }
            this.getLevel().playSound(null, target, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.33F, 1.0F);
        }
    }

    @Override
    public void readNetwork(CompoundTag compoundNBT, net.minecraft.core.HolderLookup.Provider pRegistries) {
        this.fluidTank.readFromNBT(pRegistries, compoundNBT);
    }

    @Override
    public CompoundTag writeNetwork(CompoundTag pCompound, net.minecraft.core.HolderLookup.Provider pRegistries) {
        return this.fluidTank.writeToNBT(pRegistries, pCompound);
    }
}
