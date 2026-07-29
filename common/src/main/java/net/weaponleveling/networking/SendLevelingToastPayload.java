package net.weaponleveling.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingMod;

public record SendLevelingToastPayload(ItemStack stack, int level) implements CustomPacketPayload {
    public static final Type<SendLevelingToastPayload> TYPE =
            new Type<>(WeaponLevelingMod.id("send_leveling_toast"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SendLevelingToastPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, SendLevelingToastPayload::stack,
            ByteBufCodecs.VAR_INT, SendLevelingToastPayload::level,
            SendLevelingToastPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
