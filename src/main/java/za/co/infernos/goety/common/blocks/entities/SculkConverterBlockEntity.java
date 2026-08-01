package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.blocks.IEnchantedBlock;
import za.co.infernos.goety.client.particles.SculkBubbleParticleOption;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.SculkConverterBlock;
import za.co.infernos.goety.common.blocks.SculkRelayBlock;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.utils.BlockFinder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.phys.Vec3;

public class SculkConverterBlockEntity extends ModBlockEntity implements IEnchantedBlock {
    private final SculkSpreader sculkSpreader = SculkSpreader.createLevelSpreader();
    private BlockPos nearbyRelay;
    private CursedCageBlockEntity cursedCageTile;
    protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();

    public SculkConverterBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.SCULK_CONVERTER.get(), p_155229_, p_155230_);
    }

    public Object2IntMap<Enchantment> getEnchantments(){
        return this.enchantments;
    }

    public void activate(){
        if (this.getLevel() != null && this.getLevel() instanceof ServerLevel serverLevel) {
            if (this.checkCage()) {
                int spread = this.enchantments.getOrDefault(ModEnchantments.POTENCY.get(), 0) + 1;
                int cost = 5 * spread;
                if (this.getCursedCageTile().getSouls() > cost) {
                    this.getRelay();
                    this.getCursedCageTile().decreaseSouls(cost);
                    int x = serverLevel.getRandom().nextInt(-1, 1);
                    int y = (int) BlockFinder.moveBlockDownToGround(this.getLevel(), this.getBlockPos());
                    int z = serverLevel.getRandom().nextInt(-1, 1);
                    BlockPos blockPos = new BlockPos(this.getBlockPos().getX() + x, y, this.getBlockPos().getZ() + z);
                    if (this.nearbyRelay != null){
                        y = (int) BlockFinder.moveBlockDownToGround(this.getLevel(), this.nearbyRelay);
                        blockPos = new BlockPos(this.nearbyRelay.getX() + x, y, this.nearbyRelay.getZ() + z);
                    }
                    this.sculkSpreader.addCursors(blockPos, spread);
                    SculkConverterBlock.bloom(serverLevel, this.getBlockPos(), this.getBlockState(), serverLevel.random);
                }
            }
        }
    }

    public void findRelay(){
        if (this.getLevel() != null) {
            if (this.nearbyRelay != null) {
                BlockState blockState = this.getLevel().getBlockState(this.nearbyRelay);
                if (!(blockState.getBlock() instanceof SculkRelayBlock)) {
                    this.searchRelay();
                } else {
                    if (this.getLevel() instanceof ServerLevel serverLevel) {
                        Vec3 vec3 = Vec3.atCenterOf(this.getBlockPos());
                        serverLevel.sendParticles(new SculkBubbleParticleOption(new BlockPositionSource(this.nearbyRelay), (int) vec3.distanceTo(Vec3.atCenterOf(this.nearbyRelay)) * 2), vec3.x, vec3.y, vec3.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                this.searchRelay();
            }
        }
    }

    public void getRelay(){
        if (this.getLevel() != null) {
            if (this.nearbyRelay != null) {
                BlockState blockState = this.getLevel().getBlockState(this.nearbyRelay);
                if (!(blockState.getBlock() instanceof SculkRelayBlock)) {
                    this.searchRelay();
                } else {
                    if (this.getLevel() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(new SculkChargeParticleOptions(0.0F), (double)this.nearbyRelay.getX() + 0.5D, (double)this.nearbyRelay.getY() + 1.15D, (double)this.nearbyRelay.getZ() + 0.5D, 2, 0.2D, 0.0D, 0.2D, 0.0D);
                        serverLevel.sendParticles(DustColorTransitionOptions.SCULK_TO_REDSTONE, (double)this.nearbyRelay.getX() + 0.5D, (double)this.nearbyRelay.getY() + 1.15D, (double)this.nearbyRelay.getZ() + 0.5D, 0, 0.0D, (double)serverLevel.random.nextFloat() * 0.04D, 0.0D, 0.9F);
                        Vec3 vec3 = Vec3.atCenterOf(this.getBlockPos());
                        int spread = this.enchantments.getOrDefault(ModEnchantments.POTENCY.get(), 0) + 1;
                        for (int i = 0; i < spread; ++i) {
                            serverLevel.sendParticles(new SculkBubbleParticleOption(new BlockPositionSource(this.nearbyRelay), (int) vec3.distanceTo(Vec3.atCenterOf(this.nearbyRelay)) * (serverLevel.random.nextInt(4) + 1)), vec3.x, vec3.y, vec3.z, 2, 0.2D, 0.0D, 0.2D, 0.0D);
                        }
                    }
                }
            } else {
                this.searchRelay();
            }
        }
    }

    private void searchRelay(){
        if (this.getLevel() != null) {
            for (int i = -16; i <= 16; ++i) {
                for (int j = -8; j <= 8; ++j) {
                    for (int k = -16; k <= 16; ++k) {
                        BlockPos blockpos1 = this.getBlockPos().offset(i, j, k);
                        BlockState blockstate = this.getLevel().getBlockState(blockpos1);
                        if (blockstate.getBlock() instanceof SculkRelayBlock) {
                            this.nearbyRelay = blockpos1;
                        }
                    }
                }
            }
        }
    }

    public void tick(){
        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                if (!this.checkCage()) {
                    this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SculkConverterBlock.LIT, false), 3);
                } else {
                    this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(SculkConverterBlock.LIT, true), 3);
                }
                if (!this.sculkSpreader.getCursors().isEmpty()) {
                    this.sculkSpreader.updateCursors(this.getLevel(), this.getBlockPos(), this.getLevel().getRandom(), true);
                }
            }
        }
    }

    private boolean checkCage() {
        if (this.getLevel() == null){
            return false;
        }
        BlockPos pos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ());
        BlockState blockState = this.getLevel().getBlockState(pos);
        if (blockState.is(ModBlocks.CURSED_CAGE_BLOCK.get())){
            BlockEntity tileentity = this.getLevel().getBlockEntity(pos);
            if (tileentity instanceof CursedCageBlockEntity){
                this.cursedCageTile = (CursedCageBlockEntity) tileentity;
                return !cursedCageTile.getItem().isEmpty();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void readNetwork(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries) {
        this.sculkSpreader.load(tag);
        NbtUtils.readBlockPos(tag, "NearbyRelay").ifPresent(pos -> this.nearbyRelay = pos);
        this.loadEnchants(tag);
    }

    @Override
    public CompoundTag writeNetwork(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries) {
        this.sculkSpreader.save(tag);
        if (this.nearbyRelay != null) {
            tag.put("NearbyRelay", NbtUtils.writeBlockPos(this.nearbyRelay));
        }
        this.saveEnchants(tag, ModBlocks.SCULK_CONVERTER.get().asItem());
        return tag;
    }

    public CursedCageBlockEntity getCursedCageTile(){
        return this.cursedCageTile;
    }
}
