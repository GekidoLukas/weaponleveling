package net.weaponleveling;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.RegistrarManager;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.server.packs.PackType;
import net.weaponleveling.data.LevelableItemsLoader;
import net.weaponleveling.networking.Networking;
import net.weaponleveling.server.command.ItemLevelCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeaponLevelingMod {
    public static final String MODID = "weaponleveling";

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MODID));
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static void init() {
        CommandRegistrationEvent.EVENT.register(ItemLevelCommand::register);
        Networking.registerC2SPackets();
        ReloadListenerRegistry.register(PackType.SERVER_DATA,LevelableItemsLoader.INSTANCE);
        MidnightConfig.init(MODID, WeaponLevelingConfig.class);
        //TODO
        //- Example Datapacks
        //- Individual Editing of entities
        //- Fix remaining known bugs
        //- Staring xp + following on next levels

    }
}
