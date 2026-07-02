package net.weaponleveling.data.levelable_item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.api.registry.LevelingFunctionRegistry;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.api.registry.LevelingTypeRegistry;
import net.weaponleveling.data.levelable_item.type.WornType;

import java.util.ArrayList;
import java.util.List;

public class LevelableItem {

    private final Item item;
    private final List<LevelableAttribute> attributes;
    private final List<LevelingType> types;

    private final int maxLevel;
    private final int levelStartAmount;

    private final int hitXPAmount;
    private final int hitXPChance;

    private final int armorXPRNGModifier;

    //private final String leveltype;
    private final LevelingFunction function;


    public LevelableItem(Item item,List<LevelableAttribute> attributes, List<LevelingType> types,LevelingFunction function , int maxLevel, int levelStartAmount, int hitXPAmount, int hitXPChance, int armorXPRNGModifier) {
        this.item = item;
        this.attributes = attributes;
        this.types = types;
        this.function = function;

        this.maxLevel = maxLevel;
        this.levelStartAmount = levelStartAmount;

        this.hitXPAmount = hitXPAmount;
        this.hitXPChance = hitXPChance;


        this.armorXPRNGModifier = armorXPRNGModifier;
    }

    public List<LevelableAttribute> getAttributes() {
        return attributes;
    }

    public List<LevelingType> getTypes() {
        return types;
    }

    public Item getItem() {
        return item;
    }


    public int getMaxLevel() {
        return maxLevel;
    }



    public int getLevelStartAmount() {
        return levelStartAmount;
    }

    public int getHitXPAmount() {
        return hitXPAmount;
    }

    public int getHitXPChance() {
        return hitXPChance;
    }

    public int getArmorXPRNGModifier() {
        return armorXPRNGModifier;
    }

    public boolean hasType(LevelingType type) {
        for(var typeInItem : types) {
            if(typeInItem.getClass() == type.getClass()) return true;
        }
        return false;
    }

    public LevelingFunction getFunction() {
        return function;
    }

    public static LevelableItem fromJson(JsonObject object, ResourceLocation resourceLocation) {
        Item item = BuiltInRegistries.ITEM.get(resourceLocation);


        //Attributes
        List<LevelableAttribute> attributes = new ArrayList<>();
        if(object.has("attributes") && object.get("attributes").isJsonArray()) {
            JsonArray array = object.getAsJsonArray("attributes");
            for(JsonElement element : array) {
                if(element.isJsonObject()) {
                    LevelableAttribute check = LevelableAttribute.fromJSON(element.getAsJsonObject());
                    if(check != null) attributes.add(check);
                }
            }
        }

        //Types
        List<LevelingType> types = new ArrayList<>();
        if(object.has("leveling_types") || object.has("leveling_type"))
        {
            types = LevelingTypeRegistry.fromJson(object.has("leveling_types") ?  object.get("leveling_types") : object.get("leveling_type"));
        }

        //Function
        LevelingFunction levelingFunction = LevelingFunctions.LINEAR;
        if(object.has("leveling_function")) {
            levelingFunction = LevelingFunctionRegistry.fromJson(object.get("leveling_function"));
        }

        //Level
        int maxLevel = WeaponLevelingConfig.max_item_level;
        if (object.has("maxLevel")) {
            maxLevel = Math.max(0,Math.min(object.get("maxLevel").getAsInt(),1000));
        }


        int levelStartAmount = WeaponLevelingConfig.starting_xp_amount;
        if (object.has("levelStartAmount")) {
            levelStartAmount = Math.max(0,Math.min(object.get("levelStartAmount").getAsInt(),10000000));
        }

        int hitXPAmount = WeaponLevelingConfig.hit_xp_amount;
        if (object.has("hitXPAmount")) {
            hitXPAmount = Math.max(0,Math.min(object.get("hitXPAmount").getAsInt(),10000000));
        }

        int hitXPChance = WeaponLevelingConfig.hit_xp_chance;
        if (object.has("hitXPChance")) {
            hitXPChance = Math.max(0,Math.min(object.get("hitXPChance").getAsInt(),100));
        }


        int XPApplyChance = WeaponLevelingConfig.xp_apply_chance;
        if (object.has("armorXPRNGModifier")) {
            XPApplyChance = Math.max(0,Math.min(object.get("armorXPRNGModifier").getAsInt(),100));
        }


        if(attributes.isEmpty() || types.isEmpty()) {
            return null;
        }

        return new LevelableItem(item,attributes, types,levelingFunction, maxLevel, levelStartAmount, hitXPAmount, hitXPChance, XPApplyChance);
    }

    public static LevelableItem fromNBT(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag == null) return null;
        if(!tag.contains("levelable")) return null;
        CompoundTag levelableTag = tag.getCompound("levelable");
        if(levelableTag.contains("disabled") && levelableTag.getBoolean("disabled")) return null;
        Item item = stack.getItem();
        LevelableItem jsonItem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(item));

        //Attributes
        List<LevelableAttribute> attributes = new ArrayList<>();
        if(levelableTag.contains("attributes"))
        {
            for(var element : levelableTag.getList("attributes", Tag.TAG_COMPOUND)) {
                if(element instanceof CompoundTag compoundTag) {
                    LevelableAttribute levelableAttribute = LevelableAttribute.fromNBT(compoundTag);
                    if(levelableAttribute != null) attributes.add(levelableAttribute);
                }
            }
        }
        if(attributes.isEmpty() && jsonItem != null) {
            attributes.addAll(jsonItem.getAttributes());
        }

        //Types
        List<LevelingType> types =  new ArrayList<>();
        if(levelableTag.contains("leveling_type"))
        {
            LevelingType levelingType = LevelingTypeRegistry.fromNBT(levelableTag.getCompound("leveling_type"));
            if(levelingType != null) types.add(levelingType);
        } else if(levelableTag.contains("leveling_types"))
        {
            for(Tag element : levelableTag.getList("leveling_types", Tag.TAG_COMPOUND)) {
                if(element instanceof CompoundTag leveling_type && leveling_type.contains("type")) {
                    LevelingType levelingType = LevelingTypeRegistry.fromNBT(leveling_type);
                    if(levelingType != null) types.add(levelingType);
                }
            }
        }
        if(types.isEmpty() && jsonItem != null) {
            types.addAll(jsonItem.getTypes());
        }

        //Function
        LevelingFunction levelingFunction = jsonItem != null ? jsonItem.getFunction() : LevelingFunctions.LINEAR;
        if(levelableTag.contains("leveling_function")) {
            levelingFunction = LevelingFunctionRegistry.fromNBT(levelableTag.getCompound("leveling_function"));
        }

        //Level
        int maxLevel = jsonItem != null ? jsonItem.getMaxLevel() : WeaponLevelingConfig.max_item_level;
        if (levelableTag.contains("maxLevel")) {
            maxLevel = Math.max(0,Math.min(levelableTag.getInt("maxLevel"),1000));
        }


        int levelStartAmount = jsonItem != null ? jsonItem.getLevelStartAmount() : WeaponLevelingConfig.starting_xp_amount;
        if (levelableTag.contains("levelStartAmount")) {
            levelStartAmount = Math.max(0,Math.min(levelableTag.getInt("levelStartAmount"),10000000));
        }

        int hitXPAmount = jsonItem != null ? jsonItem.getHitXPAmount() : WeaponLevelingConfig.hit_xp_amount;
        if (levelableTag.contains("hitXPAmount")) {
            hitXPAmount = Math.max(0,Math.min(levelableTag.getInt("hitXPAmount"),10000000));
        }

        int hitXPChance = jsonItem != null ? jsonItem.getHitXPChance() : WeaponLevelingConfig.hit_xp_chance;
        if (levelableTag.contains("hitXPChance")) {
            hitXPChance = Math.max(0,Math.min(levelableTag.getInt("hitXPChance"),100));
        }


        int XPApplyChance = jsonItem != null ? jsonItem.getArmorXPRNGModifier() : WeaponLevelingConfig.xp_apply_chance;
        if (levelableTag.contains("armorXPRNGModifier")) {
            XPApplyChance = Math.max(0,Math.min(levelableTag.getInt("armorXPRNGModifier"),100));
        }


        if(attributes.isEmpty() || types.isEmpty()) {
            return null;
        }


        return new LevelableItem(item,attributes, types,levelingFunction, maxLevel, levelStartAmount, hitXPAmount, hitXPChance, XPApplyChance);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation( BuiltInRegistries.ITEM.getKey(this.item));

        buf.writeVarInt(this.attributes.size());
        for (LevelableAttribute attr : this.attributes) {
            attr.write(buf);
        }
        buf.writeVarInt(this.types.size());
        for (LevelingType type : this.types) {
            ResourceLocation id = LevelingTypeRegistry.getID(type);
            buf.writeResourceLocation(id);
            type.write(buf);
        }
        ResourceLocation functionID = LevelingFunctionRegistry.getID(function);
        buf.writeResourceLocation(functionID);
        function.write(buf);

        buf.writeVarInt(this.maxLevel);
        buf.writeVarInt(this.levelStartAmount);
        buf.writeVarInt(this.hitXPAmount);
        buf.writeVarInt(this.hitXPChance);
        buf.writeVarInt(this.armorXPRNGModifier);
    }

    public static LevelableItem read(FriendlyByteBuf buf) {
        Item item = BuiltInRegistries.ITEM.get(buf.readResourceLocation());
        int attrCount = buf.readVarInt();
        List<LevelableAttribute> attributes = new ArrayList<>();
        for (int i = 0; i < attrCount; i++) {
            attributes.add(LevelableAttribute.read(buf));
        }
        int typeCount = buf.readVarInt();
        List<LevelingType> types = new ArrayList<>();
        for (int i = 0; i < typeCount; i++) {
            LevelingType type = LevelingTypeRegistry.getByID(buf.readResourceLocation());
            if(type != null) {
                type.read(buf);
                types.add(type);
            }

        }

        LevelingFunction levelingFunction = LevelingFunctionRegistry.getByID(buf.readResourceLocation());
        if(levelingFunction != null) {
            levelingFunction.read(buf);
        }


        int maxLevel = buf.readVarInt();
        int levelStartAmount = buf.readVarInt();
        int hitXPAmount = buf.readVarInt();
        int hitXPChance = buf.readVarInt();
        int critXPAmount = buf.readVarInt();
        return new LevelableItem(item, attributes, types, levelingFunction, maxLevel, levelStartAmount, hitXPAmount, hitXPChance, critXPAmount);
    }
}
