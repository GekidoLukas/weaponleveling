package net.weaponleveling.data.levelable_item;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.weaponleveling.WeaponLevelingConfig;

public class LevelableAttribute {


    private final double valuePerLevel;
    private final Attribute attribute;
    private final LevelingType levelingType;




    public LevelableAttribute(double valuePerLevel, Attribute attribute, LevelingType levelingType) {
        this.valuePerLevel = valuePerLevel;
        this.attribute = attribute;
        this.levelingType = levelingType;
    }


    public static LevelableAttribute fromJSON(JsonObject object) {
        double valuePerLevel = object.has("valuePerLevel") ? object.get("valuePerLevel").getAsDouble() : WeaponLevelingConfig.value_per_level;
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(new ResourceLocation(object.get("attribute").getAsString()));

        LevelingType levelingType = LevelingType.fromString(object.get("levelingType").getAsString());

        return attribute != null ? new LevelableAttribute(valuePerLevel,attribute, levelingType) : null;
    }

    public double getValuePerLevel() {
        return valuePerLevel;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public LevelingType getLevelingType() {
        return levelingType;
    }


    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.valuePerLevel);
        buf.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(this.attribute));
        buf.writeEnum(this.levelingType);
    }

    public static LevelableAttribute read(FriendlyByteBuf buf) {
        double valuePerLevel = buf.readDouble();
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(buf.readResourceLocation());
        LevelingType levelingType = buf.readEnum(LevelingType.class);
        return new LevelableAttribute(valuePerLevel, attribute, levelingType);
    }
}
