package net.weaponleveling.fabric;


import com.google.gson.JsonElement;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.weaponleveling.WLConfigReader;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.LevelableItemsLoader;

import java.util.Map;

public class WeaponLevelingModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
//        ForgeConfigRegistry.INSTANCE.register(WeaponLevelingMod.MODID, ModConfig.Type.SERVER, WeaponLevelingConfigFabric.Server.SPEC, "weaponleveling-server.toml");
//        ForgeConfigRegistry.INSTANCE.register(WeaponLevelingMod.MODID, ModConfig.Type.CLIENT, WeaponLevelingConfigFabric.Client.SPEC, "weaponleveling-client.toml");
        WeaponLevelingMod.init();
        registerEvents();
    }



    private void registerEvents() {

        CommonLifecycleEvents.TAGS_LOADED.register((phase, listener) -> {
            Map<ResourceLocation, JsonElement> jsonMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(jsonMap);
        });

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(((player, joined) -> {
            WLConfigReader.sync(player);
        }));
    }



}
