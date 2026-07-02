package net.weaponleveling.data.mob_xp;

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
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;

import java.util.HashMap;
import java.util.Map;

public class MobXPLoader extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final String directory = "mob_xp";

    public static final MobXPLoader INSTANCE = new MobXPLoader();
    public static Map<ResourceLocation, JsonElement> MAP = new HashMap<>();

    public MobXPLoader() {
        super(GSON, directory);
    }



    public void setMap(Map<ResourceLocation, MobXP> newmap) {
        mobMap = newmap;
    }



    private static Map<ResourceLocation, MobXP> mobMap = ImmutableMap.of();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        MAP = jsonMap;
    }

    public static void applyNew(Map<ResourceLocation, JsonElement> jsonMap) {


        WeaponLevelingMod.LOGGER.info("Starting Mob Death Levels Registry!");
        Map<ResourceLocation, MobXP> builder = new HashMap<>();

        jsonMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject jsonElementAsJsonObject = jsonElement.getAsJsonObject();


            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if(jsonObject.has("entity_type")) {
                    boolean hasHasTag = jsonElementAsJsonObject.get("entity_type").getAsString().startsWith("#");
                    if(jsonElementAsJsonObject.get("entity_type").getAsString().contains(":")) {
                        String namespace = jsonElementAsJsonObject.get("entity_type").getAsString().split(":")[0].replace("#","");
                        String name = jsonElementAsJsonObject.get("entity_type").getAsString().split(":")[1];
                        ResourceLocation id = new ResourceLocation(namespace,name);
                        TagKey<EntityType<?>> entityTagKey = TagKey.create(Registries.ENTITY_TYPE, id);
                        if(hasHasTag && BuiltInRegistries.ENTITY_TYPE.getTag(entityTagKey).isPresent()) {
                            if(WeaponLevelingConfig.send_registry_in_log) WeaponLevelingMod.LOGGER.info("Tagkey exists: #" + resourceLocation.toString() );
                            BuiltInRegistries.ENTITY_TYPE.getTag(entityTagKey).get().forEach((itemHolder) -> {
                                EntityType<?> entity = itemHolder.value();
                                if(WeaponLevelingConfig.send_registry_in_log) WeaponLevelingMod.LOGGER.info("#" + resourceLocation + " contains " + BuiltInRegistries.ENTITY_TYPE.getKey(entity));
                                MobXP mobXP = MobXP.fromJson(jsonObject, BuiltInRegistries.ENTITY_TYPE.getKey(entity));
                                if(mobXP != null) {
                                    builder.remove(BuiltInRegistries.ENTITY_TYPE.getKey(entity));
                                    builder.put(BuiltInRegistries.ENTITY_TYPE.getKey(entity), mobXP);
                                }

                            });

                        } else {
                            if(WeaponLevelingConfig.send_registry_in_log) WeaponLevelingMod.LOGGER.info("Registering: " + id);
                            MobXP mobXP = MobXP.fromJson(jsonObject, id);
                            if(mobXP != null) {
                                builder.remove(id);
                                builder.put(id, mobXP);
                            }
                        }

                    } else {
                        WeaponLevelingMod.LOGGER.error("{} does not contain a VALID \"entity_type\" field", () -> resourceLocation);
                    }

                } else {
                    WeaponLevelingMod.LOGGER.error("{} Does not contain the field \"entity_type\"", () -> resourceLocation);
                }



            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                WeaponLevelingMod.LOGGER.error("Parsing error loading Mob Death Levels {}: {}", resourceLocation, jsonparseexception.getMessage());
            }
        });

        WeaponLevelingMod.LOGGER.info("Mob XP Registry has finished!");
        mobMap = builder;
    }


    public static MobXP get(ResourceLocation resourceLocation) {
        return mobMap.get(resourceLocation);
    }

    public static boolean isValid(EntityType<?> entity) {
        if(entity.is(TagKey.create(Registries.ENTITY_TYPE,WeaponLevelingMod.id("entities_blacklist")))) return false;
        return get(BuiltInRegistries.ENTITY_TYPE.getKey(entity)) != null;
    }
}
