package net.weaponleveling;

import eu.midnightdust.lib.config.MidnightConfig;
import net.weaponleveling.util.ToastHelper;

import java.util.List;

public class WeaponLevelingConfig extends MidnightConfig {
    @Comment(category = "client", centered = true)
    public static Comment client_title;

    @Entry(category = "client")
    @Client
    public static boolean holdShiftToShow = false;
    @Entry(category = "client", isColor = true)
    @Client
    public static int titleColor = 12517240;
    @Entry(category = "client", isColor = true)
    @Client
    public static int arrowColor = 12517240;
    @Entry(category = "client", isColor = true)
    @Client
    public static int textColor = 9736850;
    @Entry(category = "client", isColor = true)
    @Client
    public static int valuesColor = 15422034;
    @Entry(category = "client", isColor = true)
    @Client
    public static int shiftColor = 12517240;
    @Entry(category = "client", isColor = true)
    @Client
    public static int brokenColor = 15422034;


    //LEVELING

    @Comment(category = "leveling", centered = true)
    public static Comment leveling_title;
    @Entry(category = "leveling", min = 0, max = 10000)
    public static int hit_xp_amount = 1;
    @Entry(category = "leveling", min = 0, max = 100, isSlider = true)
    public static int hit_percentage = 20;
    @Entry(category = "leveling", min = 0, max = 10000)
    public static int crit_xp_amount = 2;
    @Entry(category = "leveling", min = 0, max = 100, isSlider = true)
    public static int crit_percentage = 80;
    @Entry(category = "leveling", min = 0, max = 10000)
    public static int max_item_level = 500;
    @Entry(category = "leveling", min = 0, max = 10000)
    public static int level_modifier = 80;
    @Entry(category = "leveling", min = 0, max = 10000)
    public static int starting_xp_amount = 100;
    @Entry(category = "leveling", min = 0d, max = 100d)
    public static double damage_per_level = 0.1d;
    @Entry(category = "leveling", min = 0d, max = 100d)
    public static double armor_per_level = 0.1d;
    @Entry(category = "leveling", min = 0d, max = 100d)
    public static double toughness_per_level = 0.1d;

    @Entry(category = "leveling", min = 0, max = 10000)
    public static int armor_rng_modifier = 50;

    @Entry(category = "leveling", min = 0d, max = 10d)
    public static double bow_like_damage_modifier=0.25d;
    @Entry(category = "leveling")
    public static ToastHelper.LevelUpType level_up_type = ToastHelper.LevelUpType.TOAST;


    //ITEMS
    @Comment(category = "items", centered = true)
    public static Comment items_title;
    @Entry(category = "items")
    public static boolean broken_items_wont_vanish = true;

    @Entry(category = "items")
    public static boolean send_registry_in_log;

    @Entry(category = "items")
    public static boolean disable_unlisted_items;



    @Entry(category = "items")
    public static boolean levelable_items_auto_unbreakable = true;





    //ENTITIES
    @Comment(category = "entities", centered = true)
    public static Comment entities_title;

    @Entry(category = "entities", min = 0, max = 10000)
    public static int xp_for_generic_kill = 1;
    @Entry(category = "entities", min = 0, max = 10000)
    public static int xp_for_animal_kill = 2;
    @Entry(category = "entities", min = 0, max = 10000)
    public static int xp_for_monster_kill = 10;

    @Entry(category = "entities", min = 0, max = 10000)
    public static int xp_for_mini_boss_kill = 50;
    @Entry(category = "entities", min = 0, max = 10000)
    public static int xp_for_boss_kill = 100;




}
