package net.weaponleveling.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingMod;

import java.util.HashMap;
import java.util.Map;

public class LevelableItemsLoader extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final String directory = "levelable_items";

    public static final LevelableItemsLoader INSTANCE = new LevelableItemsLoader();
    public static Map<ResourceLocation, JsonElement> MAP = new HashMap<>();

    public LevelableItemsLoader() {
        super(GSON, directory);
    }



    public void setMap(Map<ResourceLocation, LevelableItem> newmap) {
        itemmap = newmap;
    }



    private static Map<ResourceLocation, LevelableItem> itemmap = ImmutableMap.of();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        MAP = jsonMap;
    }

    public static void applyNew(Map<ResourceLocation, JsonElement> jsonMap) {



        ImmutableMap.Builder<ResourceLocation, LevelableItem> builder = ImmutableMap.builder();
        jsonMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject jsonElementAsJsonObject = jsonElement.getAsJsonObject();

            if(jsonElementAsJsonObject.has("taglist") && jsonElementAsJsonObject.get("taglist").getAsBoolean()) {

                try {

                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String keyString = resourceLocation.getPath();

                    if(WLPlatformGetter.sendRegistryInLog()) WeaponLevelingMod.LOGGER.info("Registering Tag: #" + resourceLocation);


                    TagKey<Item> key = TagKey.create(Registry.ITEM_REGISTRY, resourceLocation);

                    if (Registry.ITEM.getTag(key).isPresent()) {
                        if(WLPlatformGetter.sendRegistryInLog()) WeaponLevelingMod.LOGGER.info("Tagkey exists: #" + resourceLocation.toString() );

                        Registry.ITEM.getTag(key).get().forEach((itemHolder) -> {
                            Item item = itemHolder.value();
                            if(WLPlatformGetter.sendRegistryInLog()) WeaponLevelingMod.LOGGER.info("#" + resourceLocation + " contains " + Registry.ITEM.getKey(item));

                            LevelableItem levelableItem = LevelableItem.fromJson(jsonObject, Registry.ITEM.getKey(item));
                            builder.put(Registry.ITEM.getKey(item), levelableItem);
                        });


                    } else WeaponLevelingMod.LOGGER.error("{} is not a valid Item Tag", () -> resourceLocation);

                } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                    WeaponLevelingMod.LOGGER.error("Parsing error loading Item Levels {}: {}", resourceLocation, jsonparseexception.getMessage());
                }
            } else {
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if(WLPlatformGetter.sendRegistryInLog()) WeaponLevelingMod.LOGGER.info("Registering: " + resourceLocation);

                    if (Registry.ITEM.containsKey(resourceLocation)) {

                        LevelableItem levelableItem = LevelableItem.fromJson(jsonObject, resourceLocation);
                        builder.put(resourceLocation, levelableItem);


                    } else WeaponLevelingMod.LOGGER.error("{} is not a valid Item", () -> resourceLocation);

                } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                    WeaponLevelingMod.LOGGER.error("Parsing error loading Item Levels {}: {}", resourceLocation, jsonparseexception.getMessage());
                }
            }

        });


        Map<ResourceLocation, LevelableItem> map = builder.build();

        itemmap = map;
    }


    public static LevelableItem get(ResourceLocation resourceLocation) {
        return itemmap.get(resourceLocation);
    }

    public static boolean isValid(Item item) {
        return get(Registry.ITEM.getKey(item)) != null;
    }
}
