package net.weaponleveling.fabric;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.fabricmc.api.ModInitializer;
import net.weaponleveling.data.LevelableItem;
import net.weaponleveling.data.LevelableItemsLoader;
import net.weaponleveling.fabric.config.WeaponLevelingConfigFabric;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class WeaponLevelingModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ModLoadingContext.registerConfig(WeaponLevelingMod.MODID, ModConfig.Type.SERVER, WeaponLevelingConfigFabric.Server.SPEC, "weaponleveling-server.toml");
        ModLoadingContext.registerConfig(WeaponLevelingMod.MODID, ModConfig.Type.CLIENT, WeaponLevelingConfigFabric.Client.SPEC, "weaponleveling-client.toml");
        WeaponLevelingMod.init();
        registerEvents();
    }



    private void registerEvents() {

        CommonLifecycleEvents.TAGS_LOADED.register((phase, listener)-> {
            WeaponLevelingMod.LOGGER.warn("Reloaded Tags here");
            Map<ResourceLocation, JsonElement> jsonMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(jsonMap);
        });
    }



}
