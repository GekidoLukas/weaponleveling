package net.weaponleveling.api.registry;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.LevelableAttribute;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.function.LinearFunction;
import net.weaponleveling.data.levelable_item.type.EmptyType;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.data.levelable_item.type.LevelingTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelingTypeRegistry {


    private static final Map<ResourceLocation, LevelingType> TYPE_MAP = new HashMap<>();


    public static void register(ResourceLocation id, LevelingType type) {
        TYPE_MAP.put(id,type);
    }



    public static LevelingType getByID(ResourceLocation id) {
        Class<? extends LevelingType> actionClass = TYPE_MAP.getOrDefault(id, null).getClass();
        try {
            return actionClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }

    }

    public static ResourceLocation getID(LevelingType type) {
        for(var entry : TYPE_MAP.entrySet()) {
            if(entry.getValue().getClass() == type.getClass()) return entry.getKey();
        }
        return WeaponLevelingMod.id("empty");
    }


    public static MapCodec<? extends LevelingType> getCodecByID(ResourceLocation id) {
        LevelingType function = TYPE_MAP.get(id);
        if (function != null) {
            return function.getCodec();
        }
        return EmptyType.CODEC;
    }

    public static StreamCodec<ByteBuf, ? extends LevelingType> getStreamCodecByID(ResourceLocation id) {
        LevelingType function = TYPE_MAP.get(id);
        if (function != null) {
            return function.getStreamCodec();
        }
        return EmptyType.STREAM_CODEC;
    }

    public static boolean hasObject(LevelingType type) {
        for(var entry : TYPE_MAP.entrySet()) {
            if(entry.getValue().getClass() == type.getClass()) return true;
        }
        return false;
    }



    public static List<LevelingType> fromJson(JsonElement parentElement) {
        return  LIST_CODEC.parse(JsonOps.INSTANCE,parentElement).result().orElse(List.of(LevelingTypes.EMPTY));
    }


    public static final Codec<List<LevelingType>> LIST_CODEC = Codec.either(
            LevelingType.CODEC.listOf(),
            LevelingType.CODEC
    ).xmap(
            either -> either.map(
                    list -> list,
                    List::of
            ),
            list -> list.size() == 1 ? Either.right(list.getFirst()) : Either.left(list)
    );
}
