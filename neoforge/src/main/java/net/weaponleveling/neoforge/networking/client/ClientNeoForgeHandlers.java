package net.weaponleveling.neoforge.networking.client;

import dev.architectury.networking.NetworkManager;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.neoforge.networking.ConfigCheckC2SPayload;
import net.weaponleveling.neoforge.networking.ConfigCheckS2CPayload;

public class ClientNeoForgeHandlers {




    public static void handleConfigCheckS2CPayload(ConfigCheckS2CPayload payload, NetworkManager.PacketContext context) {

        context.queue(() -> {
            boolean serverOption = payload.option();
            boolean clientOption = WeaponLevelingConfig.apply_ranged_damage_attribute;

            NetworkManager.sendToServer(new ConfigCheckC2SPayload(clientOption));
        });
    }
}
