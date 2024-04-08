package be.minelabs.block.blocks;

import be.minelabs.block.Blocks;
import be.minelabs.state.property.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public abstract class CosmeticBlock extends Block {

    protected static final IntProperty COUNTER = Properties.COUNTER;
    protected static final DirectionProperty FACING = net.minecraft.state.property.Properties.HORIZONTAL_FACING;

    public CosmeticBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(COUNTER, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COUNTER);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(COUNTER, getBase(ctx.getWorld(), ctx.getBlockPos().down()));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).getBlock() != Blocks.LAB_SINK
                && world.getBlockState(pos.down()).getBlock() != net.minecraft.block.Blocks.CAULDRON;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN) {
            return state.with(COUNTER, getBase(neighborState.getBlock()));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }


    protected float getYOffset(@NotNull BlockState state) {
        return switch (state.get(COUNTER)) {
            case 1 -> 0.0625f;
            case 2 -> 0.125f;
            default -> 0f;
        };
    }

    protected int getBase(@NotNull World world, BlockPos pos) {
        return getBase(world.getBlockState(pos).getBlock());
    }

    private int getBase(Block block) {
        int base = 0;
        if (block instanceof LabBlock || block instanceof LabLewisBlock) {
            base = 2;
        } else if (block instanceof LabCenterBlock) {
            base = 1;
        }
        return base;
    }
}
