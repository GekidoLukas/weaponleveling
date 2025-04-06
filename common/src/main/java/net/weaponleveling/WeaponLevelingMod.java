package net.weaponleveling;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.EnvType;
import net.minecraft.server.packs.PackType;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.client.ClientEvents;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import net.weaponleveling.item.ModItems;
import net.weaponleveling.networking.Networking;
import net.weaponleveling.server.command.ItemLevelCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class WeaponLevelingMod {
    public static final String MODID = "weaponleveling";

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MODID));
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
//        PlayerEvent.PLAYER_QUIT.register(player -> {
//
//        });



//        LifecycleEvent.SERVER_STARTED.register((LevelableDefaults::writeFromGameRule));
        //TODO
        //- Example Datapacks
        //- Individual Editing of entities
        //- Fix remaining known bugs
        //- Staring xp + following on next levels

    }
}
