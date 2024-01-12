package net.weaponleveling.networking;

import net.minecraft.resources.ResourceLocation;
import net.weaponleveling.WeaponLevelingMod;

public class Networking {

    public static final ResourceLocation TOAST_PACKET_ID = new ResourceLocation(WeaponLevelingMod.MODID, "toast_packet");


    public static void registerC2SPackets() {
    }

    public static void registerS2CPackets() {
        S2CRecievers.receive();
    }

}
