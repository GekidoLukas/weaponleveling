package net.weaponleveling.fabric.compat.bettercombat;

import dev.architectury.platform.Platform;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingMod;

public class BetterCombatCompat {

    public static final String modId = "bettercombat";
    public static final Boolean isLoaded = Platform.isModLoaded(modId);


    public static ItemStack getAttackItem (Player player) {
            if(isLoaded) {

                return BetterCombatMethods.getAttackItem(player);
            }
            return player.getMainHandItem();


    }
}
