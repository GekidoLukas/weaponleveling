package net.weaponleveling.data.levelable_item.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class QuadraticFunction extends LevelingFunction{



    public static final MapCodec<QuadraticFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.DOUBLE.optionalFieldOf("coefficient", 0.4d).forGetter(QuadraticFunction::getCoefficient)
            ).apply(instance, QuadraticFunction::new)
    );

    public static final StreamCodec<ByteBuf, QuadraticFunction> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, QuadraticFunction::getCoefficient,
            QuadraticFunction::new
    );

    private double coefficient;

    public QuadraticFunction() {
        this(0.4d);
    }

    public QuadraticFunction(double coefficient) {
        this.coefficient = coefficient;
    }


    @Override
    public long calculateProgress(int level, int startingAmount) {
        return (long) (coefficient * ((long) level * level)  + startingAmount);
    }


    @Override
    public MapCodec<? extends LevelingFunction> getCodec() {
        return CODEC;
    }

    @Override
    public StreamCodec<ByteBuf, ? extends LevelingFunction> getStreamCodec() {
        return STREAM_CODEC;
    }

    public double getCoefficient() {
        return coefficient;
    }
}
