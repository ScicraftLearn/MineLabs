package be.minelabs.client.network;

import be.minelabs.block.entity.IonicBlockEntity;
import be.minelabs.block.entity.LewisBlockEntity;
import be.minelabs.network.NetworkingConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class ClientNetworking {
    public static void onInitializeClient() {
        registerS2CPacketHandlers();
    }

    public static void registerS2CPacketHandlers() {
        //Receive on CLIENT
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.LEWISDATASYNC, (c, h, buf, sender) -> {
            BlockPos pos = buf.readBlockPos();
            int size = buf.readInt();
            DefaultedList<Ingredient> ingredients = DefaultedList.of();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    ingredients.add(Ingredient.fromPacket(buf));
                }
            }
            BlockEntity be = c.world.getBlockEntity(pos);
            if (be instanceof LewisBlockEntity lewis) {
                lewis.setIngredients(ingredients);
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.IONICDATASYNC, (c, h, buf, sender) -> {
            BlockPos pos = buf.readBlockPos();
            int size = buf.readInt();
            DefaultedList<Ingredient> ingredients = DefaultedList.of();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    ingredients.add(Ingredient.fromPacket(buf));
                }
            }
            int split = buf.readByte();

            BlockEntity be = c.world.getBlockEntity(pos);
            if (be instanceof IonicBlockEntity ionic) {
                ionic.setIngredients(ingredients);
                ionic.setSplit(split);
            }
        });
    }
}
