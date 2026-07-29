package net.weaponleveling.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.weaponleveling.WeaponLevelingMod;

public record ConfigSyncPayload(boolean brokenItemsWontVanish,
                                boolean levelableItemsAutoUnbreakable,
                                int hitXpAmount,
                                int hitXpChance,
                                int maxItemLevel,
                                int startingXpAmount,
                                int xpApplyChance,
                                double valuePerLevel
) implements CustomPacketPayload {
    public static final Type<ConfigSyncPayload> TYPE =
            new Type<>(WeaponLevelingMod.id("sync_config"));

    public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeBoolean(payload.brokenItemsWontVanish());
                buf.writeBoolean(payload.levelableItemsAutoUnbreakable());
                buf.writeInt(payload.hitXpAmount());
                buf.writeInt(payload.hitXpChance());
                buf.writeInt(payload.maxItemLevel());
                buf.writeInt(payload.startingXpAmount());
                buf.writeInt(payload.xpApplyChance());
                buf.writeDouble(payload.valuePerLevel());
            },
            buf -> new ConfigSyncPayload(
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readDouble()
            )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
