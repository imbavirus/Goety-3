package za.co.infernos.goety.common.blocks;

import za.co.infernos.goety.api.items.magic.IWand;
import za.co.infernos.goety.common.blocks.entities.AnimatorBlockEntity;
import za.co.infernos.goety.common.items.WaystoneItem;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import za.co.infernos.goety.compat.legacy.neoforge.common.extensions.IForgeBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;

public class AnimatorBlock extends BaseEntityBlock implements IForgeBlock {
    public static final MapCodec<AnimatorBlock> CODEC = simpleCodec(AnimatorBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public AnimatorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE).setValue(TRIGGERED, Boolean.FALSE));
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        // The original commented-out NBT code is replaced by Data Component logic if needed.
        // For now, it's just the super call as the instruction's provided snippet was problematic for this method.
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
        if (tileEntity instanceof AnimatorBlockEntity animatorBlock) {
            if (stack.getItem() instanceof WaystoneItem && pPlayer.isCrouching()) {
                CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag compoundnbt = customData.copyTag();
                if (compoundnbt.contains("targetPos")) {
                    BlockPos blockPos = BlockPos.of(compoundnbt.getLong("targetPos"));
                    animatorBlock.setPosition(blockPos);
                    pPlayer.displayClientMessage(Component.translatable("info.goety.animator.success"), true);
                    pLevel.playSound(null, pPos, SoundEvents.ARROW_HIT_PLAYER, SoundSource.BLOCKS, 1.0F, 0.45F);
                }
                return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
            } else if (animatorBlock.getItem().isEmpty() && stack.getItem() instanceof WaystoneItem) {
                if (WaystoneItem.hasBlock(stack)) {
                    this.setItem(pLevel, pPos, pState, stack);
                    return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        BlockEntity tileEntity = pLevel.getBlockEntity(pPos);
        if (tileEntity instanceof AnimatorBlockEntity animatorBlock) {
            if (!animatorBlock.getItem().isEmpty()) {
                if (pPlayer.isCrouching() && animatorBlock.getPosition() != null) {
                    animatorBlock.setShowBlock(!animatorBlock.isShowBlock());
                    pLevel.playSound(null, pPos, ModSounds.CAST_SPELL.get(), SoundSource.BLOCKS, 0.25F, 2.0F);
                } else {
                    this.dropItem(pLevel, pPos);
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public void setItem(Level pLevel, BlockPos pPos, BlockState pState, ItemStack pStack) {
        BlockEntity tileentity = pLevel.getBlockEntity(pPos);
        if (tileentity instanceof AnimatorBlockEntity animatorBlock) {
            animatorBlock.setItem(pStack.split(1));
            pLevel.playSound(null, pPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public void dropItem(Level pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide) {
            BlockEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof AnimatorBlockEntity animatorBlock) {
                ItemStack itemstack = animatorBlock.getItem();
                if (!itemstack.isEmpty()) {
                    pLevel.levelEvent(1010, pPos, 0);
                    animatorBlock.clearContent();
                    float f = 0.7F;
                    double d0 = (double)(pLevel.random.nextFloat() * f) + (double)0.15F;
                    double d1 = (double)(pLevel.random.nextFloat() * f) + (double)0.060000002F + 0.6D;
                    double d2 = (double)(pLevel.random.nextFloat() * f) + (double)0.15F;
                    ItemStack itemstack1 = itemstack.copy();
                    ItemEntity itementity = new ItemEntity(pLevel, (double)pPos.getX() + d0, (double)pPos.getY() + d1, (double)pPos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickUpDelay();
                    if (pLevel.addFreshEntity(itementity)){
                        pLevel.playSound(null, pPos, SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            this.dropItem(pLevel, pPos);
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean flag = pLevel.hasNeighborSignal(pPos) || pLevel.hasNeighborSignal(pPos.above());
        boolean flag1 = pState.getValue(TRIGGERED);
        if (pState.getValue(POWERED)) {
            if (flag && !flag1) {
                BlockEntity tileentity = pLevel.getBlockEntity(pPos);
                if (tileentity instanceof AnimatorBlockEntity animatorBlock) {
                    animatorBlock.summonGolem();
                }
                pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.TRUE), 4);
            } else if (!flag && flag1) {
                pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.FALSE), 4);
            }
        }

    }

    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, POWERED, TRIGGERED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new AnimatorBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof AnimatorBlockEntity blockEntity1)
                blockEntity1.tick();
        };
    }
}

