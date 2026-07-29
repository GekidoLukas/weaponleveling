package net.weaponleveling.api.registry;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.function.LinearFunction;

import java.util.HashMap;
import java.util.Map;

public class LevelingFunctionRegistry {


    private static final Map<ResourceLocation, LevelingFunction> FUNCTION_MAP = new HashMap<>();


    public static void register(ResourceLocation id, LevelingFunction function) {
        FUNCTION_MAP.put(id,function);
    }



    public static LevelingFunction getByID(ResourceLocation id) {
        Class<? extends LevelingFunction> actionClass = FUNCTION_MAP.getOrDefault(id, LevelingFunctions.LINEAR).getClass();
        try {
            return actionClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return LevelingFunctions.LINEAR;
        }

    }

    public static ResourceLocation getID(LevelingFunction function) {
        for(var entry : FUNCTION_MAP.entrySet()) {
            if(entry.getValue().getClass() == function.getClass()) return entry.getKey();
        }
        return WeaponLevelingMod.id("linear");
    }


    public static MapCodec<? extends LevelingFunction> getCodecByID(ResourceLocation id) {
        LevelingFunction function = FUNCTION_MAP.get(id);
        if (function != null) {
            return function.getCodec();
        }
        return LinearFunction.CODEC;
    }

    public static StreamCodec<ByteBuf, ? extends LevelingFunction> getStreamCodecByID(ResourceLocation id) {
        LevelingFunction function = FUNCTION_MAP.get(id);
        if (function != null) {
            return function.getStreamCodec();
        }
        return LinearFunction.STREAM_CODEC;
    }

    public static boolean hasObject(LevelingFunction function) {
        for(var entry : FUNCTION_MAP.entrySet()) {
            if(entry.getValue().getClass() == function.getClass()) return true;
        }
        return false;
    }


    public static LevelingFunction fromJson(JsonElement parentElement) {
        return LevelingFunction.CODEC.parse(JsonOps.INSTANCE, parentElement).result().orElse(LevelingFunctions.LINEAR);
    }
}
