package net.weaponleveling.api.registry;

import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.LevelableAttribute;
import net.weaponleveling.data.levelable_item.type.LevelingType;

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

    public static boolean hasObject(LevelingType type) {
        for(var entry : TYPE_MAP.entrySet()) {
            if(entry.getValue().getClass() == type.getClass()) return true;
        }
        return false;
    }



    public static List<LevelingType> fromJson(JsonElement parentElement) {
        List<LevelingType> types = new ArrayList<>();

        if(parentElement.isJsonArray()) {
            for(JsonElement element : parentElement.getAsJsonArray()) {
                try {
                    LevelingType jsonType = getByID(new ResourceLocation(element.getAsJsonObject().get("type").getAsString()));
                    if(jsonType == null) continue;
                    jsonType.setData(element.getAsJsonObject());
                    types.add(jsonType);
                } catch (Exception ignored){
                    continue;
                }
            }
        } else if(parentElement.isJsonObject()) {
            try {
                LevelingType jsonType = getByID(new ResourceLocation(parentElement.getAsJsonObject().get("type").getAsString()));
                if(jsonType != null) {
                    jsonType.setData(parentElement.getAsJsonObject());
                    types.add(jsonType);
                }
            } catch (Exception ignored){}
        }


        return types;
    }

    public static LevelingType fromNBT(CompoundTag tag) {
        LevelingType levelingType = getByID(new ResourceLocation(tag.getString("type")));
        if(levelingType != null) levelingType.setData(tag);

        return levelingType;
    }

    public static boolean hasValidInNBT(ListTag tag) {
        int correctOnes = 0;
        for (var item : tag) {
            if(item instanceof CompoundTag compoundTag) {
                LevelingType levelingType = fromNBT(compoundTag);
                if(levelingType != null) correctOnes++;
            }
        }
        return correctOnes > 0;
    }
}
