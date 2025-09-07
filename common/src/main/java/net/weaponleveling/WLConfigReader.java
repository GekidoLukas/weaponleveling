package net.weaponleveling;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static net.weaponleveling.networking.Networking.SYNC_CONFIG;

public class WLConfigReader {

    public static final ResourceLocation CONFIG_CHANNEL = new ResourceLocation(WeaponLevelingMod.MODID, "config_check");


    public static void sync(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(WeaponLevelingConfig.broken_items_wont_vanish);
        buf.writeBoolean(WeaponLevelingConfig.levelable_items_auto_unbreakable);
        buf.writeInt(WeaponLevelingConfig.hit_xp_amount);
        buf.writeInt(WeaponLevelingConfig.hit_xp_chance);
        buf.writeInt(WeaponLevelingConfig.max_item_level);
        buf.writeInt(WeaponLevelingConfig.starting_xp_amount);
        buf.writeInt(WeaponLevelingConfig.xp_apply_chance);
        buf.writeInt(WeaponLevelingConfig.level_modifier);
        buf.writeDouble(WeaponLevelingConfig.value_per_level);

        NetworkManager.sendToPlayer(player, SYNC_CONFIG, buf);
    }
}
