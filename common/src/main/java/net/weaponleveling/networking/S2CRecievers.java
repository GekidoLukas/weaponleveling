package net.weaponleveling.networking;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.client.CustomToast;
import net.weaponleveling.data.ranged_damage.RangedDamageLoader;

public class S2CRecievers {



    public static void receive() {
        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                SendLevelingToastPayload.TYPE,
                SendLevelingToastPayload.STREAM_CODEC,
                (payload, context) -> {
                    ItemStack stack = payload.stack();
                    int level = payload.level();
                    context.queue(() -> {
                        Minecraft.getInstance().getToasts().addToast(new CustomToast(stack, level));
                    });
                }
        );



        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                ConfigSyncPayload.TYPE,
                ConfigSyncPayload.STREAM_CODEC,
                (payload, context) -> {

                    context.queue(() -> {
                        WeaponLevelingConfig.broken_items_wont_vanish = payload.brokenItemsWontVanish();
                        WeaponLevelingConfig.levelable_items_auto_unbreakable = payload.levelableItemsAutoUnbreakable();
                        WeaponLevelingConfig.hit_xp_amount = payload.hitXpAmount();
                        WeaponLevelingConfig.hit_xp_chance = payload.hitXpChance();
                        WeaponLevelingConfig.max_item_level = payload.maxItemLevel();
                        WeaponLevelingConfig.starting_xp_amount = payload.startingXpAmount();
                        WeaponLevelingConfig.xp_apply_chance = payload.xpApplyChance();
                        WeaponLevelingConfig.value_per_level = payload.valuePerLevel();

                    });
                }
        );


        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                LevelableDataSyncPayload.TYPE,
                LevelableDataSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.queue(() -> {
                        LevelableItemsLoader.setMap(payload.itemMap());
                    });
                }
        );

        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                RangedDamageDataSyncPayload.TYPE,
                RangedDamageDataSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.queue(() -> {
                        RangedDamageLoader.setMap(payload.itemMap());
                    });
                }
        );
    }
}
