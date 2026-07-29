package net.weaponleveling.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.LevelableItem;

import java.util.HashMap;
import java.util.Map;

public record LevelableDataSyncPayload(Map<ResourceLocation, LevelableItem> itemMap) implements CustomPacketPayload {

    public static final Type<LevelableDataSyncPayload> TYPE =
            new Type<>(WeaponLevelingMod.id("sync_levelable_data"));


    public static final StreamCodec<RegistryFriendlyByteBuf, LevelableDataSyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeInt(payload.itemMap().size());
                payload.itemMap().forEach((resourceLocation, levelableItem) -> {
                    buf.writeResourceLocation(resourceLocation);
                    LevelableItem.STREAM_CODEC.encode(buf, levelableItem);
                });
            },
            buf -> {
                int size = buf.readInt();
                Map<ResourceLocation, LevelableItem> map = new HashMap<>(size);
                for (int i = 0; i < size; i++) {
                    ResourceLocation rl = buf.readResourceLocation();
                    LevelableItem levelableItem = LevelableItem.STREAM_CODEC.decode(buf);
                    map.put(rl, levelableItem);
                }
                return new LevelableDataSyncPayload(map);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
