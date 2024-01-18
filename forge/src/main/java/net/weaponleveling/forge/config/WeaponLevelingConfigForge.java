package net.weaponleveling.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.weaponleveling.util.ToastHelper;

import java.util.List;

public class WeaponLevelingConfigForge {


    public static class Client {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;


        public static final ForgeConfigSpec.ConfigValue<Boolean> holdshift_for_tooltip;
        static {
            BUILDER.comment("Client Config").push("client");

            holdshift_for_tooltip = BUILDER.comment("Whether the extended tooltip should be rendered when shift is pressed or not").define("shift_for_tooltip",false);



            SPEC = BUILDER.build();
        }

    }
    public static class Server {

        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        public static final ForgeConfigSpec SPEC;

        public static final ForgeConfigSpec.ConfigValue<Integer> value_hit_xp_amount;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_hit_percentage;

        public static final ForgeConfigSpec.ConfigValue<Integer> value_crit_xp_amount;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_crit_percentage;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_generic;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_monster;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_animal;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_miniboss;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_boss;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_max_level;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_level_modifier;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_starting_level_amount;
        public static final ForgeConfigSpec.ConfigValue<Double> value_damage_per_level;

        public static final ForgeConfigSpec.ConfigValue<Integer> value_armor_min_armor_xp_amount;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklist_items;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> melee_items;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> projectile_items;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> armor_items;

        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> unbreakable_items_whitelist;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> unbreakable_items_blacklist;


        public static final ForgeConfigSpec.ConfigValue<Boolean> levelable_items_auto_unbreakable;
        public static final ForgeConfigSpec.ConfigValue<Double> value_max_damage_reduction;
        public static final ForgeConfigSpec.ConfigValue<Double> value_bowlike_damage_modifier;


        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entities_animal;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entities_monster;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entities_miniboss;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entities_boss;

        public static final ForgeConfigSpec.ConfigValue<Boolean> broken_items_wont_vanish;

        public static final ForgeConfigSpec.ConfigValue<Boolean> send_registry_in_log;

        public static final ForgeConfigSpec.ConfigValue<Boolean> disable_unlisted_items;

        public static final ForgeConfigSpec.EnumValue<ToastHelper.LevelUpType> levelup_type;

        static {
            BUILDER.comment("Server Config").push("server");
            value_hit_xp_amount = BUILDER.comment("The amount of Weapon XP a player gains when hitting a mob").defineInRange("hittingXPAmount",1, 0, 10000);
            value_hit_percentage = BUILDER.comment("The chance of a hit giving you xp").defineInRange("hittingChance",20, 0, 100);

            value_crit_xp_amount = BUILDER.comment("The amount of Weapon XP a player gains when critting a mob").defineInRange("value_crit_xp_amount",2, 0, 10000);
            value_crit_percentage = BUILDER.comment("The chance of a crit giving you xp").defineInRange("value_crit_percentage",80, 0, 100);




            value_max_level = BUILDER.comment("The Max Level a Weapon can Reach").defineInRange("weaponMaxLevel",500, 0, 10000);
            value_level_modifier = BUILDER.comment("The Modifier for the equation used to calculate the amount of xp needed to level up (Default = 80)").defineInRange("weaponLevelModifier",80, 0, 10000);
            value_starting_level_amount = BUILDER.comment("The XP amount needed to reach the first level (Default = 50)").defineInRange("weaponStartAmount",50, 0, 10000);
            value_damage_per_level = BUILDER.comment("The Extra Damage a weapon gets for each level (Default = 0.1)").defineInRange("weaponDamagePerLevel",0.1d, 0, 100);
            value_bowlike_damage_modifier = BUILDER.comment("The Value, that gets multiplied with the bow's damage to lower it. It is recommended to leave it below 1  (Default = 0.25d)").defineInRange("weaponBowLikeModifier",0.25d, 0, 10);


            value_max_damage_reduction = BUILDER.comment("The Max Percentage an Armor Part can reduce").defineInRange("weaponMaxReduction",75.0, 0, 100);

            value_armor_min_armor_xp_amount = BUILDER.comment("The value that defines the range in which an RNG will generate a random amount of XP for each armor Piece. Setting it to 100 will cause the RNG to have no effect").defineInRange("value_armor_min_armor_xp_amount",50, 0, 100);


            BUILDER.comment("Item Config").push("item");
            blacklist_items = BUILDER.comment("Blacklisted Items").defineList("blacklist_items", ConfigListsForge.DEFAULT_ITEM_BLACKLIST, o -> o instanceof String);
            melee_items = BUILDER.comment("Melee Weapons, that are not extending the AxeItem or SwordItem Class").defineList("melee_items", ConfigListsForge.DEFAULT_MELEE_ITEMS, o -> o instanceof String);
            armor_items = BUILDER.comment("Armors, that are not extending the Armor Class or Should be added if Option \"disable_unlisted_items\" is set to true").defineList("armor_items", ConfigListsForge.DEFAULT_ARMOR_ITEMS, o -> o instanceof String);
            projectile_items = BUILDER.comment("Projectile Weapons, that are not extending the ProjectileItem Class").defineList("projectile_items", ConfigListsForge.DEFAULT_PROJECTILE_ITEMS, o -> o instanceof String);
            levelup_type = BUILDER.comment("How the player is notified about the item's Level Up",
                    "ACTIONBAR: Will display the Level Up in the Actionbar",
                    "TOAST: Will display the Level Up in the Actionbar").defineEnum("levelUpDisplayType", ToastHelper.LevelUpType.TOAST);


            unbreakable_items_whitelist = BUILDER.comment("Items, that should not fully break when their durability reaches 0.").defineList("unbreakable_items_whitelist", ConfigListsForge.DEFAULT_UNBREAKABLE_WHITELIST, o -> o instanceof String);
            unbreakable_items_blacklist = BUILDER.comment("Items, you want to break, even if they are a Levelable item or in the whitelist (why did you put em there?).").defineList("unbreakable_items_blacklist", ConfigListsForge.DEFAULT_UNBREAKABLE_BLACKLIST, o -> o instanceof String);
            levelable_items_auto_unbreakable = BUILDER.comment("If set to true, all items that are levelable will not break when durability reaches 0").define("levelable_items_auto_unbreakable", true);


            broken_items_wont_vanish = BUILDER.comment("If set to true, items will not vanish when broken, but rather have a useless version").define("broken_items_wont_vanish", true);
            disable_unlisted_items = BUILDER.comment("If set to true, Swords, Axes and Armor will not be supported").define("disable_unlisted_items", false);

            send_registry_in_log = BUILDER.comment("Defines whether Weapon Leveling will log every registered weapon as debug. If an Item is invalid it will still send an Error.").define("send_registry_in_log", false);

            BUILDER.pop();


            BUILDER.comment("Entity Config").push("entiy");
            value_kill_generic = BUILDER.comment("The generic Amount of XP a Player gains when killing a not defined Entity").defineInRange("killingGenericXPAmount",1, 0, 10000);
            value_kill_animal = BUILDER.comment("The amount of Weapon XP a player gains when killing a mob").defineInRange("killingAnimalXPAmount",2, 0, 10000);
            value_kill_monster = BUILDER.comment("The amount of Weapon XP a player gains when killing a monster").defineInRange("killingMonsterXPAmount",10, 0, 10000);
            value_kill_miniboss = BUILDER.comment("The amount of Weapon XP a player gains when killing a mini boss").defineInRange("killingMiniBossXPAmount",50, 0, 10000);
            value_kill_boss = BUILDER.comment("The amount of Weapon XP a player gains when killing a boss").defineInRange("KillingBossXPAmount",100, 0, 10000);
            BUILDER.comment("\nAny LivingEntity, that is killed without being in the list will just have the GenericXPAmount\n");
            entities_animal = BUILDER.comment("A List of all the Animals affected by the mod").defineList("animal_entities", ConfigListsForge.DEFAULT_ANIMALS, o -> o instanceof String);
            entities_monster = BUILDER.comment("A List of all the Monsters affected by the mod").defineList("monster_entities", ConfigListsForge.DEFAULT_MONSTER, o -> o instanceof String);
            entities_miniboss = BUILDER.comment("A List of all the Mini Bosses affected by the mod").defineList("miniboss_entities", ConfigListsForge.DEFAULT_MINIBOSSES, o -> o instanceof String);
            entities_boss = BUILDER.comment("A List of all the Bosses affected by the mod").defineList("boss_entities", ConfigListsForge.DEFAULT_BOSSES, o -> o instanceof String);


            SPEC = BUILDER.build();
        }
    }
}
