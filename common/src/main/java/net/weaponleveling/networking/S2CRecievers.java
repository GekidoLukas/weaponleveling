package net.weaponleveling.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.util.CustomToast;
import net.weaponleveling.util.DataGetter;

public class S2CRecievers {



    public static void receive() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, Networking.TOAST_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack stack = buf.readItem();
            int level = buf.readInt();
            Minecraft.getInstance().getToasts().addToast(new CustomToast(stack, level));
        });

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, Networking.CONFIG_SYNC_PACKET, (buf, context) -> {

            DataGetter.HIT_XP_AMOUNT = buf.readInt();
            DataGetter.HIT_PERCENTAGE = buf.readInt();
            DataGetter.CRIT_XP_AMOUNT = buf.readInt();
            DataGetter.CRIT_PERCENTAGE = buf.readInt();
            DataGetter.MAX_LEVEL = buf.readInt();
            DataGetter.LEVEL_MODIFIER = buf.readInt();
            DataGetter.STARTING_XP = buf.readInt();
            DataGetter.DAMAGE_PER_LEVEL = buf.readDouble();
            DataGetter.ARMOR_PER_LEVEL = buf.readDouble();
            DataGetter.TOUGHNESS_PER_LEVEL = buf.readDouble();
            DataGetter.ARMOR_RNG = buf.readInt();;
            DataGetter.BOW_LIKE_MODIFIER = buf.readDouble();
            DataGetter.BROKEN_ITEMS_WONT_VANISH = buf.readBoolean();
            DataGetter.DISABLE_UNLISTED = buf.readBoolean();
            DataGetter.LEVELABLE_AUTO_UNBREAKABLE = buf.readBoolean();
            DataGetter.XP_GENERIC = buf.readInt();
            DataGetter.XP_ANIMAL = buf.readInt();
            DataGetter.XP_MONSTER = buf.readInt();
            DataGetter.XP_MINIBOSS = buf.readInt();
            DataGetter.XP_BOSS = buf.readInt();

        });
    }
}
