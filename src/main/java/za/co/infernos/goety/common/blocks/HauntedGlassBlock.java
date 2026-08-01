package za.co.infernos.goety.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HauntedGlassBlock extends HalfTransparentBlock {
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
    public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.create("connected_north");
    public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.create("connected_south");
    public static final BooleanProperty CONNECTED_WEST = BooleanProperty.create("connected_west");
    public static final BooleanProperty CONNECTED_EAST = BooleanProperty.create("connected_east");
    public boolean isPlayerOnly;
    public boolean isTinted;

    public HauntedGlassBlock(BlockBehaviour.Properties properties, boolean isPlayerOnly, boolean isTinted) {
        super(properties);
        this.isPlayerOnly = isPlayerOnly;
        this.isTinted = isTinted;
        this.registerDefaultState(stateDefinition.any().setValue(CONNECTED_DOWN, Boolean.FALSE).setValue(CONNECTED_EAST, Boolean.FALSE).setValue(CONNECTED_NORTH, Boolean.FALSE).setValue(CONNECTED_SOUTH, Boolean.FALSE).setValue(CONNECTED_UP, Boolean.FALSE).setValue(CONNECTED_WEST, Boolean.FALSE));
    }

    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (this.isTinted){
            return false;
        } else {
            return super.propagatesSkylightDown(blockState, blockGetter, blockPos);
        }
    }

    public int getLightBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (this.isTinted) {
            return blockGetter.getMaxLightLevel();
        } else {
            return super.getLightBlock(blockState, blockGetter, blockPos);
        }
    }

    public boolean skipRendering(@Nonnull BlockState state, BlockState adjacentBlockState, @Nonnull Direction side) {
        return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext context1){
            if (context1.getEntity() instanceof Player){
                if (this.isPlayerOnly){
                    return Shapes.empty();
                }
            } else {
                if (!this.isPlayerOnly){
                    return Shapes.empty();
                }
            }
        }
        return state.getShape(world, pos);
    }

    public PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        if (state.getBlock() instanceof HauntedGlassBlock glassBlock){
            if (!glassBlock.isPlayerOnly){
                return PathType.OPEN;
            }
        }
        return super.getBlockPathType(state, level, pos, mob);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(CONNECTED_DOWN, this.isSideConnectable(world, pos, Direction.DOWN))
                .setValue(CONNECTED_EAST, this.isSideConnectable(world, pos, Direction.EAST))
                .setValue(CONNECTED_NORTH, this.isSideConnectable(world, pos, Direction.NORTH))
                .setValue(CONNECTED_SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH))
                .setValue(CONNECTED_UP, this.isSideConnectable(world, pos, Direction.UP))
                .setValue(CONNECTED_WEST, this.isSideConnectable(world, pos, Direction.WEST));
    }

    @Nonnull
    @Override
    public BlockState updateShape(BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
        return stateIn.setValue(CONNECTED_DOWN, this.isSideConnectable(world, pos, Direction.DOWN))
                .setValue(CONNECTED_EAST, this.isSideConnectable(world, pos, Direction.EAST))
                .setValue(CONNECTED_NORTH, this.isSideConnectable(world, pos, Direction.NORTH))
                .setValue(CONNECTED_SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH))
                .setValue(CONNECTED_UP, this.isSideConnectable(world, pos, Direction.UP))
                .setValue(CONNECTED_WEST, this.isSideConnectable(world, pos, Direction.WEST));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
    }

    private boolean isSideConnectable(BlockGetter world, BlockPos pos, Direction side) {
        final BlockState stateConnection = world.getBlockState(pos.relative(side));
        return stateConnection != null && stateConnection.getBlock() == this;
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
        // Explicitly drop the block item using popResource
        if (!pLevel.isClientSide) {
            ItemStack itemStack = new ItemStack(this);
            popResource(pLevel, pPos, itemStack);
        }
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
    }
}
