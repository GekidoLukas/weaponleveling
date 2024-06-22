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
    public static int starting_xp_amount = 50;
    @Entry(category = "leveling", min = 0d, max = 100d)
    public static double damage_per_level = 0.1d;
    @Entry(category = "leveling", min = 0, max = 10000)
    public static int armor_rng_modifier = 50;
    @Entry(category = "leveling", min = 0, max = 100, isSlider = true, precision = 1)
    public static double max_damage_reduction_percentage= 75.0d;
    @Entry(category = "leveling", min = 0d, max = 10d)
    public static double bow_like_damage_modifier=0.25d;
    @Entry(category = "leveling")
    public static ToastHelper.LevelUpType level_up_type = ToastHelper.LevelUpType.TOAST;


    //ITEMS
    @Comment(category = "items", centered = true)
    public static Comment items_title;
    @Entry(category = "items")
    public static boolean broken_items_wont_vanish;

    @Entry(category = "items")
    public static boolean send_registry_in_log;

    @Entry(category = "items")
    public static boolean disable_unlisted_items;

//    @Entry(category = "items")
//    public static List<String> blacklist_items = ConfigLists.DEFAULT_ITEM_BLACKLIST;
//    @Entry(category = "items")
//    public static List<String> melee_items = ConfigLists.DEFAULT_MELEE_ITEMS;
//    @Entry(category = "items")
//    public static List<String> projectile_items = ConfigLists.DEFAULT_PROJECTILE_ITEMS;
//    @Entry(category = "items")
//    public static List<String> armor_items = ConfigLists.DEFAULT_ARMOR_ITEMS;

//    @Entry(category = "items")
//    public static List<String> unbreakable_items_whitelist = ConfigLists.DEFAULT_UNBREAKABLE_WHITELIST;
//    @Entry(category = "items")
//    public static List<String> unbreakable_items_blacklist = ConfigLists.DEFAULT_UNBREAKABLE_BLACKLIST;

    @Entry(category = "items")
    public static boolean levelable_items_auto_unbreakable = true;





    //ENTITIES
    @Comment(category = "entities", centered = true)
    public static Comment entities_title;
//    @Entry(category = "entities")
//    public static List<String> entities_animal = ConfigLists.DEFAULT_ANIMALS;
//    @Entry(category = "entities")
//    public static List<String> entities_monster = ConfigLists.DEFAULT_MONSTER;
//    @Entry(category = "entities")
//    public static List<String> entities_mini_boss = ConfigLists.DEFAULT_MINIBOSSES;
//    @Entry(category = "entities")
//    public static List<String> entities_boss = ConfigLists.DEFAULT_BOSSES;

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
