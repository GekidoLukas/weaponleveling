package net.weaponleveling.networking.message;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class PacketSendToast {

    public PacketSendToast(FriendlyByteBuf buf) {

    }

    public PacketSendToast() {

    }

    public void encode(FriendlyByteBuf buf) {
        // Encode data into the buf
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        // On receive
    }

}

