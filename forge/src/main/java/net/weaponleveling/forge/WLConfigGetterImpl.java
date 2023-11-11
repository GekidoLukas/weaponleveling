package net.weaponleveling.forge;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.forge.compat.bettercombat.BetterCombatCompat;
import net.weaponleveling.forge.compat.cgm.CGMCompat;
import net.weaponleveling.forge.config.WeaponLevelingConfigForge;
import net.weaponleveling.util.ToastHelper;

import java.util.List;

public class WLConfigGetterImpl {


    //Client
    public static boolean getHoldingShift() {
        return WeaponLevelingConfigForge.Client.holdshift_for_tooltip.get();
    }

    //Server

    public static int getHitXPAmount() {
        return WeaponLevelingConfigForge.Server.value_hit_xp_amount.get();
    }

    public static int getHitXPPercentage() {
        return WeaponLevelingConfigForge.Server.value_hit_percentage.get();
    }

    public static int getCritXPAmount() {
        return WeaponLevelingConfigForge.Server.value_crit_xp_amount.get();
    }

    public static int getCritXPPercentage() {
        return WeaponLevelingConfigForge.Server.value_crit_percentage.get();
    }


    public static int getXPKillGeneric() {
        return WeaponLevelingConfigForge.Server.value_kill_generic.get();
    }

    public static int getXPKillMonster() {
        return WeaponLevelingConfigForge.Server.value_kill_monster.get();
    }

    public static int getXPKillAnimal() {
        return WeaponLevelingConfigForge.Server.value_kill_animal.get();
    }

    public static int getXPKillMiniboss() {
        return WeaponLevelingConfigForge.Server.value_kill_miniboss.get();
    }

    public static int getXPKillBoss() {
        return WeaponLevelingConfigForge.Server.value_kill_boss.get();
    }


    public static int getMaxLevel() {
        return WeaponLevelingConfigForge.Server.value_max_level.get();
    }

    public static int getLevelModifier() {
        return WeaponLevelingConfigForge.Server.value_level_modifier.get();
    }

    public static int getStartingLevelAmount() {
        return WeaponLevelingConfigForge.Server.value_starting_level_amount.get();
    }

    public static double getDamagePerLevel() {
        return WeaponLevelingConfigForge.Server.value_damage_per_level.get();
    }

    public static double getMaxDamageReduction() {
        return WeaponLevelingConfigForge.Server.value_max_damage_reduction.get();
    }

    public static double getBowlikeModifier() {
        return WeaponLevelingConfigForge.Server.value_bowlike_damage_modifier.get();
    }

    public static int getArmorXPRNGModifier() {
        return WeaponLevelingConfigForge.Server.value_armor_min_armor_xp_amount.get();
    }


    public static List<? extends String> getBlacklistedItems() {
        return WeaponLevelingConfigForge.Server.blacklist_items.get();
    }

    public static List<? extends String> getMeleeItems() {
        return WeaponLevelingConfigForge.Server.melee_items.get();
    }

    public static List<? extends String> getProjectedItems() {
        return WeaponLevelingConfigForge.Server.projectile_items.get();
    }

    public static List<? extends String> getArmorItems() {
        return WeaponLevelingConfigForge.Server.armor_items.get();
    }

    public static List<? extends String> getUnbreakableWhitelist() {
        return WeaponLevelingConfigForge.Server.unbreakable_items_whitelist.get();
    }
    public static List<? extends String> getUnbreakableBlacklist() {
        return WeaponLevelingConfigForge.Server.unbreakable_items_blacklist.get();
    }

    public static boolean getLevelableIsUnbreakable() {
        return WeaponLevelingConfigForge.Server.levelable_items_auto_unbreakable.get();
    }

    public static List<? extends String> getAnimalEntities() {
        return WeaponLevelingConfigForge.Server.entities_animal.get();
    }

    public static List<? extends String> getMonsterEntities() {
        return WeaponLevelingConfigForge.Server.entities_monster.get();
    }

    public static List<? extends String> getMinibossEntities() {
        return WeaponLevelingConfigForge.Server.entities_miniboss.get();
    }

    public static List<? extends String> getBossEntities() {
        return WeaponLevelingConfigForge.Server.entities_boss.get();
    }


    public static boolean getDisableUnlistedItems() {
        return WeaponLevelingConfigForge.Server.disable_unlisted_items.get();
    }


    public static ToastHelper.LevelUpType getLevelUpType() {
        return WeaponLevelingConfigForge.Server.levelup_type.get();
    }

    public static ItemStack getAttackItem(Player player) {
        return BetterCombatCompat.getAttackItem(player);
    }

    public static boolean isCGMGunItem(ItemStack stack) {
        return CGMCompat.isGunItem(stack);
    }

    public static boolean getBrokenItemsDontVanish() {
        return WeaponLevelingConfigForge.Server.broken_items_wont_vanish.get();
    }
}
