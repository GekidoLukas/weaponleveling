package net.weaponleveling;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.weaponleveling.client.ClientEvents;
import net.weaponleveling.networking.Networking;

public class WeaponLevelingModClient {

    public static void init() {
        ClientTooltipEvent.ITEM.register(ClientEvents::onTooltipRender);

        Networking.registerS2CPackets();
    }
}
