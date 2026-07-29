package net.weaponleveling.neoforge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.weaponleveling.WeaponLevelingMod;

public record ConfigCheckS2CPayload(boolean option) implements CustomPacketPayload {

    public static final Type<ConfigCheckS2CPayload> TYPE =
            new Type<>(WeaponLevelingMod.id("config_check_s2c"));

    public static final StreamCodec<FriendlyByteBuf, ConfigCheckS2CPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ConfigCheckS2CPayload::option,
            ConfigCheckS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
