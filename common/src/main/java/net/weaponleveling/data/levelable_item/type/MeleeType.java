package net.weaponleveling.data.levelable_item.type;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class MeleeType extends LevelingType{


    public static final MapCodec<MeleeType> CODEC = MapCodec.unit(MeleeType::new);

    public static final StreamCodec<ByteBuf, MeleeType> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {},
            buf -> new MeleeType()
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
