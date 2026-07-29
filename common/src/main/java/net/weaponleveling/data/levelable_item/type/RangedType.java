package net.weaponleveling.data.levelable_item.type;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class RangedType extends LevelingType{

    public static final MapCodec<RangedType> CODEC = MapCodec.unit(RangedType::new);

    public static final StreamCodec<ByteBuf, RangedType> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {},
            buf -> new RangedType()
    );

    @Override
    public MapCodec<? extends LevelingType> getCodec() {
        return CODEC;
    }

    @Override
    public StreamCodec<ByteBuf, ? extends LevelingType> getStreamCodec() {
        return STREAM_CODEC;
    }
}
