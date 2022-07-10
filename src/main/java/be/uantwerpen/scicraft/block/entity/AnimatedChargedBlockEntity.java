package be.uantwerpen.scicraft.block.entity;

import be.uantwerpen.scicraft.block.Blocks;
import be.uantwerpen.scicraft.block.ChargedBlock;
import be.uantwerpen.scicraft.block.ChargedPionBlock;
import be.uantwerpen.scicraft.item.Items;
import be.uantwerpen.scicraft.network.NetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;

public class AnimatedChargedBlockEntity extends BlockEntity {
    public long time = 0;
    public Vec3i movement_direction = Vec3i.ZERO;
    public final static int time_move_ticks = 8;
    public BlockState render_state = net.minecraft.block.Blocks.AIR.getDefaultState();
    public boolean annihilation = false;

    public AnimatedChargedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        // Inputs from the packet from the server
        tag.putLong("time", time);
        tag.putInt("md", ChargedBlockEntity.vec2int(movement_direction));
        tag.putString("rs", render_state.toString());
        super.writeNbt(tag);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound tag) {
        time = tag.getLong("time");
        movement_direction = ChargedBlockEntity.int2vec(tag.getInt("md"));
        render_state = string2BlockState(tag.getString("rs"));
        super.readNbt(tag);
    }

    public BlockState string2BlockState(String blockNameState) {
        // write the lines for the packet to the client/nbt tag
        boolean withProperty = false;
        String blockValue = "0";
        String[] blockNames = blockNameState.toUpperCase().split("}",2);
        String blockName = blockNames[0].split(":",2)[1];
        String[] blockStates = blockNames[1].split("=",2);
        if (blockStates.length == 2) {
            String blockState = blockStates[0].split("\\[",2)[1];
            blockValue = blockStates[1].split("\\]",2)[0];
            if (Objects.equals(blockState, "AGE")) {
                withProperty = true;
            }
        }
        Object o = new Blocks();
        Class<?> c = o.getClass();
        Field f = null;
        try {
            f = c.getDeclaredField(blockName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        assert f != null;
        f.setAccessible(true);
        BlockState outState = net.minecraft.block.Blocks.AIR.getDefaultState();
        try {
            if (withProperty) {
                outState = ((ChargedBlock) f.get(o)).getDefaultState().with(ChargedPionBlock.COLOUR, Integer.parseInt(blockValue));
            } else {
                outState = ((ChargedBlock) f.get(o)).getDefaultState();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return outState;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            if (time == 0) {
                //start animation
                time = world.getTime();

                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(pos);
                buf.writeString(render_state.toString());
                buf.writeBoolean(annihilation);
                for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, pos)) {
                    // update client on the block for the animation
                    ServerPlayNetworking.send(player, NetworkingConstants.CHARGED_MOVE_STATE, buf);
                }
            }
            if (world.getTime() - time > time_move_ticks) {
                // remove block after animation
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
                if (world.getBlockState(pos.mutableCopy().add(movement_direction)).getBlock().equals(be.uantwerpen.scicraft.block.Blocks.CHARGED_PLACEHOLDER)) { //also change other particle for client
                    world.setBlockState(pos.mutableCopy().add(movement_direction), render_state, Block.NOTIFY_ALL);
                }
                if (annihilation) {
                    // also spawn some photons for the annihilation.
                    ItemStack itemStack = new ItemStack(Items.PHOTON, 1);
                    double a = pos.getX() + movement_direction.getX() / 2d;
                    double b = pos.getY() + movement_direction.getY() / 2d;
                    double c = pos.getZ() + movement_direction.getZ() / 2d;
                    ItemEntity itemEntity = new ItemEntity(world, a, b, c, itemStack);
                    world.spawnEntity(itemEntity);
                }
                markDirty();
            }
        } else {
            if (time == 0) {
                // client --> do nothing, only render.
                time = world.getTime();
            }
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, AnimatedChargedBlockEntity be) {
        be.tick(world, pos, state);
    }
}
