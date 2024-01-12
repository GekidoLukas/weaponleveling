package net.weaponleveling.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.CustomToast;

public class S2CRecievers {



    public static void receive() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, Networking.TOAST_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack stack = buf.readItem();
            int level = buf.readInt();
            Minecraft.getInstance().getToasts().addToast(new CustomToast(stack, level));
        });
    }
}
