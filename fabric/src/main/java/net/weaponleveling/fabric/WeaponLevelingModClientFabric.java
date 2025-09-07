package net.weaponleveling.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.weaponleveling.WLConfigReader;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingModClient;

import java.util.concurrent.CompletableFuture;

public class WeaponLevelingModClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WeaponLevelingModClient.init();
        ClientLoginNetworking.registerGlobalReceiver(WLConfigReader.CONFIG_CHANNEL, (client, handler, buf, listenerAdder) -> {
            boolean serverOption = buf.readBoolean();
            boolean clientOption = WeaponLevelingConfig.apply_ranged_damage_attribute;

            FriendlyByteBuf reply = PacketByteBufs.create();
            reply.writeBoolean(clientOption);

            return CompletableFuture.completedFuture(reply);
        });
    }
}
