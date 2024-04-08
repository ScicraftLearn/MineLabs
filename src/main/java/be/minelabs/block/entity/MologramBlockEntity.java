package be.minelabs.block.entity;

import be.minelabs.block.blocks.MologramBlock;
import be.minelabs.item.IMoleculeItem;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class MologramBlockEntity extends BlockEntity implements SidedStorageBlockEntity {
    private final SimpleInventory inventory = new SimpleInventory(1) {
        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            // Only molecule items allowed
            return stack.getItem() instanceof IMoleculeItem;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            // No stacking allowed
            if (!getStack(0).isEmpty())
                return false;
            if (!isValid(0, stack))
                return false;

            return super.canInsert(stack);
        }
    };

    public MologramBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.MOLOGRAM_BLOCK_ENTITY, pos, state);
        inventory.addListener(this::onInventoryChanged);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    public ItemStack getContents() {
        return inventory.getStack(0);
    }

    public void setContents(ItemStack stack) {
        inventory.setStack(0, stack);
    }

    @Override
    public @Nullable Storage<ItemVariant> getItemStorage(Direction side) {
        return InventoryStorage.of(inventory, null);
    }

    private void onInventoryChanged(Inventory inventory) {
        if (!(world instanceof ServerWorld serverWorld))
            return;

        BlockState state = serverWorld.getBlockState(pos);
        MologramBlock.onInventoryChanged(state, world, pos, inventory);
        // Tells the server block entity to sync its data to the client which is needed for rendering the contents.
        serverWorld.getChunkManager().markForUpdate(pos);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.put("Content", inventory.toNbtList());
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("Content"))
            inventory.readNbtList(tag.getList("Content", NbtElement.COMPOUND_TYPE));
    }

    //sync data server client:
    //Warning: Need to call world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS); to trigger the update. //
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

}