package net.weaponleveling;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.weaponleveling.client.ClientEvents;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.networking.Networking;

import java.util.HashMap;

public class WeaponLevelingModClient {

    public static void init() {
        ClientTooltipEvent.ITEM.register(ClientEvents::onTooltipRender);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> {
            LevelableItemsLoader.setMap(new HashMap<>());
        });

        Networking.registerS2CPackets();
    }
}
