package net.weaponleveling;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.type.LevelingTypes;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import net.weaponleveling.data.ranged_damage.RangedDamageLoader;
import net.weaponleveling.item.ModItems;
import net.weaponleveling.item.component.WLDataComponents;
import net.weaponleveling.networking.ConfigSyncPayload;
import net.weaponleveling.networking.LevelableDataSyncPayload;
import net.weaponleveling.networking.RangedDamageDataSyncPayload;
import net.weaponleveling.networking.SendLevelingToastPayload;
import net.weaponleveling.server.command.ItemLevelCommand;
import net.weaponleveling.server.command.PrintTagListsCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class WeaponLevelingMod {
    public static final String MODID = "weaponleveling";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static void init() {
        WLAttributes.register();
        LevelingTypes.register();
        LevelingFunctions.register();
        CommandRegistrationEvent.EVENT.register(ItemLevelCommand::register);
        CommandRegistrationEvent.EVENT.register(PrintTagListsCommand::register);
        ModItems.register();
        WLDataComponents.register();
        ReloadListenerRegistry.register(PackType.SERVER_DATA, LevelableItemsLoader.INSTANCE);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, MobXPLoader.INSTANCE);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, RangedDamageLoader.INSTANCE);


        PlayerEvent.PLAYER_JOIN.register((player -> {
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

        if (Platform.getEnv() != EnvType.CLIENT) {
            NetworkManager.registerS2CPayloadType(ConfigSyncPayload.TYPE, ConfigSyncPayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(LevelableDataSyncPayload.TYPE, LevelableDataSyncPayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(SendLevelingToastPayload.TYPE, SendLevelingToastPayload.STREAM_CODEC);
            NetworkManager.registerS2CPayloadType(RangedDamageDataSyncPayload.TYPE, RangedDamageDataSyncPayload.STREAM_CODEC);
        }
    }

    public static ResourceLocation id(String string) {
        return ResourceLocation.fromNamespaceAndPath(MODID,string);
    }
}
