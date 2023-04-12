package net.weaponleveling.forge.compat.bettercombat;

import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.PlayerAttackProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BetterCombatMethods {

    public static ItemStack getAttackItem(Player player) {
        if(BetterCombatCompat.isLoaded) {
                if (PlayerAttackHelper.getCurrentAttack(player, ((PlayerAttackProperties)player).getComboCount()) == null) return player.getMainHandItem();
            else return PlayerAttackHelper.getCurrentAttack(player, ((PlayerAttackProperties)player).getComboCount()).itemStack();
        }
        else return player.getMainHandItem();
    }
}
