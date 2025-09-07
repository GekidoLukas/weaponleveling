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


    @Comment(category = "general", centered = true)
    public static Comment general_title;

    @Entry(category = "general")
    public static boolean apply_ranged_damage_attribute = true;

    //LEVELING
    @Comment(category = "leveling", centered = true)
    public static Comment leveling_title;

    @Entry(category = "leveling")
    public static boolean send_registry_in_log;

    @Entry(category = "leveling")
    public static ToastHelper.LevelUpType level_up_type = ToastHelper.LevelUpType.TOAST;

    @Entry(category = "leveling")
    public static int kill_xp = 1;

    //Syncable
    @Entry(category = "leveling")
    public static boolean broken_items_wont_vanish = true;

    @Entry(category = "leveling")
    public static boolean levelable_items_auto_unbreakable = true;


    @Entry(category = "leveling")
    public static int hit_xp_amount = 1;

    @Entry(category = "leveling")
    public static int hit_xp_chance = 20;

    @Entry(category = "leveling")
    public static int max_item_level = 500;

    @Entry(category = "leveling")
    public static int starting_xp_amount = 100;

    @Entry(category = "leveling")
    public static int xp_apply_chance = 50;

    @Entry(category = "leveling")
    public static int level_modifier = 80;




    @Entry(category = "leveling")
    public static double value_per_level = 0.1d;


}
