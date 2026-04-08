package net.weaponleveling.forge.networking;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.weaponleveling.WLConfigReader;

public class ForgeNetworking {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CONFIG_CHANNEL = NetworkRegistry.newSimpleChannel(
            WLConfigReader.CONFIG_CHANNEL,
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CONFIG_CHANNEL.messageBuilder(ConfigCheckC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ConfigCheckC2SPacket::new)
                .encoder(ConfigCheckC2SPacket::encode)
                .consumerMainThread(ConfigCheckC2SPacket::handle)
                .add();

        CONFIG_CHANNEL.messageBuilder(ConfigCheckS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ConfigCheckS2CPacket::new)
                .encoder(ConfigCheckS2CPacket::encode)
                .consumerMainThread(ConfigCheckS2CPacket::handle)
                .add();
    }
}
