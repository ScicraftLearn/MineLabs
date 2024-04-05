package be.minelabs.block.blocks;

import be.minelabs.block.Blocks;
import be.minelabs.block.entity.QuantumFieldBlockEntity;
import be.minelabs.state.property.Properties;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class QuantumfieldBlock extends TransparentBlock implements BlockEntityProvider {
    public static final int MAX_AGE = 10;
    public static final int DECAYRATE = 1;

    public static final BooleanProperty MASTER = Properties.MASTER;
    public static final IntProperty AGE = IntProperty.of("age", 0, MAX_AGE);
    // If block is part of a cloud (determines rendering)
    public static final BooleanProperty CLOUD = Properties.CLOUD;

    public QuantumfieldBlock() {
        // Properties of all quantumfield blocks
        // Change the first value in strength to get the wanted mining speed
        // Made material not solid so portals can't spawn on it.
        super(FabricBlockSettings
                        .of(Material.POWDER_SNOW)
                        .strength(0f, 2.0f)
                        .nonOpaque()
                        .solidBlock((state, world, pos) -> false)
                        .suffocates((state, world, pos) -> false)
                        .blockVision((state, world, pos) -> false)
                        .ticksRandomly()
                        .emissiveLighting((state, world, pos) -> true)
        );
        this.setDefaultState(getDefaultState().with(AGE, 0).with(MASTER, false).with(CLOUD, true));

    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Quantum fields placed by the player are not clouds
        return super.getPlacementState(ctx).with(CLOUD, false);
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return false;
    }

    public static boolean isMaster(BlockState state) {
        return state.getBlock() instanceof QuantumfieldBlock && state.get(MASTER);
    }

    public static int getAge(BlockState state) {
        return state.get(AGE);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return isMaster(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!isMaster(state)) return;

        int age = getAge(state);
        age = Math.min(age + DECAYRATE, MAX_AGE);
        if (age == MAX_AGE) {
            world.scheduleBlockTick(pos, this, 5);
        }
        state = state.with(AGE, age);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        int age = getAge(state);
        if (neighborState.isOf(state.getBlock()) && age < getAge(neighborState)) {
            age = getAge(neighborState);
            if (age == MAX_AGE) {
                world.scheduleBlockTick(pos, this, 5);
            }
            return state.with(AGE, age);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, MASTER, CLOUD);
    }

    public void removeQuantumBlockIfNeeded(BlockState state, ServerWorld world, BlockPos pos) {
        if (MAX_AGE == getAge(state)) {
            world.removeBlock(pos, false);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        removeQuantumBlockIfNeeded(state, world, pos);
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return isMaster(state) ? createMasterBlockEntity(pos, state) : null;
    }

    public BlockEntity createMasterBlockEntity(BlockPos pos, BlockState state) {
        return new QuantumFieldBlockEntity(pos, state);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        // Place block back
        world.setBlockState(pos, state, Block.NOTIFY_ALL);

        shrinkField(world, pos);
    }

    private Optional<BlockPos> getMasterPos(World world, BlockPos pos) {
        if (isMaster(world.getBlockState(pos))) return Optional.of(pos);

        return collectFieldBlocks(world, pos).stream().filter(p -> isMaster(world.getBlockState(p))).findFirst();
    }

    /**
     * Trace field by traversing all directions and count neighbors.
     */
    private Set<BlockPos> collectFieldBlocks(World world, BlockPos pos) {
        return collectFieldBlocks(world, pos, world.getBlockState(pos).getBlock(), new HashSet<>());
    }

    private Set<BlockPos> collectFieldBlocks(World world, BlockPos pos, Block block, Set<BlockPos> visited) {
        visited.add(pos);
        for (BlockPos neighbor : Arrays.stream(DIRECTIONS).map(pos::offset).toList()) {
            if (!world.getBlockState(neighbor).isOf(block)) continue;
            if (!visited.contains(neighbor))
                collectFieldBlocks(world, neighbor, block, visited);
        }
        return visited;
    }

    private void shrinkField(World world, BlockPos pos) {
        Set<BlockPos> field = collectFieldBlocks(world, pos);

        Optional<BlockPos> masterOpt = getMasterPos(world, pos);

        // No master found -> disconnected component so remove block itself
        if (masterOpt.isEmpty()){
            world.removeBlock(pos, false);
            return;
        }

        BlockPos master = masterOpt.get();
        Optional<BlockPos> edgePos = field.stream()
                .max(Comparator.comparing(master::getSquaredDistance));
        edgePos.ifPresent(p -> world.removeBlock(p, false));
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if (state.isOf(newState.getBlock()))
            return;

        // block removed
        if (pos.getY() == AtomicFloor.ATOMIC_FLOOR_LAYER) {
            world.setBlockState(pos, Blocks.ATOM_FLOOR.getDefaultState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // Only has collision from top
        if (!context.isAbove(VoxelShapes.fullCube(), pos, true) || context.isDescending()) {
            return VoxelShapes.empty();
        }
        return VoxelShapes.fullCube();
    }
}
