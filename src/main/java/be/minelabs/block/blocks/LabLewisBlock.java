package be.minelabs.block.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class LabLewisBlock extends AbstractLewisBlock {
    public LabLewisBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            default -> LabBlock.SHAPE_N;
            case SOUTH -> LabBlock.SHAPE_S;
            case EAST -> LabBlock.SHAPE_E;
            case WEST -> LabBlock.SHAPE_W;
        };
    }

}
