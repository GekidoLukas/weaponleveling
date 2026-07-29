package net.weaponleveling.data.levelable_item.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class LinearFunction extends LevelingFunction{


    public static final MapCodec<LinearFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("slope", 80).forGetter(LinearFunction::getSlope)
            ).apply(instance, LinearFunction::new)
    );

    public static final StreamCodec<ByteBuf, LinearFunction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, LinearFunction::getSlope,
            LinearFunction::new
    );

    private int slope;

    public LinearFunction() {
        this(80);
    }

    public LinearFunction(int slope) {
        this.slope = slope;
    }

    @Override
    public long calculateProgress(int level, int startingAmount) {
        return (long) slope * level  + startingAmount;
    }

    @Override
    public MapCodec<? extends LevelingFunction> getCodec() {
        return CODEC;
    }
    @Override
    public StreamCodec<ByteBuf, ? extends LevelingFunction> getStreamCodec() {
        return STREAM_CODEC;
    }

    public int getSlope() {
        return slope;
    }
}
