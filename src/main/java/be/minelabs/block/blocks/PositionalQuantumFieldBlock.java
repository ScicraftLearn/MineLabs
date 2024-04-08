package be.minelabs.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class PositionalQuantumFieldBlock extends QuantumfieldBlock{

    public static final BooleanProperty X_ODD = BooleanProperty.of("x_odd");
    public static final BooleanProperty Y_ODD = BooleanProperty.of("y_odd");
    public static final BooleanProperty Z_ODD = BooleanProperty.of("z_odd");

    public PositionalQuantumFieldBlock(){
        super();
        setDefaultState(getDefaultState().with(X_ODD, false).with(Y_ODD, false).with(Z_ODD, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(X_ODD, Y_ODD, Z_ODD);
    }

    public static BlockState getStateWithPositionInfo(BlockState state, BlockPos pos){
        return state
                .with(X_ODD, Math.floorMod(pos.getX(), 2) == 1)
                .with(Y_ODD, Math.floorMod(pos.getY(), 2) == 1)
                .with(Z_ODD, Math.floorMod(pos.getZ(), 2) == 1);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return getStateWithPositionInfo(super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos), pos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state == null) return state;
        return getStateWithPositionInfo(state, ctx.getBlockPos());
    }

}
