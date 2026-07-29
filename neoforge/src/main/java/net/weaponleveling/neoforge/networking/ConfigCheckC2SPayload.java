package net.weaponleveling.neoforge.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.weaponleveling.WeaponLevelingMod;


public record ConfigCheckC2SPayload(boolean option) implements CustomPacketPayload {

    public static final Type<ConfigCheckC2SPayload> TYPE =
            new Type<>(WeaponLevelingMod.id("config_check_c2s"));

    public static final StreamCodec<FriendlyByteBuf, ConfigCheckC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ConfigCheckC2SPayload::option,
            ConfigCheckC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
