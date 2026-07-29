package net.weaponleveling.data.levelable_item.type;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PassiveInventoryType extends LevelingType{



    public static final MapCodec<PassiveInventoryType> CODEC = MapCodec.unit(PassiveInventoryType::new);

    public static final StreamCodec<ByteBuf, PassiveInventoryType> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {},
            buf -> new PassiveInventoryType()
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
