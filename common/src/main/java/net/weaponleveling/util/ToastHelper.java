package net.weaponleveling.util;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static net.weaponleveling.networking.Networking.TOAST_PACKET_ID;

public class ToastHelper {
    public static void sendToast(ServerPlayer player, ItemStack stack, int level) {

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeItem(stack);
        buf.writeInt(level);
        NetworkManager.sendToPlayer(player, TOAST_PACKET_ID, buf);
    }

    public enum LevelUpType {ACTIONBAR, TOAST}
}
