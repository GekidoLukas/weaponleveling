package net.weaponleveling;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ToastHelper;

import java.util.List;

public class WLPlatformGetter {

    //Client
    @ExpectPlatform
    public static boolean getHoldingShift() {
        throw new AssertionError();
    }


    //Server
    @ExpectPlatform
    public static int getHitXPAmount() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getHitXPPercentage() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getCritXPAmount() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getCritXPPercentage() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getXPKillGeneric() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getXPKillMonster() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getXPKillAnimal() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getXPKillMiniboss() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getXPKillBoss() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static int getMaxLevel() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getLevelModifier() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getStartingLevelAmount() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static double getDamagePerLevel() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static double getMaxDamageReduction() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static double getBowlikeModifier() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static int getArmorXPRNGModifier() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static List<? extends String> getBlacklistedItems() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static List<? extends String> getMeleeItems() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static List<? extends String> getProjectedItems() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static List<? extends String> getArmorItems() {
        throw new AssertionError();
    }


    @ExpectPlatform
    public static List<? extends String> getAnimalEntities() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static List<? extends String> getMonsterEntities() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static List<? extends String> getMinibossEntities() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static List<? extends String> getBossEntities() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean getDisableUnlistedItems() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static List<? extends String> getUnbreakableWhitelist() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static List<? extends String> getUnbreakableBlacklist() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static boolean getLevelableIsUnbreakable() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean getBrokenItemsDontVanish() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ToastHelper.LevelUpType getLevelUpType() {
        throw new AssertionError();
    }

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
