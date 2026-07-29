package net.weaponleveling.data.levelable_item.type;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PassiveHandType extends LevelingType{


    public static final MapCodec<PassiveHandType> CODEC = MapCodec.unit(PassiveHandType::new);

    public static final StreamCodec<ByteBuf, PassiveHandType> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {},
            buf -> new PassiveHandType()
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
