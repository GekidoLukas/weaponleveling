package net.weaponleveling.data.levelable_item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingConfig;

import java.util.ArrayList;
import java.util.List;

public class LevelableItem {

    private final Item item;
    private final List<LevelableAttribute> attributes;

    private final int maxLevel;
    private final int levelModifier;
    private final int levelStartAmount;

    private final int hitXPAmount;
    private final int hitXPChance;

    private final int XPRNGModifier;

    //private final String leveltype;


    public LevelableItem(Item item,List<LevelableAttribute> attributes, int maxLevel, int levelModifier, int levelStartAmount, int hitXPAmount, int hitXPChance, int critXPAmount) {
        this.item = item;
        this.attributes = attributes;

        this.maxLevel = maxLevel;
        this.levelModifier = levelModifier;
        this.levelStartAmount = levelStartAmount;

        this.hitXPAmount = hitXPAmount;
        this.hitXPChance = hitXPChance;


        this.XPRNGModifier = critXPAmount;
    }

    public List<LevelableAttribute> getAttributes() {
        return attributes;
    }

    public Item getItem() {
        return item;
    }


    public int getMaxLevel() {
        return maxLevel;
    }

    public int getLevelModifier() {
        return levelModifier;
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


        //Level
        int maxLevel = WeaponLevelingConfig.max_item_level;
        if (object.has("maxLevel")) {
            maxLevel = object.get("maxLevel").getAsInt();
        }

        int levelModifier = WeaponLevelingConfig.level_modifier;
        if (object.has("levelModifier")) {
            levelModifier = object.get("levelModifier").getAsInt();
        }

        int levelStartAmount = WeaponLevelingConfig.starting_xp_amount;
        if (object.has("levelStartAmount")) {
            levelStartAmount = object.get("levelStartAmount").getAsInt();
        }

        int hitXPAmount = WeaponLevelingConfig.hit_xp_amount;
        if (object.has("hitXPAmount")) {
            hitXPAmount = object.get("hitXPAmount").getAsInt();
        }

        int hitXPChance = WeaponLevelingConfig.hit_xp_chance;
        if (object.has("hitXPChance")) {
            hitXPChance = object.get("hitXPChance").getAsInt();
        }


        int XPApplyChance = WeaponLevelingConfig.xp_apply_chance;
        if (object.has("XPApplyChance")) {
            XPApplyChance = object.get("XPApplyChance").getAsInt();
        }


        if(attributes.isEmpty()) {
            return null;
        }

        return new LevelableItem(item,attributes, maxLevel, levelModifier, levelStartAmount, hitXPAmount, hitXPChance, XPApplyChance);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation( BuiltInRegistries.ITEM.getKey(this.item));

        buf.writeVarInt(this.attributes.size());
        for (LevelableAttribute attr : this.attributes) {
            attr.write(buf);
        }

        buf.writeVarInt(this.maxLevel);
        buf.writeVarInt(this.levelModifier);
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
        int maxLevel = buf.readVarInt();
        int levelModifier = buf.readVarInt();
        int levelStartAmount = buf.readVarInt();
        int hitXPAmount = buf.readVarInt();
        int hitXPChance = buf.readVarInt();
        int critXPAmount = buf.readVarInt();
        return new LevelableItem(item, attributes, maxLevel, levelModifier, levelStartAmount, hitXPAmount, hitXPChance, critXPAmount);
    }
}
