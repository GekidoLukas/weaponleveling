package net.weaponleveling.fabric;


import com.google.gson.JsonElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.LevelableItemsLoader;
import net.weaponleveling.fabric.config.WeaponLevelingConfigFabric;

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
            Map<ResourceLocation, JsonElement> jsonMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(jsonMap);
        });
    }



}
