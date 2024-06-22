package net.weaponleveling.forge;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.forge.compat.HandItemCheck;
import net.weaponleveling.forge.compat.cgm.CGMCompat;
import net.weaponleveling.forge.compat.epicfight.EpicFightCompat;

public class WLPlatformGetterImpl {


    public static ItemStack getAttackItem(Player player) {
        return HandItemCheck.getAttackItem(player);
    }

    public static boolean isCGMGunItem(ItemStack stack) {
        return CGMCompat.isGunItem(stack);
    }


    public static void updateEpicFight(Player player, int xp) {
       EpicFightCompat.updateEpicItem(player,xp);
    }

}
