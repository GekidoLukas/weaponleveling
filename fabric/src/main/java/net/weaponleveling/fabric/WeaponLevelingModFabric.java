package net.weaponleveling.fabric;


import com.google.gson.JsonElement;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.weaponleveling.WLConfigReader;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.mob_xp.MobXPLoader;

import java.util.Map;

public class WeaponLevelingModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WeaponLevelingMod.init();
        registerEvents();
    }



    private void registerEvents() {

        CommonLifecycleEvents.TAGS_LOADED.register((phase, listener) -> {
            if(Platform.getEnv() == EnvType.SERVER) {
                Map<ResourceLocation, JsonElement> itemMap = LevelableItemsLoader.MAP;
                LevelableItemsLoader.applyNew(itemMap);
                Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
                MobXPLoader.applyNew(mobMap);
            } else {
                localServerLoad();
            }
        });


        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(((player, joined) -> {
            if(Platform.getEnv() == EnvType.SERVER) {
                if(player.server.isDedicatedServer()) {
                    WLConfigReader.sync(player);
                    LevelableItemsLoader.sync(player);
                }
            }
        }));
    }

    @Environment(EnvType.CLIENT)
    public static void localServerLoad() {
        if(Minecraft.getInstance().isLocalServer()) {
            Map<ResourceLocation, JsonElement> itemMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(itemMap);
            Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
            MobXPLoader.applyNew(mobMap);
        }
    }



}
