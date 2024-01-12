package net.weaponleveling;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import net.weaponleveling.data.LevelableItemsLoader;
import net.weaponleveling.networking.Networking;
import net.weaponleveling.server.command.ItemLevelCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeaponLevelingMod {
    public static final String MODID = "weaponleveling";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static void init() {
        CommandRegistrationEvent.EVENT.register(ItemLevelCommand::register);
        //Networking.registerC2SPackets();
        ReloadListenerRegistry.register(PackType.SERVER_DATA,LevelableItemsLoader.INSTANCE);
    }
}
