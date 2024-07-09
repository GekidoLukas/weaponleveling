package net.weaponleveling.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.util.ModUtils;

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


        WeaponLevelingMod.LOGGER.info("Starting Levelable Registry!");
//        ImmutableMap.Builder<ResourceLocation, LevelableItem> builder = ImmutableMap.builder();
        Map<ResourceLocation, LevelableItem> builder = new HashMap<>();

        jsonMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject jsonElementAsJsonObject = jsonElement.getAsJsonObject();


            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if(jsonElementAsJsonObject.has("item")) {
                    boolean hasHasTag = jsonElementAsJsonObject.get("item").getAsString().startsWith("#");
                    if(jsonElementAsJsonObject.get("item").getAsString().contains(":")) {
                        String namespace = jsonElementAsJsonObject.get("item").getAsString().split(":")[0].replace("#","");
                        String name = jsonElementAsJsonObject.get("item").getAsString().split(":")[1];
                        ResourceLocation id = new ResourceLocation(namespace,name);
                        TagKey<Item> itemTagKey = TagKey.create(Registries.ITEM, id);

                        if(hasHasTag && BuiltInRegistries.ITEM.getTag(itemTagKey).isPresent()) {

                            if(WeaponLevelingConfig.send_registry_in_log) WeaponLevelingMod.LOGGER.info("Tagkey exists: #" + resourceLocation.toString() );

                            BuiltInRegistries.ITEM.getTag(itemTagKey).get().forEach((itemHolder) -> {
                                Item item = itemHolder.value();
                                if(WeaponLevelingConfig.send_registry_in_log) WeaponLevelingMod.LOGGER.info("#" + resourceLocation + " contains " + BuiltInRegistries.ITEM.getKey(item));

                                LevelableItem levelableItem = LevelableItem.fromJson(jsonObject, BuiltInRegistries.ITEM.getKey(item));
                                builder.put(BuiltInRegistries.ITEM.getKey(item), levelableItem);
                            });
                        }
                        else if(BuiltInRegistries.ITEM.containsKey(id)){
                            if(WeaponLevelingConfig.send_registry_in_log) WeaponLevelingMod.LOGGER.info("Registering: " + id);

                            LevelableItem levelableItem = LevelableItem.fromJson(jsonObject, id);
                            builder.remove(id);
                            builder.put(id, levelableItem);
                        }
                        else {
                            WeaponLevelingMod.LOGGER.error("{} is not a valid Item or Item Tag", () -> id);
                        }
                    } else {
                        WeaponLevelingMod.LOGGER.error("{} does not contain a VALID \"item\" field", () -> resourceLocation);
                    }

                } else {
                    WeaponLevelingMod.LOGGER.error("{} Does not contain the field \"item\"", () -> resourceLocation);
                }


            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                WeaponLevelingMod.LOGGER.error("Parsing error loading Item Levels {}: {}", resourceLocation, jsonparseexception.getMessage());
            }
        });

        WeaponLevelingMod.LOGGER.info("Levelable Registry has finished!");
        itemmap = builder;
    }


    public static LevelableItem get(ResourceLocation resourceLocation) {
        return itemmap.get(resourceLocation);
    }

    public static boolean isValid(Item item) {
        return get(BuiltInRegistries.ITEM.getKey(item)) != null;
    }
}
