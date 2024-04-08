package be.minelabs.block.entity;

import be.minelabs.inventory.AtomicInventory;
import be.minelabs.screen.AtomStorageScreenHandler;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class AtomicStorageBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, SidedStorageBlockEntity {

    private AtomicInventory inventory = new AtomicInventory(AtomicInventory.STORAGE_STACK);

    public AtomicStorageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.ATOMIC_STORAGE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AtomStorageScreenHandler(syncId, playerInventory, inventory);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return super.createNbt();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        inventory.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.inventory = new AtomicInventory(AtomicInventory.STORAGE_STACK);
        inventory.readNbt(nbt);
        super.readNbt(nbt);
    }

    public AtomicInventory getInventory() {
        return inventory;
    }

    @Override
    public @Nullable Storage<ItemVariant> getItemStorage(Direction side) {
        if (side == Direction.UP) {
            return null; // no insert/extract for up
        }
        return InventoryStorage.of(inventory, side);
    }
}
