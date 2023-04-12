package net.weaponleveling.fabric;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.fabric.compat.bettercombat.BetterCombatCompat;
import net.weaponleveling.fabric.config.WeaponLevelingConfigFabric;
import net.weaponleveling.util.ToastHelper;

import java.util.List;

public class WLConfigGetterImpl {


    //Client
    public static boolean getHoldingShift() {
        return WeaponLevelingConfigFabric.Client.holdshift_for_tooltip.get();
    }

    //Server

    public static int getHitXPAmount() {
        return WeaponLevelingConfigFabric.Server.value_hit_xp_amount.get();
    }

    public static int getHitXPPercentage() {
        return WeaponLevelingConfigFabric.Server.value_hit_percentage.get();
    }

    public static int getCritXPAmount() {
        return WeaponLevelingConfigFabric.Server.value_crit_xp_amount.get();
    }

    public static int getCritXPPercentage() {
        return WeaponLevelingConfigFabric.Server.value_crit_percentage.get();
    }


    public static int getXPKillGeneric() {
        return WeaponLevelingConfigFabric.Server.value_kill_generic.get();
    }

    public static int getXPKillMonster() {
        return WeaponLevelingConfigFabric.Server.value_kill_monster.get();
    }

    public static int getXPKillAnimal() {
        return WeaponLevelingConfigFabric.Server.value_kill_animal.get();
    }

    public static int getXPKillMiniboss() {
        return WeaponLevelingConfigFabric.Server.value_kill_miniboss.get();
    }

    public static int getXPKillBoss() {
        return WeaponLevelingConfigFabric.Server.value_kill_boss.get();
    }


    public static int getMaxLevel() {
        return WeaponLevelingConfigFabric.Server.value_max_level.get();
    }

    public static int getLevelModifier() {
        return WeaponLevelingConfigFabric.Server.value_level_modifier.get();
    }

    public static int getStartingLevelAmount() {
        return WeaponLevelingConfigFabric.Server.value_starting_level_amount.get();
    }

    public static double getDamagePerLevel() {
        return WeaponLevelingConfigFabric.Server.value_damage_per_level.get();
    }

    public static double getMaxDamageReduction() {
        return WeaponLevelingConfigFabric.Server.value_max_damage_reduction.get();
    }

    public static double getBowlikeModifier() {
        return WeaponLevelingConfigFabric.Server.value_bowlike_damage_modifier.get();
    }

    public static int getArmorXPRNGModifier() {
        return WeaponLevelingConfigFabric.Server.value_armor_min_armor_xp_amount.get();
    }


    public static List<? extends String> getBlacklistedItems() {
        return WeaponLevelingConfigFabric.Server.blacklist_items.get();
    }

    public static List<? extends String> getMeleeItems() {
        return WeaponLevelingConfigFabric.Server.melee_items.get();
    }

    public static List<? extends String> getProjectedItems() {
        return WeaponLevelingConfigFabric.Server.projectile_items.get();
    }

    public static List<? extends String> getArmorItems() {
        return WeaponLevelingConfigFabric.Server.armor_items.get();
    }



    public static List<? extends String> getAnimalEntities() {
        return WeaponLevelingConfigFabric.Server.entities_animal.get();
    }

    public static List<? extends String> getMonsterEntities() {
        return WeaponLevelingConfigFabric.Server.entities_monster.get();
    }

    public static List<? extends String> getMinibossEntities() {
        return WeaponLevelingConfigFabric.Server.entities_miniboss.get();
    }

    public static List<? extends String> getBossEntities() {
        return WeaponLevelingConfigFabric.Server.entities_boss.get();
    }


    public static boolean getDisableUnlistedItems() {
        return WeaponLevelingConfigFabric.Server.disable_unlisted_items.get();
    }


    public static ToastHelper.LevelUpType getLevelUpType() {
        return WeaponLevelingConfigFabric.Server.levelup_type.get();
    }

    public static ItemStack getAttackItem(Player player) {
        return BetterCombatCompat.getAttackItem(player);
    }
}
