package net.weaponleveling;

import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.client.ClientEvents;
import net.weaponleveling.networking.Networking;
import net.weaponleveling.util.CustomToast;

import static net.weaponleveling.networking.Networking.TOAST_PACKET_ID;

public class WeaponLevelingModClient {

    public static void init() {
        ClientTooltipEvent.ITEM.register(ClientEvents::onTooltipRender);

        Networking.registerS2CPackets();
    }
}
