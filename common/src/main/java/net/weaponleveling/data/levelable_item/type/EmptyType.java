package net.weaponleveling.data.levelable_item.type;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class EmptyType extends LevelingType {

    public static final MapCodec<EmptyType> CODEC = MapCodec.unit(EmptyType::new);

    public static final StreamCodec<ByteBuf, EmptyType> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {},
            buf -> new EmptyType()
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
