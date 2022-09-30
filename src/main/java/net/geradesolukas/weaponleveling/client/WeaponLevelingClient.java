package net.geradesolukas.weaponleveling.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.geradesolukas.weaponleveling.networking.Networking;

@Environment(EnvType.CLIENT)
public class WeaponLevelingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ItemTooltipCallback.EVENT.register(((stack, context, lines) ->
                ClientEvents.onTooltipRender(stack,lines,context)));

        Networking.registerS2CPackets();
    }
}
