package net.geradesolukas.weaponleveling.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class WeaponLevelingConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> value_hit_xp_amount;
    public static final ForgeConfigSpec.ConfigValue<Integer> value_hit_percentage;
    public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_generic;
    public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_mob;
    public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_animal;
    public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_miniboss;
    public static final ForgeConfigSpec.ConfigValue<Integer> value_kill_boss;
    public static final ForgeConfigSpec.ConfigValue<Integer> value_max_level;


    static {
        BUILDER.comment("Common Config").push("common");
        value_hit_xp_amount = BUILDER.comment("The amount of Weapon XP a player gains when hitting a mob").defineInRange("hittingXPAmount",1, 0, 10000);
        value_hit_percentage = BUILDER.comment("The chance of a hit giving you xp").defineInRange("hittingChance",20, 0, 100);


        value_kill_generic = BUILDER.comment("The generic Amount of XP a Player gains when killing a not defined Entity").defineInRange("killingGenericXPAmount",1, 0, 10000);
        value_kill_animal = BUILDER.comment("The amount of Weapon XP a player gains when killing a mob").defineInRange("killingAnimalXPAmount",2, 0, 10000);
        value_kill_mob = BUILDER.comment("The amount of Weapon XP a player gains when killing a mob").defineInRange("killingMobXPAmount",10, 0, 10000);
        value_kill_miniboss = BUILDER.comment("The amount of Weapon XP a player gains when killing a mini boss").defineInRange("killingMiniBossXPAmount",50, 0, 10000);
        value_kill_boss = BUILDER.comment("The amount of Weapon XP a player gains when killing a boss").defineInRange("KillingBossXPAmount",100, 0, 10000);


        value_max_level = BUILDER.comment("The Max Level a Weapon can Reach").defineInRange("weaponMaxLevel",500, 0, 10000);



        SPEC = BUILDER.build();
    }
}
