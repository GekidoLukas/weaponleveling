package net.weaponleveling.forge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.weaponleveling.WeaponLevelingConfig;

import java.util.function.Supplier;

public class ConfigCheckS2CPacket {
    private final boolean option;

    public ConfigCheckS2CPacket(boolean option) {
        this.option = option;
    }

    public ConfigCheckS2CPacket(FriendlyByteBuf buf) {
        this.option = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(option);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            boolean serverOption = option;
            boolean clientOption = WeaponLevelingConfig.apply_ranged_damage_attribute;

            ForgeNetworking.CONFIG_CHANNEL.sendToServer(new ConfigCheckC2SPacket(clientOption));
        });
        ctx.get().setPacketHandled(true);
    }
}
