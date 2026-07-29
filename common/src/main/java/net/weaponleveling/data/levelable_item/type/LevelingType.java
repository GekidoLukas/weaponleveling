package net.weaponleveling.data.levelable_item.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.weaponleveling.api.registry.LevelingTypeRegistry;

public abstract class LevelingType {





    public static final Codec<LevelingType> CODEC = ResourceLocation.CODEC.dispatch(
            "type",
            LevelingTypeRegistry::getID,
            LevelingTypeRegistry::getCodecByID
    );

    public static final StreamCodec<ByteBuf, LevelingType> STREAM_CODEC = ResourceLocation.STREAM_CODEC.dispatch(
            LevelingTypeRegistry::getID,
            LevelingTypeRegistry::getStreamCodecByID
    );

    protected LevelingType() {

    }



    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LevelingType other) {
            return LevelingTypeRegistry.getID(other).equals(LevelingTypeRegistry.getID(this));
        } else {
            return false;
        }
    }

    public abstract MapCodec<? extends LevelingType> getCodec();
    public abstract StreamCodec<ByteBuf, ? extends LevelingType> getStreamCodec();
}
