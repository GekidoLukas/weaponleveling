package net.weaponleveling.data.levelable_item.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.weaponleveling.api.registry.LevelingFunctionRegistry;


public abstract class LevelingFunction {


    public static final Codec<LevelingFunction> CODEC = ResourceLocation.CODEC.dispatch(
            "function",
            LevelingFunctionRegistry::getID,
            LevelingFunctionRegistry::getCodecByID
    );

    public static final StreamCodec<ByteBuf, LevelingFunction> STREAM_CODEC = ResourceLocation.STREAM_CODEC.dispatch(
            LevelingFunctionRegistry::getID,
            LevelingFunctionRegistry::getStreamCodecByID
    );

    protected LevelingFunction() {

    }


    public abstract long calculateProgress(int level, int startingAmount);

    public abstract MapCodec<? extends LevelingFunction> getCodec();
    public abstract StreamCodec<ByteBuf, ? extends LevelingFunction> getStreamCodec();
}
