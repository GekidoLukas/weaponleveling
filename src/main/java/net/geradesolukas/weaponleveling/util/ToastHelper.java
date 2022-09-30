package net.geradesolukas.weaponleveling.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.geradesolukas.weaponleveling.networking.Networking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class ToastHelper {


    public static void sendToast(ServerPlayerEntity player, ItemStack stack, int level) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeItemStack(stack);
        buf.writeInt(level);
        ServerPlayNetworking.send(player, Networking.LEVELUPTOAST, buf);
    }
}
