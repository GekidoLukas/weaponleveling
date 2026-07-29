package net.weaponleveling.util;

import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.networking.SendLevelingToastPayload;


public class ToastHelper {
    public static void sendToast(ServerPlayer player, ItemStack stack, int level) {
        NetworkManager.sendToPlayer(player,new SendLevelingToastPayload(stack,level));
    }

    public enum LevelUpType {ACTIONBAR, TOAST}
}
