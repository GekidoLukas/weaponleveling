package net.weaponleveling.data.ranged_damage;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.attribute.IRangedWeapon;
import net.weaponleveling.data.levelable_item.LevelableItem;
import net.weaponleveling.data.mob_xp.MobXP;
import net.weaponleveling.item.ModItems;

import java.util.HashMap;
import java.util.Map;

public class RangedDamageLoader extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final String directory = "ranged_damage";

    public static final RangedDamageLoader INSTANCE = new RangedDamageLoader();
    public static Map<ResourceLocation, JsonElement> MAP = new HashMap<>();

    public RangedDamageLoader() {
        super(GSON, directory);
    }



    public void setMap(Map<ResourceLocation, RangedDamageEntry> newmap) {
        rangedDamageMap = newmap;
    }



    private static Map<ResourceLocation, RangedDamageEntry> rangedDamageMap = ImmutableMap.of();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        MAP = jsonMap;
    }

    public static void applyNew(Map<ResourceLocation, JsonElement> jsonMap) {


        WeaponLevelingMod.LOGGER.info("Starting Ranged Damage Registry!");
        Map<ResourceLocation, RangedDamageEntry> builder = new HashMap<>();

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
                                if(jsonElementAsJsonObject.has("excludeTag")) {
                                    String excludeNamespace = jsonElementAsJsonObject.get("excludeTag").getAsString().split(":")[0].replace("#","");
                                    String excludeName = jsonElementAsJsonObject.get("excludeTag").getAsString().split(":")[1];
                                    ResourceLocation excludeID = new ResourceLocation(excludeNamespace,excludeName);
                                    TagKey<Item> excludeTagKey = TagKey.create(Registries.ITEM, excludeID);
                                    if(BuiltInRegistries.ITEM.getTag(excludeTagKey).isPresent() && item.getDefaultInstance().is(excludeTagKey)) return;
                                }
                                if(item.equals(ModItems.BROKEN_ITEM.get())) return;
                                if(item instanceof IRangedWeapon) {
                                    if(WeaponLevelingConfig.send_registry_in_log) WeaponLevelingMod.LOGGER.info("#" + resourceLocation + " contains " + BuiltInRegistries.ITEM.getKey(item));

                                    RangedDamageEntry rangedDamageEntry = RangedDamageEntry.fromJson(jsonObject);
                                    if(rangedDamageEntry != null) {
                                        builder.remove(BuiltInRegistries.ITEM.getKey(item));
                                        builder.put(BuiltInRegistries.ITEM.getKey(item), rangedDamageEntry);
                                    }
                                } else {
                                    WeaponLevelingMod.LOGGER.error("{} is not a ranged weapon", () -> id);
                                }

                            });
                        }
                        else if(BuiltInRegistries.ITEM.containsKey(id)){
                            Item item = BuiltInRegistries.ITEM.get(id);
                            if(jsonElementAsJsonObject.has("excludeTag")) {
                                String excludeNamespace = jsonElementAsJsonObject.get("excludeTag").getAsString().split(":")[0].replace("#","");
                                String excludeName = jsonElementAsJsonObject.get("excludeTag").getAsString().split(":")[1];
                                ResourceLocation excludeID = new ResourceLocation(excludeNamespace,excludeName);
                                TagKey<Item> excludeTagKey = TagKey.create(Registries.ITEM, excludeID);
                                if(BuiltInRegistries.ITEM.getTag(excludeTagKey).isPresent() && item.getDefaultInstance().is(excludeTagKey)) return;
                            }
                            if(item.equals(ModItems.BROKEN_ITEM.get())) return;
                            if(item instanceof IRangedWeapon) {
                                if(WeaponLevelingConfig.send_registry_in_log) WeaponLevelingMod.LOGGER.info("Registering: " + id);

                                RangedDamageEntry rangedDamageEntry = RangedDamageEntry.fromJson(jsonObject);
                                if(rangedDamageEntry != null) {
                                    builder.remove(id);
                                    builder.put(id, rangedDamageEntry);
                                }
                            } else {
                                WeaponLevelingMod.LOGGER.error("{} is not a ranged weapon", () -> id);
                            }

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
                WeaponLevelingMod.LOGGER.error("Parsing error loading Ranged Damage Levels {}: {}", resourceLocation, jsonparseexception.getMessage());
            }
        });

        WeaponLevelingMod.LOGGER.info("Ranged Damage Registry has finished!");
        rangedDamageMap = builder;
    }


    public static RangedDamageEntry get(Item item) {
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(item);
        return rangedDamageMap.get(resourceLocation);
    }

    public static boolean isValid(Item item) {
        return get(item) != null;
    }
}
