package net.weaponleveling;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.EnvType;
import net.minecraft.server.packs.PackType;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import net.weaponleveling.item.ModItems;
import net.weaponleveling.networking.Networking;
import net.weaponleveling.server.command.ItemLevelCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeaponLevelingMod {
    public static final String MODID = "weaponleveling";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static void init() {
        WLAttributes.register();
        CommandRegistrationEvent.EVENT.register(ItemLevelCommand::register);
        Networking.registerC2SPackets();
        ModItems.register();
        ReloadListenerRegistry.register(PackType.SERVER_DATA,LevelableItemsLoader.INSTANCE);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, MobXPLoader.INSTANCE);
        MidnightConfig.init(MODID, WeaponLevelingConfig.class);

        PlayerEvent.PLAYER_JOIN.register((player -> {
            if(Platform.getEnv() == EnvType.SERVER) {
                if(player.server.isDedicatedServer()) {
                    WLConfigReader.sync(player);
                    LevelableItemsLoader.sync(player);
                }
            }
        }));



        //TODO
        //- Example Datapacks
        //- Fix remaining known bugs
        //- Add Forge Item Tags
        //- Toggleable Ranged Attribute
        //- 

    }
}
