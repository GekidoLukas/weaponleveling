package net.weaponleveling.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ItemLevelData(int level, long levelprogress) {


    public static final Codec<ItemLevelData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("level").forGetter(ItemLevelData::level),
                    Codec.LONG.fieldOf("levelprogress").forGetter(ItemLevelData::levelprogress)
            ).apply(instance, ItemLevelData::new)
    );
    public static final StreamCodec<FriendlyByteBuf, ItemLevelData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ItemLevelData::level,
            ByteBufCodecs.VAR_LONG, ItemLevelData::levelprogress,
            ItemLevelData::new
    );

    public static ItemLevelData create(int level, long levelprogress) {
        return new ItemLevelData(level,levelprogress);
    }

}
