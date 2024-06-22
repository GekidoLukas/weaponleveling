package net.weaponleveling;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ToastHelper;

import java.util.List;

public class WLPlatformGetter {


    @ExpectPlatform
    public static ItemStack getAttackItem(Player player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isCGMGunItem(ItemStack stack) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void updateEpicFight(Player playern, int xp) {
        throw new AssertionError();
    }

}
