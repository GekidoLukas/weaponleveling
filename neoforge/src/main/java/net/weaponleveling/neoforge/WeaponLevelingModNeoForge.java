package net.weaponleveling.neoforge;

import com.google.gson.JsonElement;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.weaponleveling.WLConfigReader;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.WeaponLevelingModClient;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import net.weaponleveling.data.ranged_damage.RangedDamageLoader;
import net.weaponleveling.neoforge.networking.*;
import net.weaponleveling.neoforge.networking.client.ClientNeoForgeHandlers;

import java.util.Map;

@Mod(WeaponLevelingMod.MODID)
@EventBusSubscriber
public final class WeaponLevelingModNeoForge {
    public WeaponLevelingModNeoForge() {
        IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();

        modBus.addListener(this::setup);
        WeaponLevelingMod.init();
        if(Platform.getEnv() == Dist.CLIENT) {
            WeaponLevelingModClient.init();
            NetworkManager.registerReceiver(
                    NetworkManager.s2c(),
                    ConfigCheckS2CPayload.TYPE,
                    ConfigCheckS2CPayload.STREAM_CODEC,
                    ClientNeoForgeHandlers::handleConfigCheckS2CPayload
            );

        }


    }

    private void setup(final FMLCommonSetupEvent event) {

        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                ConfigCheckC2SPayload.TYPE,
                ConfigCheckC2SPayload.STREAM_CODEC,
                ServerNeoForgeHandlers::handleConfigCheckC2SPayload
        );

        PlayerEvent.PLAYER_JOIN.register((player -> {
            boolean serverOption = WeaponLevelingConfig.apply_ranged_damage_attribute;


            NetworkManager.sendToPlayer(player,new ConfigCheckS2CPayload(serverOption));
        }));

        if (Platform.getEnv() != Dist.CLIENT) {
            NetworkManager.registerS2CPayloadType(ConfigCheckS2CPayload.TYPE, ConfigCheckS2CPayload.STREAM_CODEC);
        }

    }




    @SubscribeEvent
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> entityType : event.getTypes()) {

            if (!event.has(entityType, WLAttributes.RANGED_DAMAGE)) {
                event.add(entityType, WLAttributes.RANGED_DAMAGE);
            }
        }
    }



    @SubscribeEvent
    public static void addReloadListeners(TagsUpdatedEvent event) {
        if(Platform.getEnvironment() == Env.SERVER) {
            Map<ResourceLocation, JsonElement> itemMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(itemMap,event.getRegistryAccess());
            Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
            MobXPLoader.applyNew(mobMap);
            Map<ResourceLocation, JsonElement> rangedDamageMap = RangedDamageLoader.MAP;
            RangedDamageLoader.applyNew(rangedDamageMap);
        } else {
            localServerLoad(event.getRegistryAccess());

        }
    }

    @SubscribeEvent
    public static void syncConfig(OnDatapackSyncEvent event) {
        if(event.getPlayer() != null) {
            ServerPlayer player = event.getPlayer();
            if(player.server.isDedicatedServer()) {
                WLConfigReader.sync(player);
                LevelableItemsLoader.sync(player);
                RangedDamageLoader.sync(player);
            } else if(player.server.isSingleplayer() && !player.server.isSingleplayerOwner(player.getGameProfile())) {
                WLConfigReader.sync(player);
                LevelableItemsLoader.sync(player);
                RangedDamageLoader.sync(player);
            }
        }
    }







    @OnlyIn(Dist.CLIENT)
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
