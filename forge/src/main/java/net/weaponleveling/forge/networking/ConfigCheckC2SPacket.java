package net.weaponleveling.forge.networking;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.weaponleveling.WeaponLevelingConfig;

import java.util.function.Supplier;

public class ConfigCheckC2SPacket {

    private final boolean option;

    public ConfigCheckC2SPacket(boolean option) {
        this.option = option;
    }

    public ConfigCheckC2SPacket(FriendlyByteBuf buf) {
        this.option = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(option);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                boolean clientOption = option;
                boolean serverOption = WeaponLevelingConfig.apply_ranged_damage_attribute;

                if (clientOption != serverOption) {
                    player.connection.disconnect(Component.empty().withStyle(ChatFormatting.RED)
                            .append(Component.literal("Config mismatch!\n Config Option \""))
                            .append(Component.literal("apply_ranged_damage_attribute").withStyle(ChatFormatting.GOLD))
                            .append(Component.literal("\" is set to \""))
                            .append(Component.literal(""+ serverOption).withStyle(ChatFormatting.GOLD))
                            .append(Component.literal("\" on the Server and set to \""))
                            .append(Component.literal(""+ clientOption).withStyle(ChatFormatting.GOLD))
                            .append(Component.literal("\" on the Client."))
                    );
                }

            }
        });
        ctx.get().setPacketHandled(true);
    }
}
