package net.geradesolukas.weaponleveling.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.networking.message.PacketSendToast;
import net.geradesolukas.weaponleveling.util.CustomToast;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class Networking {


    public static final Identifier LEVELUPTOAST = new Identifier(WeaponLeveling.MODID, "leveluptoast");

    public static void registerC2SPackets() {
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(LEVELUPTOAST,PacketSendToast::recieve);
    }



}
