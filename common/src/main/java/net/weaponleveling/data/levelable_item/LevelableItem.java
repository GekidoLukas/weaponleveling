package net.weaponleveling.data.levelable_item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.api.registry.LevelingFunctionRegistry;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.api.registry.LevelingTypeRegistry;
import net.weaponleveling.item.component.WLDataComponents;

import java.util.ArrayList;
import java.util.List;


public record LevelableItem(Item item, List<LevelableAttribute> attributes, List<LevelingType> types,
                            LevelingFunction function, int maxLevel, int levelStartAmount, int hitXPAmount,
                            int hitXPChance, int wornMinXPPercentage) {


    public static final StreamCodec<RegistryFriendlyByteBuf, LevelableItem> STREAM_CODEC = StreamCodec.of(
        // Encoder
        (buf, item) -> {
            ByteBufCodecs.registry(Registries.ITEM).encode(buf, item.item);
            LevelableAttribute.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, item.attributes);
            LevelingType.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, item.types);
            LevelingFunction.STREAM_CODEC.encode(buf, item.function);

            ByteBufCodecs.VAR_INT.encode(buf, item.maxLevel);
            ByteBufCodecs.VAR_INT.encode(buf, item.levelStartAmount);
            ByteBufCodecs.VAR_INT.encode(buf, item.hitXPAmount);
            ByteBufCodecs.VAR_INT.encode(buf, item.hitXPChance);
            ByteBufCodecs.VAR_INT.encode(buf, item.wornMinXPPercentage);
        },
        // Decoder
        buf -> new LevelableItem(
                ByteBufCodecs.registry(Registries.ITEM).decode(buf),
                LevelableAttribute.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf),
                LevelingType.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf),
                LevelingFunction.STREAM_CODEC.decode(buf),
                ByteBufCodecs.VAR_INT.decode(buf),
                ByteBufCodecs.VAR_INT.decode(buf),
                ByteBufCodecs.VAR_INT.decode(buf),
                ByteBufCodecs.VAR_INT.decode(buf),
                ByteBufCodecs.VAR_INT.decode(buf)
        )
);

    public static final Codec<LevelableItem> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("item", Items.AIR).forGetter(LevelableItem::item),
                    LevelableAttribute.CODEC.listOf().fieldOf("attributes").forGetter(LevelableItem::attributes),
                    LevelingType.CODEC.listOf().fieldOf("leveling_types").forGetter(LevelableItem::types),
                    LevelingFunction.CODEC.optionalFieldOf("leveling_function", LevelingFunctions.LINEAR).forGetter(LevelableItem::function),
                    Codec.INT.optionalFieldOf("max_level", WeaponLevelingConfig.max_item_level).forGetter(LevelableItem::maxLevel),
                    Codec.INT.optionalFieldOf("level_start_amount", WeaponLevelingConfig.starting_xp_amount).forGetter(LevelableItem::levelStartAmount),
                    Codec.INT.optionalFieldOf("hit_xp_amount", WeaponLevelingConfig.hit_xp_amount).forGetter(LevelableItem::hitXPAmount),
                    Codec.INT.optionalFieldOf("hit_xp_chance", WeaponLevelingConfig.hit_xp_chance).forGetter(LevelableItem::hitXPChance),
                    Codec.INT.optionalFieldOf("worn_min_xp_percentage", WeaponLevelingConfig.xp_apply_chance).forGetter(LevelableItem::wornMinXPPercentage)
            ).apply(instance, LevelableItem::new)
    );

    public boolean hasType(LevelingType type) {
        for (var typeInItem : types) {
            if (typeInItem.getClass() == type.getClass()) return true;
        }
        return false;
    }

    public static LevelableItem fromJson(JsonObject object, ResourceLocation resourceLocation, HolderLookup.Provider registries) {
        Item item = BuiltInRegistries.ITEM.get(resourceLocation);


        //Attributes
        List<LevelableAttribute> attributes = new ArrayList<>();
        if (object.has("attributes") && object.get("attributes").isJsonArray()) {
            JsonArray array = object.getAsJsonArray("attributes");
            for (JsonElement element : array) {
                if (element.isJsonObject()) {
                    LevelableAttribute check = LevelableAttribute.fromJSON(element.getAsJsonObject(), registries);
                    if (check != null) attributes.add(check);
                }
            }
        }

        //Types
        List<LevelingType> types = new ArrayList<>();
        if (object.has("leveling_types") || object.has("leveling_type")) {
            types = LevelingTypeRegistry.fromJson(object.has("leveling_types") ? object.get("leveling_types") : object.get("leveling_type"));
        }

        //Function
        LevelingFunction levelingFunction = LevelingFunctions.LINEAR;
        if (object.has("leveling_function")) {
            levelingFunction = LevelingFunctionRegistry.fromJson(object.get("leveling_function"));
        }

        //Level
        int maxLevel = WeaponLevelingConfig.max_item_level;
        if (object.has("max_level") || object.has("maxLevel")) {
            int input = object.has("max_level") ? object.get("max_level").getAsInt() :object.get("maxLevel").getAsInt();
            maxLevel = Math.clamp(input, 0, 1000);
        }


        int levelStartAmount = WeaponLevelingConfig.starting_xp_amount;
        if (object.has("level_start_amount") || object.has("levelStartAmount")) {
            int input = object.has("level_start_amount") ? object.get("level_start_amount").getAsInt() :object.get("levelStartAmount").getAsInt();
            levelStartAmount = Math.clamp(input, 0,  10000000);
        }

        int hitXPAmount = WeaponLevelingConfig.hit_xp_amount;
        if (object.has("hit_xp_amount") || object.has("hitXPAmount")) {
            int input = object.has("hit_xp_amount") ? object.get("hit_xp_amount").getAsInt() :object.get("hitXPAmount").getAsInt();
            hitXPAmount = Math.clamp(input, 0, 10000000);
        }

        int hitXPChance = WeaponLevelingConfig.hit_xp_chance;
        if (object.has("hit_xp_chance") || object.has("hitXPChance")) {
            int input = object.has("hit_xp_chance") ? object.get("hit_xp_chance").getAsInt() :object.get("hitXPChance").getAsInt();
            hitXPChance = Math.clamp(input, 0, 100);
        }


        int wornMinXPPercentage = WeaponLevelingConfig.xp_apply_chance;
        if (object.has("worn_min_xp_percentage") || object.has("armorXPRNGModifier")) {
            int input = object.has("worn_min_xp_percentage") ? object.get("worn_min_xp_percentage").getAsInt() :object.get("armorXPRNGModifier").getAsInt();
            wornMinXPPercentage = Math.clamp(input, 0, 100);
        }


        if (attributes.isEmpty() || types.isEmpty()) {
            return null;
        }

        return new LevelableItem(item, attributes, types, levelingFunction, maxLevel, levelStartAmount, hitXPAmount, hitXPChance, wornMinXPPercentage);
    }

    public static LevelableItem fromNBT(ItemStack stack) {
        if (stack.has(WLDataComponents.ITEM_LEVELABLE_DATA.get())) {
            return stack.get(WLDataComponents.ITEM_LEVELABLE_DATA.get());
        }
        return null;
    }
}
