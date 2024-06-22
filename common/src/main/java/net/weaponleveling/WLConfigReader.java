package net.weaponleveling;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import static net.weaponleveling.networking.Networking.CONFIG_SYNC_PACKET;

public class WLConfigReader {



    public static void sync(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(WeaponLevelingConfig.hit_xp_amount);
        buf.writeInt(WeaponLevelingConfig.hit_percentage);
        buf.writeInt(WeaponLevelingConfig.crit_xp_amount);
        buf.writeInt(WeaponLevelingConfig.crit_percentage);
        buf.writeInt(WeaponLevelingConfig.max_item_level);
        buf.writeInt(WeaponLevelingConfig.level_modifier);
        buf.writeInt(WeaponLevelingConfig.starting_xp_amount);
        buf.writeDouble(WeaponLevelingConfig.damage_per_level);
        buf.writeInt(WeaponLevelingConfig.armor_rng_modifier);
        buf.writeDouble(WeaponLevelingConfig.max_damage_reduction_percentage);
        buf.writeDouble(WeaponLevelingConfig.bow_like_damage_modifier);
        buf.writeBoolean(WeaponLevelingConfig.broken_items_wont_vanish);
        buf.writeBoolean(WeaponLevelingConfig.disable_unlisted_items);
        buf.writeBoolean(WeaponLevelingConfig.levelable_items_auto_unbreakable);
        buf.writeInt(WeaponLevelingConfig.xp_for_generic_kill);
        buf.writeInt(WeaponLevelingConfig.xp_for_animal_kill);
        buf.writeInt(WeaponLevelingConfig.xp_for_monster_kill);
        buf.writeInt(WeaponLevelingConfig.xp_for_mini_boss_kill);
        buf.writeInt(WeaponLevelingConfig.xp_for_boss_kill);


        NetworkManager.sendToPlayer(player, CONFIG_SYNC_PACKET, buf);
    }
}
