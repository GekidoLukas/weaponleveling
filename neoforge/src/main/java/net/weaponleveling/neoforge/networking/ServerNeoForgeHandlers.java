package net.weaponleveling.neoforge.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.weaponleveling.WeaponLevelingConfig;

public class ServerNeoForgeHandlers {




    public static void handleConfigCheckC2SPayload(ConfigCheckC2SPayload payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getPlayer() instanceof ServerPlayer player) {
                boolean clientOption = payload.option();
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

    }
}
