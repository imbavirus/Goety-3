package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.blocks.IEnchantedBlock;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.SculkGrowerBlock;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SPlayWorldSoundPacket;
import za.co.infernos.goety.config.MainConfig;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class SculkGrowerBlockEntity extends ModBlockEntity implements IEnchantedBlock {
    private CursedCageBlockEntity cursedCageTile;
    private int growCharges = -1;
    private int decayTimer = 100;
    private final List<BlockPos> growablePlantPos = Lists.newArrayList();
    private final List<BlockPos> totalPlantPos = Lists.newArrayList();
    protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();

    public SculkGrowerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.SCULK_GROWER.get(), p_155229_, p_155230_);
    }

    @Override
    public Object2IntMap<Enchantment> getEnchantments() {
        return this.enchantments;
    }

    public void tick() {
        if (this.getLevel() != null && !this.getLevel().isClientSide) {
            if (this.checkCage()) {
                this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SculkGrowerBlock.LIT, true), 3);
            } else {
                this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SculkGrowerBlock.LIT, false), 3);
            }
            this.scanTotalPlants();
            if (this.getLevel().getGameTime() % 5 == 0) {
                this.findPlants();
            }
            if (this.growCharges <= 0) {
                if (!this.growablePlantPos.isEmpty() && this.checkCage()) {
                    if (this.takeSouls()) {
                        this.growCharges = za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SculkGrowerCharge, 0);
                    }
                }
            } else {
                this.growPlants();
            }
            if (this.totalPlantPos.isEmpty() || this.decayTimer <= 0) {
                this.decayCharges();
            }
        }
    }

    public void commonTick() {
        if (this.getLevel() != null && !this.getLevel().isClientSide) {
            if (this.growCharges <= 0) {
                this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SculkGrowerBlock.CHARGED, false),
                        3);
            } else {
                this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SculkGrowerBlock.CHARGED, true),
                        3);
            }
        }
    }

    public void decayCharges() {
        if (this.getLevel() != null && this.getLevel() instanceof ServerLevel serverLevel) {
            if (this.growCharges > 0 && this.getLevel().getGameTime() % 10 == 0) {
                --this.growCharges;
                double d0 = 0.5625D;
                float f5 = 0.15F + 0.02F * serverLevel.random.nextFloat();
                float f = 0.4F + 0.3F * serverLevel.random.nextFloat();
                ModNetwork.sendToALL(
                        new SPlayWorldSoundPacket(this.getBlockPos(), SoundEvents.SCULK_BLOCK_CHARGE, f5, f));
                for (Direction direction : Direction.values()) {
                    BlockPos blockpos = this.getBlockPos().relative(direction);
                    if (!serverLevel.getBlockState(blockpos).isSolidRender(serverLevel, blockpos)) {
                        Direction.Axis direction$axis = direction.getAxis();
                        double d1 = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getStepX()
                                : (double) serverLevel.random.nextFloat();
                        double d2 = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getStepY()
                                : (double) serverLevel.random.nextFloat();
                        double d3 = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getStepZ()
                                : (double) serverLevel.random.nextFloat();
                        serverLevel.sendParticles(ParticleTypes.SCULK_CHARGE_POP,
                                (double) this.getBlockPos().getX() + d1, (double) this.getBlockPos().getY() + d2,
                                (double) this.getBlockPos().getZ() + d3, 2, 0.2D, 0.0D, 0.2D, 0.0D);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void scanTotalPlants() {
        if (this.getLevel() != null) {
            this.totalPlantPos.clear();
            int distance = 4 + this.enchantments.getOrDefault(ModEnchantments.RADIUS.get(), 0);
            int y = distance / 2;
            for (int i = -distance; i <= distance; ++i) {
                for (int j = -y; j <= y; ++j) {
                    for (int k = -distance; k <= distance; ++k) {
                        BlockPos blockPos = this.getBlockPos().offset(i, j, k);
                        if (this.getLevel().hasChunkAt(blockPos)) {
                            BlockState blockState = this.getLevel().getBlockState(blockPos);
                            if (!blockState.isAir()) {
                                if (isCrops(blockPos)) {
                                    if (!this.totalPlantPos.contains(blockPos)) {
                                        this.totalPlantPos.add(blockPos);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void findPlants() {
        if (this.getLevel() != null && this.getLevel() instanceof ServerLevel serverLevel) {
            this.growablePlantPos.clear();
            int distance = 5 + this.enchantments.getOrDefault(ModEnchantments.RADIUS.get(), 0);
            int x = serverLevel.random.nextInt(distance) * (serverLevel.random.nextBoolean() ? -1 : 1);
            int y = distance / 2;
            int z = serverLevel.random.nextInt(distance) * (serverLevel.random.nextBoolean() ? -1 : 1);
            for (int i = -y; i < y; ++i) {
                BlockPos blockPos = this.getBlockPos().offset(x, i, z);
                if (this.getLevel().hasChunkAt(blockPos)) {
                    if (isCrops(blockPos)) {
                        this.growablePlantPos.add(blockPos);
                    }
                }
            }
        }
    }

    public void growPlants() {
        if (this.getLevel() != null && this.getLevel() instanceof ServerLevel serverLevel) {
            if (!this.growablePlantPos.isEmpty()) {
                int potency = 1 + this.enchantments.getOrDefault(ModEnchantments.POTENCY.get(), 0);
                if (!za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.SculkGrowerPotency, false)) {
                    potency = 1;
                }
                int random = serverLevel.random.nextInt(this.growablePlantPos.size());
                BlockPos blockPos = this.growablePlantPos.get(random);
                BlockState blockState = serverLevel.getBlockState(blockPos);
                if (!blockState.isAir()) {
                    --this.growCharges;
                    serverLevel.sendParticles(new SculkChargeParticleOptions(0.0F), (double) blockPos.getX() + 0.5D,
                            (double) blockPos.getY(), (double) blockPos.getZ() + 0.5D, 2, 0.2D, 0.0D, 0.2D, 0.0D);
                    float f5 = 0.15F + 0.02F * serverLevel.random.nextFloat();
                    float f = 0.4F + 0.3F * serverLevel.random.nextFloat();
                    ModNetwork.sendToALL(
                            new SPlayWorldSoundPacket(blockPos, SoundEvents.SCULK_BLOCK_CHARGE, f5, f));
                    for (int i = 0; i < potency; ++i) {
                        blockState.randomTick(serverLevel, blockPos, serverLevel.random);
                    }
                    this.decayTimer = 100;
                }
            } else {
                if (this.decayTimer > 0) {
                    --this.decayTimer;
                }
            }
        }
    }

    private boolean isCrops(BlockPos blockPos) {
        if (this.getLevel() == null) {
            return false;
        }
        BlockState blockState = this.getLevel().getBlockState(blockPos);
        Block cropBlock = blockState.getBlock();
        return cropBlock != Blocks.GRASS_BLOCK && !(cropBlock instanceof DoublePlantBlock)
                && cropBlock instanceof BonemealableBlock && ((BonemealableBlock) cropBlock)
                        .isValidBonemealTarget(this.getLevel(), blockPos, blockState);
    }

    private boolean takeSouls() {
        if (this.getLevel() == null) {
            return false;
        }
        int potency = 1 + this.enchantments.getOrDefault(ModEnchantments.POTENCY.get(), 0);
        if (!za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.SculkGrowerPotency, false)) {
            potency = 1;
        }
        int cost = za.co.infernos.goety.utils.ConfigHelper.getInt(MainConfig.SculkGrowerCost, 0) * potency;
        if (this.getCursedCageTile().getSouls() > cost) {
            this.getCursedCageTile().decreaseSouls(cost);
            this.getCursedCageTile().generateManyParticles();
            return true;
        } else {
            return false;
        }
    }

    private boolean checkCage() {
        if (this.getLevel() == null) {
            return false;
        }
        BlockPos pos = this.getBlockPos().above();
        BlockState blockState = this.getLevel().getBlockState(pos);
        if (blockState.is(ModBlocks.CURSED_CAGE_BLOCK.get())) {
            BlockEntity tileentity = this.getLevel().getBlockEntity(pos);
            if (tileentity instanceof CursedCageBlockEntity) {
                this.cursedCageTile = (CursedCageBlockEntity) tileentity;
                return !cursedCageTile.getItem().isEmpty();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int getGrowCharges() {
        return this.growCharges;
    }

    @Override
    public void readNetwork(CompoundTag compoundNBT, net.minecraft.core.HolderLookup.Provider pRegistries) {
        this.growCharges = compoundNBT.getInt("GrowCharges");
        this.decayTimer = compoundNBT.getInt("DecayTimer");
        this.loadEnchants(compoundNBT);
    }

    @Override
    public CompoundTag writeNetwork(CompoundTag pCompound, net.minecraft.core.HolderLookup.Provider pRegistries) {
        pCompound.putInt("GrowCharges", this.growCharges);
        pCompound.putInt("DecayTimer", this.decayTimer);
        this.saveEnchants(pCompound, ModBlocks.SCULK_GROWER.get().asItem());
        return pCompound;
    }

    public CursedCageBlockEntity getCursedCageTile() {
        return this.cursedCageTile;
    }

}
