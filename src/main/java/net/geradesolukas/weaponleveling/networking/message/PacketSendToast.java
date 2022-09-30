package net.geradesolukas.weaponleveling.networking.message;


import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.geradesolukas.weaponleveling.util.CustomToast;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class PacketSendToast {

    public static void recieve(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
            PlayerEntity player = client.player;
            ItemStack stack = buf.readItemStack();
            int level = buf.readInt();
            MinecraftClient.getInstance().getToastManager().add(new CustomToast(stack, level));
    }

}
