package net.weaponleveling.forge.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.forge.compat.bettercombat.BetterCombatCompat;

public class HandItemCheck {


    public static ItemStack getAttackItem (Player player) {
        if(BetterCombatCompat.isLoaded) {
            return BetterCombatCompat.getAttackItem(player);
        }
        return player.getMainHandItem();


    }
}
