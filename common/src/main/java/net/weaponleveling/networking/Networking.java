package net.weaponleveling.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.util.CustomToast;

public class Networking {

    public static final ResourceLocation TOAST_PACKET_ID = new ResourceLocation(WeaponLevelingMod.MODID, "toast_packet");


    public static void registerC2SPackets() {
    }

    public static void registerS2CPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, TOAST_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack stack = buf.readItem();
            int level = buf.readInt();
            Minecraft.getInstance().getToasts().addToast(new CustomToast(stack, level));
        });
    }

}
