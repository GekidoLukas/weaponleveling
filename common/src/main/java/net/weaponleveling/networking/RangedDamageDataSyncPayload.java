package net.weaponleveling.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.ranged_damage.RangedDamageEntry;

import java.util.HashMap;
import java.util.Map;

public record RangedDamageDataSyncPayload(Map<ResourceLocation, RangedDamageEntry> itemMap) implements CustomPacketPayload {

    public static final Type<RangedDamageDataSyncPayload> TYPE =
            new Type<>(WeaponLevelingMod.id("sync_ranged_damage_data"));


    public static final StreamCodec<RegistryFriendlyByteBuf, RangedDamageDataSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeInt(payload.itemMap().size());
                payload.itemMap().forEach((resourceLocation, rangedDamageEntry) -> {
                    buf.writeResourceLocation(resourceLocation);
                    RangedDamageEntry.STREAM_CODEC.encode(buf, rangedDamageEntry);
                });
            },
            buf -> {
                int size = buf.readInt();
                Map<ResourceLocation, RangedDamageEntry> map = new HashMap<>(size);
                for (int i = 0; i < size; i++) {
                    ResourceLocation rl = buf.readResourceLocation();
                    RangedDamageEntry rangedDamageEntry = RangedDamageEntry.STREAM_CODEC.decode(buf);
                    map.put(rl, rangedDamageEntry);
                }
                return new RangedDamageDataSyncPayload(map);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
