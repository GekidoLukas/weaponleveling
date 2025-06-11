package net.weaponleveling.data.levelable_item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.data.levelable_item.type.LevelingType;

public class LevelableAttribute {


    private final double valuePerLevel;
    private final Attribute attribute;




    public LevelableAttribute(double valuePerLevel, Attribute attribute) {
        this.valuePerLevel = valuePerLevel;
        this.attribute = attribute;
    }


    public static LevelableAttribute fromJSON(JsonObject object) {
        double valuePerLevel = object.has("valuePerLevel") ? object.get("valuePerLevel").getAsDouble() : WeaponLevelingConfig.value_per_level;
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(new ResourceLocation(object.get("attribute").getAsString()));


        return attribute != null ? new LevelableAttribute(valuePerLevel,attribute) : null;
    }
    public static LevelableAttribute fromNBT(CompoundTag tag) {
        double valuePerLevel = tag.contains("valuePerLevel") ? tag.getDouble("valuePerLevel") : WeaponLevelingConfig.value_per_level;
        Attribute attribute = null;
        ResourceLocation id = new ResourceLocation(tag.getString("attribute"));
        attribute =  BuiltInRegistries.ATTRIBUTE.get(id);


        return attribute != null ? new LevelableAttribute(valuePerLevel,attribute) : null;
    }

    public static boolean hasValidInNBT(ListTag tag) {
        int correctOnes = 0;
        for (var item : tag) {
            if(item instanceof CompoundTag compoundTag) {
                LevelableAttribute levelableAttribute = fromNBT(compoundTag);
                if(levelableAttribute != null) correctOnes++;
            }
        }
        return correctOnes > 0;
    }

    public double getValuePerLevel() {
        return valuePerLevel;
    }

    public Attribute getAttribute() {
        return attribute;
    }




    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.valuePerLevel);
        buf.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(this.attribute));
    }

    public static LevelableAttribute read(FriendlyByteBuf buf) {
        double valuePerLevel = buf.readDouble();
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(buf.readResourceLocation());
//        LevelingType levelingType = buf.readEnum(LevelingType.class);
        return new LevelableAttribute(valuePerLevel, attribute);
    }
}
