package net.geradesolukas.weaponleveling.config;

import net.geradesolukas.weaponleveling.server.ModLists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class WeaponLevelingConfig {


    public static class Server {

        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        public static final ForgeConfigSpec SPEC;

        public static final ForgeConfigSpec.ConfigValue<Integer> value_hit_xp_amount;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_hit_percentage;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_generic;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_monster;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_animal;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_miniboss;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_boss;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_max_level;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_level_modifier;
        public static final ForgeConfigSpec.ConfigValue<Integer> value_starting_level_amount;
        public static final ForgeConfigSpec.ConfigValue<Double> value_damage_per_level;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklist_items;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> added_items;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> no_melee_fallback;


        public static final ForgeConfigSpec.ConfigValue<Double> value_max_damage_reduction;


        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entities_animal;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entities_monster;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entities_miniboss;
        public static final ForgeConfigSpec.ConfigValue<List<? extends String>> entities_boss;


        public static final ForgeConfigSpec.EnumValue<LevelUpType> levelup_type;
        public enum LevelUpType {ACTIONBAR, TOAST}

        static {
            BUILDER.comment("Server Config").push("server");
            value_hit_xp_amount = BUILDER.comment("The amount of Weapon XP a player gains when hitting a mob").defineInRange("hittingXPAmount",1, 0, 10000);
            value_hit_percentage = BUILDER.comment("The chance of a hit giving you xp").defineInRange("hittingChance",20, 0, 100);




            value_max_level = BUILDER.comment("The Max Level a Weapon can Reach").defineInRange("weaponMaxLevel",500, 0, 10000);
            value_level_modifier = BUILDER.comment("The Modifier for the equation used to calculate the amount of xp needed to level up (Default = 80)").defineInRange("weaponLevelModifier",80, 0, 10000);
            value_starting_level_amount = BUILDER.comment("The XP amount needed to reach the first level (Default = 50)").defineInRange("weaponStartAmount",50, 0, 10000);
            value_damage_per_level= BUILDER.comment("The Extra Damage a weapon gets for each level (Default = 0.1)").defineInRange("weaponDamagePerLevel",0.1d, 0, 100);


            value_max_damage_reduction = BUILDER.comment("The Max Percentage an Armor Part can reduce").defineInRange("weaponMaxReduction",75.0, 0, 100);



            BUILDER.comment("Item Config").push("item");
            blacklist_items = BUILDER.comment("Blacklisted Items").defineList("blacklist_items", ModLists.DEFAULT_ITEM_BLACKLIST, o -> o instanceof String);
            added_items = BUILDER.comment("Extra Items, that are not extending the AxeItem or SwordItem Class").defineList("whitelisted_items", ModLists.DEFAULT_ITEM_WHITELIST, o -> o instanceof String);
            no_melee_fallback = BUILDER.comment("Items, that should be levelable, but should not cause melee damage (only use if it the mod does not already cover this)").defineList("no_melee_items", ModLists.DEFAULT_NO_MELEE, o -> o instanceof String);
            levelup_type = BUILDER.comment("How the player is notified about the item's Level Up",
                    "ACTIONBAR: Will display the Level Up in the Actionbar",
                    "TOAST: Will display the Level Up in the Actionbar").defineEnum("levelUpDisplayType", LevelUpType.TOAST);

            BUILDER.pop();


            BUILDER.comment("Entity Config").push("entiy");
            value_kill_generic = BUILDER.comment("The generic Amount of XP a Player gains when killing a not defined Entity").defineInRange("killingGenericXPAmount",1, 0, 10000);
            value_kill_animal = BUILDER.comment("The amount of Weapon XP a player gains when killing a mob").defineInRange("killingAnimalXPAmount",2, 0, 10000);
            value_kill_monster = BUILDER.comment("The amount of Weapon XP a player gains when killing a monster").defineInRange("killingMonsterXPAmount",10, 0, 10000);
            value_kill_miniboss = BUILDER.comment("The amount of Weapon XP a player gains when killing a mini boss").defineInRange("killingMiniBossXPAmount",50, 0, 10000);
            value_kill_boss = BUILDER.comment("The amount of Weapon XP a player gains when killing a boss").defineInRange("KillingBossXPAmount",100, 0, 10000);
            BUILDER.comment("\nAny LivingEntity, that is killed without being in the list will just have the GenericXPAmount\n");
            entities_animal = BUILDER.comment("A List of all the Animals affected by the mod").defineList("animal_entities", ModLists.DEFAULT_ANIMALS, o -> o instanceof String);
            entities_monster = BUILDER.comment("A List of all the Monsters affected by the mod").defineList("monster_entities", ModLists.DEFAULT_MONSTER, o -> o instanceof String);
            entities_miniboss = BUILDER.comment("A List of all the Mini Bosses affected by the mod").defineList("miniboss_entities", ModLists.DEFAULT_MINIBOSSES, o -> o instanceof String);
            entities_boss = BUILDER.comment("A List of all the Bosses affected by the mod").defineList("boss_entities", ModLists.DEFAULT_BOSSES, o -> o instanceof String);


            SPEC = BUILDER.build();
        }
    }
}
