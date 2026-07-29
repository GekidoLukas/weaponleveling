package net.weaponleveling.data.levelable_item;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;

public record LevelableAttribute(double valuePerLevel, Holder<Attribute> attribute, boolean isPercent) {


    public static final Codec<LevelableAttribute> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.optionalFieldOf("value_per_level", WeaponLevelingConfig.value_per_level).forGetter(LevelableAttribute::valuePerLevel),
                    Attribute.CODEC.fieldOf("attribute").forGetter(LevelableAttribute::attribute),
                    Codec.BOOL.optionalFieldOf("is_percent", false).forGetter(LevelableAttribute::isPercent)
            ).apply(instance, LevelableAttribute::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LevelableAttribute> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, LevelableAttribute::valuePerLevel,
            Attribute.STREAM_CODEC, LevelableAttribute::attribute,
            ByteBufCodecs.BOOL, LevelableAttribute::isPercent,
            LevelableAttribute::new
    );


    public static LevelableAttribute fromJSON(JsonObject object, HolderLookup.Provider registries) {
        double valuePerLevel = object.has("value_per_level") ? object.get("value_per_level").getAsDouble() : ( object.has("valuePerLevel") ? object.get("valuePerLevel").getAsDouble() : WeaponLevelingConfig.value_per_level);
        boolean isPercent = object.has("is_percent") ? object.get("is_percent").getAsBoolean() : (object.has("isPercent") ? object.get("isPercent").getAsBoolean() : false);
        String attributeName = object.get("attribute").getAsString();
        try {
            Holder<Attribute> attribute = registries.lookupOrThrow(Registries.ATTRIBUTE).get(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.parse(attributeName))).get();
            return new LevelableAttribute(valuePerLevel, attribute, isPercent);
        } catch (Exception e) {
            WeaponLevelingMod.LOGGER.error("Could not find an attribute with the name \"" + attributeName + "\"");
            return null;
        }
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeDouble(this.valuePerLevel);
        buf.writeBoolean(this.isPercent);
        ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE).encode(buf, this.attribute);
    }

    public static LevelableAttribute read(RegistryFriendlyByteBuf buf) {
        double valuePerLevel = buf.readDouble();
        boolean isPercent = buf.readBoolean();
        Holder<Attribute> attribute = ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE).decode(buf);
        return new LevelableAttribute(valuePerLevel, attribute, isPercent);
    }
}
