package net.weaponleveling.fabric;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.fabric.compat.bettercombat.BetterCombatCompat;
import net.weaponleveling.fabric.config.WeaponLevelingConfigFabric;
import net.weaponleveling.util.ToastHelper;

import java.util.List;

public class WLPlatformGetterImpl {




    public static ItemStack getAttackItem(Player player) {
        return BetterCombatCompat.getAttackItem(player);
    }

    public static boolean isCGMGunItem(ItemStack stack) {
        return false;
    }



    public static void updateEpicFight(Player player, int xp) {

    }

}
