package za.co.infernos.goety.common.blocks;

import za.co.infernos.goety.common.blocks.entities.ModBlockEntities;
import za.co.infernos.goety.common.blocks.entities.VoidVaultBlockEntity;
import com.mojang.serialization.MapCodec;
import za.co.infernos.goety.common.blocks.entities.void_vault.VoidVaultState;
import za.co.infernos.goety.common.blocks.properties.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import za.co.infernos.goety.common.items.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class VoidVaultBlock extends BaseEntityBlock {
    public static final MapCodec<VoidVaultBlock> CODEC = simpleCodec(p -> new VoidVaultBlock());

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    public static final EnumProperty<VoidVaultState> STATE = ModStateProperties.VOID_VAULT_STATE;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public VoidVaultBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .lightLevel(l -> l.getValue(VoidVaultBlock.STATE).getLuminance())
                .strength(50.0F)
                .sound(SoundType.METAL)
                .isViewBlocking(ModBlocks::never)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STATE, VoidVaultState.INACTIVE));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemstack = stack;
        if (itemstack.isEmpty() || state.getValue(STATE) != VoidVaultState.ACTIVE) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else if (world instanceof ServerLevel serverLevel) {
            if (serverLevel.getBlockEntity(pos) instanceof VoidVaultBlockEntity vaultBlockEntity) {
                VoidVaultBlockEntity.Server.tryUnlock(serverLevel, pos, state, vaultBlockEntity.getConfig(), vaultBlockEntity.getServerData(), vaultBlockEntity.getSharedData(), player, itemstack);
                return ItemInteractionResult.SUCCESS;
            } else {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        } else {
            return ItemInteractionResult.CONSUME;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE, FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState p_53068_, Rotation p_53069_) {
        return p_53068_.setValue(FACING, p_53069_.rotate(p_53068_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_53065_, Mirror p_53066_) {
        return p_53065_.rotate(p_53066_.getRotation(p_53065_.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VoidVaultBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel
                ? createTickerHelper(type, ModBlockEntities.VOID_VAULT.get(), (level1, blockPos, state, block)  -> VoidVaultBlockEntity.Server.tick(serverLevel, blockPos, state, block.getConfig(), block.getServerData(), block.getSharedData()))
                : createTickerHelper(type, ModBlockEntities.VOID_VAULT.get(), (level1, blockPos, state, block) -> VoidVaultBlockEntity.Client.tick(level1, blockPos, state, block.getClientData(), block.getSharedData()));
    }
}