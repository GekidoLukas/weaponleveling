package net.weaponleveling.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingMod;

import java.util.Map;

public class LevelableItemsLoader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public LevelableItemsLoader() {
        super(GSON, "levelable_items");
    }

    private static Map<ResourceLocation, LevelableItem> itemmap = ImmutableMap.of();
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        ImmutableMap.Builder<ResourceLocation, LevelableItem> builder = ImmutableMap.builder();
        jsonMap.forEach((resourceLocation, jsonElement) -> {
            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String keyString = resourceLocation.getPath();

                WeaponLevelingMod.LOGGER.info("Registering: " + resourceLocation);

                if(Registry.ITEM.containsKey(resourceLocation)) {

                    //if(jsonObject.has("leveltypes") || jsonObject.has("disabled")) {

                    LevelableItem levelableItem = LevelableItem.fromJson(jsonObject,resourceLocation);
                    builder.put(resourceLocation, levelableItem);
                    //}else WeaponLeveling.LOGGER.error("{} does not contain a valid object", resourceLocation);

                } else WeaponLevelingMod.LOGGER.error("{} is not a valid Item", () -> resourceLocation);

            }catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                WeaponLevelingMod.LOGGER.error("Parsing error loading Item Levels {}: {}", resourceLocation, jsonparseexception.getMessage());
            }});
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
