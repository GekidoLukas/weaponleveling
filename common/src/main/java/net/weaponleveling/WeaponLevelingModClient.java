package net.weaponleveling;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.weaponleveling.client.ClientEvents;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.networking.S2CRecievers;

import java.util.HashMap;

public class WeaponLevelingModClient {

    public static void init() {
        ClientTooltipEvent.ITEM.register(ClientEvents::onTooltipRender);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> {
            LevelableItemsLoader.setMap(new HashMap<>());
        });

        S2CRecievers.receive();
    }
}
