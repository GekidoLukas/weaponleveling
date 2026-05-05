package net.weaponleveling.forge;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.PlayerNegotiationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.weaponleveling.WLConfigReader;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.WeaponLevelingModClient;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import net.weaponleveling.forge.networking.ConfigCheckS2CPacket;
import net.weaponleveling.forge.networking.ForgeNetworking;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
@Mod(WeaponLevelingMod.MODID)
public class WeaponLevelingModForge {
    @SuppressWarnings(("removal"))
    public WeaponLevelingModForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(WeaponLevelingMod.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        WeaponLevelingMod.init();
        if(Platform.getEnv() == Dist.CLIENT) {
            WeaponLevelingModClient.init();
        }
    }
    private void setup(final FMLCommonSetupEvent event) {
        ForgeNetworking.register();

        PlayerEvent.PLAYER_JOIN.register((player -> {
            boolean serverOption = WeaponLevelingConfig.apply_ranged_damage_attribute;

            ForgeNetworking.CONFIG_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new ConfigCheckS2CPacket(serverOption));
        }));

    }




    @SubscribeEvent
    public static void addReloadListeners(TagsUpdatedEvent event) {
        if(Platform.getEnvironment() == Env.SERVER) {
            Map<ResourceLocation, JsonElement> itemMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(itemMap);
            Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
            MobXPLoader.applyNew(mobMap);
        } else {
            localServerLoad();

        }
    }

    @SubscribeEvent
    public static void syncConfig(OnDatapackSyncEvent event) {
        if(event.getPlayer() != null) {
            ServerPlayer player = event.getPlayer();
            if(player.server.isDedicatedServer()) {
                WLConfigReader.sync(player);
                LevelableItemsLoader.sync(player);
            } else if(player.server.isSingleplayer() && !player.server.isSingleplayerOwner(player.getGameProfile())) {
                WLConfigReader.sync(player);
                LevelableItemsLoader.sync(player);
            }
        }
    }







    @OnlyIn(Dist.CLIENT)
    public static void localServerLoad() {
        if(Minecraft.getInstance().isLocalServer()) {
            Map<ResourceLocation, JsonElement> itemMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(itemMap);
            Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
            MobXPLoader.applyNew(mobMap);
        }
    }


}
