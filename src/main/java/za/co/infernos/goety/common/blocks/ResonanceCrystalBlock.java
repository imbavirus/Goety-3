package za.co.infernos.goety.common.blocks;

import za.co.infernos.goety.common.blocks.entities.ResonanceCrystalBlockEntity;
import za.co.infernos.goety.common.entities.ally.golem.SquallGolem;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.WaystoneItem;
import za.co.infernos.goety.common.items.block.ResonanceBlockItem;
import za.co.infernos.goety.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ResonanceCrystalBlock extends BaseEntityBlock {
    public static final com.mojang.serialization.MapCodec<ResonanceCrystalBlock> CODEC = simpleCodec(
            ResonanceCrystalBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

    @Override
    public com.mojang.serialization.MapCodec<ResonanceCrystalBlock> codec() {
        return CODEC;
    }

    public ResonanceCrystalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(POWERED, Boolean.FALSE).setValue(UNSTABLE, Boolean.FALSE));
    }

    public ResonanceCrystalBlock() {
        this(ModBlocks.JadeStoneProperties());
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pHand == InteractionHand.MAIN_HAND) {
            BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
            if (tileEntity instanceof ResonanceCrystalBlockEntity crystalBlock) {
                if (stack.getItem() instanceof WaystoneItem) {
                    if (WaystoneItem.hasBlock(stack)
                            && WaystoneItem.isSameDimension(crystalBlock, stack)) {
                        GlobalPos globalPos = WaystoneItem.getPosition(stack);
                        if (globalPos != null) {
                            if (globalPos.pos() != pPos
                                    && globalPos.pos().distToCenterSqr(pPos.getCenter()) <= Mth.square(64)) {
                                crystalBlock.addBlockPos(globalPos.pos());
                                pPlayer.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                                pPlayer.level().playLocalSound(pPos.getX(), pPos.getY(), pPos.getZ(),
                                        SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 1.0F, 0.45F, false);
                            }
                        }
                        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
            BlockHitResult pHit) {
        BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
        if (tileEntity instanceof ResonanceCrystalBlockEntity crystalBlock) {
            if (!crystalBlock.getBlockPosList().isEmpty()) {
                if (pPlayer.isCrouching() || pPlayer.isShiftKeyDown()) {
                    crystalBlock.clearBlocks();
                    pLevel.playSound(null, pPos, ModSounds.SPELL_FAIL.get(), SoundSource.BLOCKS, 0.25F, 2.0F);
                } else {
                    crystalBlock.setShowBlock(!crystalBlock.isShowBlock());
                    pLevel.playSound(null, pPos, ModSounds.CAST_SPELL.get(), SoundSource.BLOCKS, 0.25F, 2.0F);
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHit);
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState,
            @javax.annotation.Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        BlockEntity tileentity = pLevel.getBlockEntity(pPos);
        if (pPlacer instanceof Player) {
            if (pStack.getItem() instanceof ResonanceBlockItem) {
                if (tileentity instanceof ResonanceCrystalBlockEntity blockEntity) {
                    if (!ResonanceBlockItem.getGolems(pStack, pLevel).isEmpty()) {
                        for (SquallGolem squallGolem : ResonanceBlockItem.getGolems(pStack, pLevel)) {
                            blockEntity.addSquallGolem(squallGolem);
                        }
                    }
                }
            }
        }
    }

    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState,
            @javax.annotation.Nullable BlockEntity pTe, ItemStack pStack) {
        ItemStack itemStack = new ItemStack(this);
        if (pState.getValue(UNSTABLE)) {
            for (int i = 0; i < 4; ++i) {
                popResource(pLevel, pPos, new ItemStack(ModItems.JADE.get()));
            }
        } else if (pTe instanceof ResonanceCrystalBlockEntity blockEntity) {
            blockEntity.clearBlocks();
            this.setItemStackTags(itemStack, blockEntity);
            popResource(pLevel, pPos, itemStack);
        }
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos,
            Player player) {
        ItemStack itemStack = new ItemStack(this);
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof ResonanceCrystalBlockEntity blockEntity) {
            this.setItemStackTags(itemStack, blockEntity);
        }
        return itemStack;
    }

    public void setItemStackTags(ItemStack itemStack, ResonanceCrystalBlockEntity tileEntity) {
        if (!tileEntity.getUuids().isEmpty()) {
            for (UUID uuid : tileEntity.getUuids()) {
                ResonanceBlockItem.setUUIDs(itemStack, uuid);
            }
        }
    }

    public boolean isSignalSource(BlockState p_55213_) {
        return true;
    }

    public int getSignal(BlockState p_55208_, BlockGetter p_55209_, BlockPos p_55210_, Direction p_55211_) {
        return p_55208_.getValue(POWERED) ? 15 : 0;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED, UNSTABLE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(POWERED, Boolean.FALSE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ResonanceCrystalBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_,
            BlockEntityType<T> p_153214_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof ResonanceCrystalBlockEntity blockEntity1)
                blockEntity1.tick();
        };
    }
}
