package net.weaponleveling.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingMod;

import java.util.HashMap;
import java.util.Map;

public class LevelableItemsLoader extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final String directory = "levelable_items";

    public static final LevelableItemsLoader INSTANCE = new LevelableItemsLoader();
    public static Map<ResourceLocation, JsonElement> MAP = new HashMap<>();

    public LevelableItemsLoader() {
        super(GSON, "levelable_items");
    }



    public void setMap(Map<ResourceLocation, LevelableItem> newmap) {
        itemmap = newmap;
    }



    private static Map<ResourceLocation, LevelableItem> itemmap = ImmutableMap.of();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        MAP = jsonMap;
        //applyNew(jsonMap);
    }

    public static void applyNew(Map<ResourceLocation, JsonElement> jsonMap) {



        ImmutableMap.Builder<ResourceLocation, LevelableItem> builder = ImmutableMap.builder();
        jsonMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject jsonElementAsJsonObject = jsonElement.getAsJsonObject();

            if(jsonElementAsJsonObject.has("taglist") && jsonElementAsJsonObject.get("taglist").getAsBoolean()) {
                WeaponLevelingMod.LOGGER.info("Yooo");
                try {

                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String keyString = resourceLocation.getPath();

                    WeaponLevelingMod.LOGGER.info("Registering Tag: #" + resourceLocation);

                    //Registry.ITEM_REGISTRY.



                    //WeaponLevelingMod.LOGGER.info("This: " + tagKey.location().toString());
                    TagKey<Item> key = TagKey.create(Registry.ITEM_REGISTRY, resourceLocation);

                    if (Registry.ITEM.getTag(key).isPresent()) {
                        WeaponLevelingMod.LOGGER.info("Tagkey exists: #" + resourceLocation.toString() );
                        if(Registry.ITEM.getTag(key).isPresent()) WeaponLevelingMod.LOGGER.info(Registry.ITEM.getTag(key).get());
                        //if(jsonObject.has("leveltypes") || jsonObject.has("disabled")) {
                        Registry.ITEM.getTag(key).get().forEach((itemHolder) -> {
                            Item item = itemHolder.value();
                            WeaponLevelingMod.LOGGER.info("Item is here: " + Registry.ITEM.getKey(item) + " and Location" + resourceLocation);

                            LevelableItem levelableItem = LevelableItem.fromJson(jsonObject, Registry.ITEM.getKey(item));
                            builder.put(Registry.ITEM.getKey(item), levelableItem);
                        });

                        //LevelableItem levelableItem = LevelableItem.fromJson(jsonObject, resourceLocation);
                        //builder.put(resourceLocation, levelableItem);
                        //}else WeaponLeveling.LOGGER.error("{} does not contain a valid object", resourceLocation);

                    } else WeaponLevelingMod.LOGGER.error("{} is not a valid Item Tag", () -> resourceLocation);

                } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                    WeaponLevelingMod.LOGGER.error("Parsing error loading Item Levels {}: {}", resourceLocation, jsonparseexception.getMessage());
                }
            } else {
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String keyString = resourceLocation.getPath();

                    WeaponLevelingMod.LOGGER.info("Registering: " + resourceLocation);

                    if (Registry.ITEM.containsKey(resourceLocation)) {


                        //if(jsonObject.has("leveltypes") || jsonObject.has("disabled")) {

                        LevelableItem levelableItem = LevelableItem.fromJson(jsonObject, resourceLocation);
                        builder.put(resourceLocation, levelableItem);
                        //}else WeaponLeveling.LOGGER.error("{} does not contain a valid object", resourceLocation);

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
