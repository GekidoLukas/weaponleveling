package net.weaponleveling.fabric;


import com.google.gson.JsonElement;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.weaponleveling.WLConfigReader;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import net.weaponleveling.data.ranged_damage.RangedDamageLoader;

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
                LevelableItemsLoader.applyNew(itemMap,phase);
                Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
                MobXPLoader.applyNew(mobMap);
                Map<ResourceLocation, JsonElement> rangedDamageMap = RangedDamageLoader.MAP;
                RangedDamageLoader.applyNew(rangedDamageMap);
            } else {
                localServerLoad(phase);
            }
        });


        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(((player, joined) -> {
            if(player.server.isDedicatedServer()) {
                WLConfigReader.sync(player);
                LevelableItemsLoader.sync(player);
                RangedDamageLoader.sync(player);
            } else if(player.server.isSingleplayer() && !player.server.isSingleplayerOwner(player.getGameProfile())) {
                WLConfigReader.sync(player);
                LevelableItemsLoader.sync(player);
                RangedDamageLoader.sync(player);
            }
        }));

        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
            boolean serverOption = WeaponLevelingConfig.apply_ranged_damage_attribute;

            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(serverOption);
            sender.sendPacket(WLConfigReader.CONFIG_CHANNEL, buf);
        });



        ServerLoginNetworking.registerGlobalReceiver(WLConfigReader.CONFIG_CHANNEL, (server, listener, understood, buf, synchronizer, sender) -> {
            if (!understood) {
                listener.disconnect(Component.literal("Config check failed (no response)"));
                return;
            }

            boolean clientOption = buf.readBoolean();
            boolean serverOption = WeaponLevelingConfig.apply_ranged_damage_attribute;

            if (clientOption != serverOption) {
                listener.disconnect(Component.empty().withStyle(ChatFormatting.RED)
                        .append(Component.literal("Config mismatch!\n Config Option \""))
                        .append(Component.literal("apply_ranged_damage_attribute").withStyle(ChatFormatting.GOLD))
                        .append(Component.literal("\" is set to \""))
                        .append(Component.literal(""+ serverOption).withStyle(ChatFormatting.GOLD))
                        .append(Component.literal("\" on the Server and set to \""))
                        .append(Component.literal(""+ clientOption).withStyle(ChatFormatting.GOLD))
                        .append(Component.literal("\" on the Client."))
                );
            }
        });

    }

    @Environment(EnvType.CLIENT)
    public static void localServerLoad(HolderLookup.Provider registries) {
        if(Minecraft.getInstance().isLocalServer()) {
            Map<ResourceLocation, JsonElement> itemMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(itemMap,registries);
            Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
            MobXPLoader.applyNew(mobMap);
            Map<ResourceLocation, JsonElement> rangedDamageMap = RangedDamageLoader.MAP;
            RangedDamageLoader.applyNew(rangedDamageMap);
        }
    }



}
