package net.weaponleveling.data.levelable_item.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ExponentialFunction extends LevelingFunction{


    public static final MapCodec<ExponentialFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.DOUBLE.optionalFieldOf("coefficient", 0.4d).forGetter(ExponentialFunction::getCoefficient)
            ).apply(instance, ExponentialFunction::new)
    );

    public static final StreamCodec<ByteBuf, ExponentialFunction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, ExponentialFunction::getCoefficient,
            ExponentialFunction::new
    );

    private double coefficient;

    public ExponentialFunction() {
        this(0.4d);
    }

    public ExponentialFunction(double coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public long calculateProgress(int level, int startingAmount) {
        return (long) (coefficient * Math.pow(1.03d,level)  + startingAmount);
    }

    @Override
    public MapCodec<? extends LevelingFunction> getCodec() {
        return null;
    }

    @Override
    public StreamCodec<ByteBuf, ? extends LevelingFunction> getStreamCodec() {
        return STREAM_CODEC;
    }

    public double getCoefficient() {
        return coefficient;
    }
}
