package be.minelabs.util;

import be.minelabs.block.Blocks;
import be.minelabs.block.blocks.QuantumfieldBlock;
import be.minelabs.block.entity.QuantumFieldBlockEntity;
import be.minelabs.state.property.Properties;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class QuantumFieldSpawner {
    static java.util.Random r = new Random();

    public static void tryToSpawnCloud(World world, BlockPos pos) {
        BlockState field_to_spawn;
        int max_fields = 10;
//        in chunks to each side to check
        int delta_half_r = 3;
        float chance;
        int cloud_count = 0;
        WorldChunk worldChunk;
        Map<BlockPos, BlockEntity> blockEntityMap;
        for (int x = -delta_half_r; x <= delta_half_r; x++) {
            for (int z = -delta_half_r; z <= delta_half_r; z++) {
                worldChunk = world.getWorldChunk(pos.add(x * 16, 0, z * 16));
                blockEntityMap = worldChunk.getBlockEntities();
                for (BlockEntity entry : blockEntityMap.values()) {
                    if (entry instanceof QuantumFieldBlockEntity){
                        cloud_count ++;
                    }
                }
            }
        }

        chance = (float) (max_fields-cloud_count) / max_fields;
        chance = chance*chance*chance*chance*chance;

        if (shouldSpawnCloud(chance)) {
            field_to_spawn = pickField(world, pos);
            if (field_to_spawn != null) {
                spawnCloud(world, pos, field_to_spawn);
            }
        }
    }

    public static Boolean shouldSpawnCloud(float chance) {
        java.util.Random r = new java.util.Random();
        return r.nextFloat() <= chance;
    }

    public static BlockState pickField(World world, BlockPos pos) {
        BlockState quantumfield = null;
        int type = world.random.nextInt(7);
        //Only change air blocks so other fields don't get replaced
        switch (type) {
            case 0 -> quantumfield = Blocks.ELECTRON_QUANTUMFIELD.getDefaultState().with(QuantumfieldBlock.CLOUD, true);
            case 1 -> quantumfield = Blocks.DOWNQUARK_QUANTUMFIELD.getDefaultState().with(QuantumfieldBlock.CLOUD, true);
            case 2 -> quantumfield = Blocks.GLUON_QUANTUMFIELD.getDefaultState().with(QuantumfieldBlock.CLOUD, true);
            case 3 -> quantumfield = Blocks.PHOTON_QUANTUMFIELD.getDefaultState().with(QuantumfieldBlock.CLOUD, true);
            case 4 -> quantumfield = Blocks.UPQUARK_QUANTUMFIELD.getDefaultState().with(QuantumfieldBlock.CLOUD, true);
            case 5 -> quantumfield = Blocks.WEAK_BOSON_QUANTUMFIELD.getDefaultState().with(QuantumfieldBlock.CLOUD, true);
            case 6 -> quantumfield = Blocks.NEUTRINO_QUANTUMFIELD.getDefaultState().with(QuantumfieldBlock.CLOUD, true);
        }
        return quantumfield;
    }

    private static void spawnCloud(World world, BlockPos pos, BlockState state) {
        int x_size = r.nextInt(5, 25);
        int y_size = r.nextInt(4, 15);
        int z_size = r.nextInt(5, 25);
        int max_cloud_height = 100;
        int cloudHeight = r.nextInt(0, max_cloud_height) - max_cloud_height / 2;

        pos = pos.up(cloudHeight);
        float a = x_size / 2.0f;
        float b = y_size / 2.0f;
        float c = z_size / 2.0f;
        float x_sub, y_sub, z_sub;
        BlockState tempState;

//      only replace air and not too close to other fields
        /**
         * Elipse:   x^2/a^2 + y^2/b^2 + z^2/c^2 = 1
         * (a,0,0); (0,b,0),(0,0,c)
         */
        if (!checkFields(pos, world, x_size, y_size, z_size)) {
            ArrayList<BlockPos> placedPositions = Lists.newArrayList();
            placeQuantumField(world, pos, state.with(Properties.MASTER, true));
            placedPositions.add(pos);
            for (int x = -x_size / 2; x <= x_size / 2; x++) {
//                x_sub = x^2/a^2
                x_sub = x * x / (a * a);
                for (int y = -y_size / 2; y <= y_size / 2; y++) {
//                y_sub = y^2/b^2
                    y_sub = y * y / (b * b);
                    for (int z = -z_size / 2; z <= z_size / 2; z++) {
//                z_sub = z^2/b^2
                        z_sub = z * z / (c * c);
                        if (x_sub + z_sub + y_sub <= 1.0f) {
                            BlockPos blockPos = new BlockPos(x + pos.getX(), y + pos.getY(), z + pos.getZ());
                            tempState = world.getBlockState(blockPos);
                            if (tempState.isAir() || tempState.getBlock().equals(Blocks.ATOM_FLOOR)) {
                                placeQuantumField(world, blockPos, state);
                                placedPositions.add(blockPos);
                            }
                        }
                    }
                }
            }
            // Neighbours are updated after everything is placed. See FillCommand.
            for (BlockPos blockPos : placedPositions) {
                Block block = world.getBlockState(blockPos).getBlock();
                world.updateNeighbors(blockPos, block);
            }
        }
    }

    private static void placeQuantumField(World world, BlockPos pos, BlockState state){
        state = Block.postProcessState(state, world, pos);
        // only update listeners at first, neighbours are updated after. See FillCommand.
        world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
    }

    //Checking if there are no other clouds near
    private static Boolean checkFields(BlockPos pos, World world, int x_size, int y_size, int z_size) {
        int blocks_between = 5;
        BlockPos pos1 = pos.subtract(new Vec3i(x_size / 2 + blocks_between, z_size / 2 + blocks_between, y_size / 2 + blocks_between));
        BlockPos pos2 = pos.add(new Vec3i(x_size / 2 + blocks_between, z_size / 2 + blocks_between, y_size / 2 + blocks_between));
        BlockState tempState;
        Box cloudbox = new Box(pos, pos2);
        if (world instanceof ServerWorld serverWorld) {
            for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
                if (cloudbox.contains(serverPlayerEntity.getPos())) {
                    return true;
                }
            }
        }
        for (int x = pos1.getX(); x <= pos2.getX(); x++) {

            for (int y = pos1.getY(); y <= pos2.getY(); y++) {
                for (int z = pos1.getZ(); z <= pos2.getZ(); z++) {
                    tempState = world.getBlockState(new BlockPos(x, y, z));
                    if (!(tempState.isAir() || tempState.getBlock().equals(Blocks.ATOM_FLOOR))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
