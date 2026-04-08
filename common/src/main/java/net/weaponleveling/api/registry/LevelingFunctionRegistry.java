package net.weaponleveling.api.registry;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static boolean hasObject(LevelingFunction function) {
        for(var entry : FUNCTION_MAP.entrySet()) {
            if(entry.getValue().getClass() == function.getClass()) return true;
        }
        return false;
    }



    public static LevelingFunction fromJson(JsonElement parentElement) {

        if(parentElement.isJsonObject()) {
            try {
                LevelingFunction jsonType = getByID(new ResourceLocation(parentElement.getAsJsonObject().get("function").getAsString()));
                if(jsonType != null) {
                    jsonType.setData(parentElement.getAsJsonObject());
                    return jsonType;
                }
            } catch (Exception ignored){}
        }


        return LevelingFunctions.LINEAR;
    }

    public static LevelingFunction fromNBT(CompoundTag tag) {
        LevelingFunction levelingFunction =getByID(new ResourceLocation(tag.getString("function")));
        if(levelingFunction != null) levelingFunction.setData(tag);

        return levelingFunction;
    }

    public static boolean hasValidInNBT(ListTag tag) {
        int correctOnes = 0;
        for (var item : tag) {
            if(item instanceof CompoundTag compoundTag) {
                LevelingFunction levelingFunction = fromNBT(compoundTag);
                if(levelingFunction != null) correctOnes++;
            }
        }
        return correctOnes > 0;
    }
}
