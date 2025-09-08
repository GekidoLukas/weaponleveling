package net.weaponleveling.data.levelable_item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.api.registry.LevelingFunctionRegistry;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.api.registry.LevelingTypeRegistry;

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

    private final int XPRNGModifier;

    //private final String leveltype;
    private final LevelingFunction function;


    public LevelableItem(Item item,List<LevelableAttribute> attributes, List<LevelingType> types,LevelingFunction function , int maxLevel, int levelStartAmount, int hitXPAmount, int hitXPChance, int critXPAmount) {
        this.item = item;
        this.attributes = attributes;
        this.types = types;
        this.function = function;

        this.maxLevel = maxLevel;
        this.levelStartAmount = levelStartAmount;

        this.hitXPAmount = hitXPAmount;
        this.hitXPChance = hitXPChance;


        this.XPRNGModifier = critXPAmount;
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

    public int getXPRNGModifier() {
        return XPRNGModifier;
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
        if (object.has("XPApplyChance")) {
            XPApplyChance = Math.max(0,Math.min(object.get("XPApplyChance").getAsInt(),100));
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
        buf.writeVarInt(this.XPRNGModifier);
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
        WeaponLevelingMod.LOGGER.info("TYPES: " + item + types.toString());

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
