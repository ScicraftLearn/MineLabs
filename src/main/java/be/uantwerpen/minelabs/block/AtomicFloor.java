package be.uantwerpen.minelabs.block;

import be.uantwerpen.minelabs.item.Items;
import be.uantwerpen.minelabs.util.QuantumFieldSpawner;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class AtomicFloor extends AbstractGlassBlock {
    public final static int AtomicFloorLayer = 64;

    public AtomicFloor() {
        super(Settings.of(Material.AMETHYST).hardness(-1f).strength(3600000.0F).nonOpaque().ticksRandomly());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (world.isClient()) {
            return;
        }
        QuantumFieldSpawner.tryToSpawnCloud(world, pos);
    }


    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return context.isHolding(Items.ATOM_FLOOR) || context.isHolding(Items.BOHR_BLOCK) ?
                VoxelShapes.fullCube() : VoxelShapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }
}