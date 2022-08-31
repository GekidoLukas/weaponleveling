package net.geradesolukas.weaponleveling.util;

import net.geradesolukas.weaponleveling.networking.Networking;
import net.geradesolukas.weaponleveling.networking.message.PacketSendToast;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ToastHelper {


    public static void sendToast(ServerPlayer player, ItemStack stack, int level) {

        Networking.sendToClient(new PacketSendToast(stack,level),player);
    }
}
