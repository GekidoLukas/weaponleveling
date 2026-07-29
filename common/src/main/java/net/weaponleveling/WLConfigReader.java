package net.weaponleveling;

import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.weaponleveling.networking.ConfigSyncPayload;


public class WLConfigReader {

    public static final ResourceLocation CONFIG_CHANNEL = ResourceLocation.fromNamespaceAndPath(WeaponLevelingMod.MODID, "config_check");


    public static void sync(ServerPlayer player) {
        NetworkManager.sendToPlayer(player, new ConfigSyncPayload(
                WeaponLevelingConfig.broken_items_wont_vanish,
                WeaponLevelingConfig.levelable_items_auto_unbreakable,
                WeaponLevelingConfig.hit_xp_amount,
                WeaponLevelingConfig.hit_xp_chance,
                WeaponLevelingConfig.max_item_level,
                WeaponLevelingConfig.starting_xp_amount,
                WeaponLevelingConfig.xp_apply_chance,
                WeaponLevelingConfig.value_per_level
        ));
    }
}
