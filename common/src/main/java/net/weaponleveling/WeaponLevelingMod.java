package net.weaponleveling;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.RandomSource;
import net.weaponleveling.api.event.ItemReplaceBrokenEvent;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.type.LevelingTypes;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import net.weaponleveling.item.ModItems;
import net.weaponleveling.networking.Networking;
import net.weaponleveling.server.command.ItemLevelCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class WeaponLevelingMod {
    public static final String MODID = "weaponleveling";


    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static void init() {
        WLAttributes.register();
        LevelingTypes.register();
        LevelingFunctions.register();
        CommandRegistrationEvent.EVENT.register(ItemLevelCommand::register);
        Networking.registerC2SPackets();
        ModItems.register();
        ReloadListenerRegistry.register(PackType.SERVER_DATA,LevelableItemsLoader.INSTANCE);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, MobXPLoader.INSTANCE);


        PlayerEvent.PLAYER_JOIN.register((player -> {
            if(Platform.getEnv() == EnvType.SERVER) {
                if(player.server.isDedicatedServer()) {
                    WLConfigReader.sync(player);
                    LevelableItemsLoader.sync(player);
                }
            }
        }));









    }

    public static ResourceLocation id(String string) {
        return new ResourceLocation(MODID,string);
    }
}
