package net.weaponleveling.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.LevelableItem;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.client.CustomToast;

import java.util.HashMap;
import java.util.Map;

public class S2CRecievers {



    public static void receive() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, Networking.TOAST_PACKET_ID, (buf, context) -> {
            Player player = context.getPlayer();
            ItemStack stack = buf.readItem();
            int level = buf.readInt();
            Minecraft.getInstance().getToasts().addToast(new CustomToast(stack, level));
        });




        NetworkManager.registerReceiver(NetworkManager.Side.S2C, Networking.SYNC_CONFIG, (buf, context) -> {
            WeaponLevelingConfig.broken_items_wont_vanish = buf.readBoolean();
            WeaponLevelingConfig.levelable_items_auto_unbreakable = buf.readBoolean();
            WeaponLevelingConfig.hit_xp_amount = buf.readInt();
            WeaponLevelingConfig.hit_xp_chance = buf.readInt();
            WeaponLevelingConfig.max_item_level = buf.readInt();
            WeaponLevelingConfig.starting_xp_amount = buf.readInt();
            WeaponLevelingConfig.xp_apply_chance = buf.readInt();
            WeaponLevelingConfig.value_per_level = buf.readDouble();


        });


        NetworkManager.registerReceiver(NetworkManager.Side.S2C, Networking.SYNC_DATA, (buf, context) -> {
            WeaponLevelingMod.LOGGER.info("Receiving Levelable Item Data from Server");
            Map<ResourceLocation, LevelableItem> builder = new HashMap<>();
            int count = buf.readInt();

            for(int i = 0; i< count; i++) {
                ResourceLocation resourceLocation = buf.readResourceLocation();
                LevelableItem levelableItem = LevelableItem.read(buf);
                builder.put(resourceLocation,levelableItem);
            }

            LevelableItemsLoader.setMap(builder);
            WeaponLevelingMod.LOGGER.info("Received " + count + " Levelable Item Entries");
        });
    }
}
