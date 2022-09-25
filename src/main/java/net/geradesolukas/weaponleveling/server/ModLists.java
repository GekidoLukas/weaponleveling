package net.geradesolukas.weaponleveling.server;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ModLists {
    private static final String MCID = "minecraft:";
    private static final String ALEXMOBSID = "alexsmobs:";
    private static final String TETRAID = "tetra:";
    private static final String TINKERSID = "tconstruct:";

    public static final List<String> DEFAULT_ITEM_BLACKLIST = ImmutableList.of();
    //public static final List<String> DEFAULT_NO_MELEE = ImmutableList.of();
    public static final List<String> DEFAULT_MELEE_ITEMS = ImmutableList.of(MCID +"trident",
            TETRAID + "modular_sword", TETRAID + "modular_double",
            TINKERSID + "dagger", TINKERSID + "sword", TINKERSID + "cleaver", TINKERSID + "scythe", TINKERSID + "broad_axe", TINKERSID + "hand_axe",
            TINKERSID + "sledge_hammer", TINKERSID + "mattock"

    );

    public static final List<String> DEFAULT_PROJECTILE_ITEMS = ImmutableList.of(MCID +"trident",MCID +"bow",MCID + "crossbow",
            TETRAID + "modular_crossbow", TETRAID + "modular_bow"

    );


    public static final List<String> DEFAULT_ANIMALS = ImmutableList.of(MCID +"pig",MCID +"cow",MCID +"sheep",MCID +"chicken",MCID +"mooshroom",
            MCID +"axolotl",MCID +"bee",MCID +"cat",MCID +"donkey",MCID +"fox",MCID +"goat",
            MCID +"horse",MCID +"llama",MCID +"mule",MCID +"ocelot",MCID +"panda",MCID +"rabbit",
            MCID +"strider",MCID +"hoglin",MCID +"trader_llama",MCID +"turtle",MCID +"wolf",
            MCID +"squid",MCID +"glow_squid",MCID +"dolphin",MCID +"polar_bear",MCID +"pufferfish",
            MCID +"salmon",MCID +"cod",MCID +"tropical_fish",


            ALEXMOBSID +"grizzly_bear",ALEXMOBSID +"roadrunner",ALEXMOBSID +"gazelle",ALEXMOBSID +"crocodile",ALEXMOBSID +"fly",
            ALEXMOBSID +"hummingbird",ALEXMOBSID +"orca",ALEXMOBSID +"sunbird",ALEXMOBSID +"gorilla",ALEXMOBSID +"rattlesnake",
            ALEXMOBSID +"endergrade",ALEXMOBSID +"hammerhead_shark",ALEXMOBSID +"lobster",ALEXMOBSID +"komodo_dragon",ALEXMOBSID +"capuchin_monkey",
            ALEXMOBSID +"warped_toad",ALEXMOBSID +"moose",ALEXMOBSID +"raccoon",ALEXMOBSID +"blobfish",ALEXMOBSID +"seal",
            ALEXMOBSID +"cockroach",ALEXMOBSID +"shoebill",ALEXMOBSID +"elephant",ALEXMOBSID +"crow",ALEXMOBSID +"snow_leopard",
            ALEXMOBSID +"alligator_snapping_turtle",ALEXMOBSID +"mungus",ALEXMOBSID +"mantis_shrimp",ALEXMOBSID +"polar_bear",ALEXMOBSID +"emu",
            ALEXMOBSID +"platypus",ALEXMOBSID +"dropbear",ALEXMOBSID +"tasmanian_devil",ALEXMOBSID +"kangaroo",ALEXMOBSID +"cachalot_whale",
            ALEXMOBSID +"leafcutter_ant",ALEXMOBSID +"bald_eagle",ALEXMOBSID +"tiger",ALEXMOBSID +"tarantula_hawk",ALEXMOBSID +"frilled_shark",
            ALEXMOBSID +"mimic_octopus",ALEXMOBSID +"seagull",ALEXMOBSID +"tusklin",ALEXMOBSID +"toucan",ALEXMOBSID +"maned_wolf",
            ALEXMOBSID +"anaconda",ALEXMOBSID +"anteater",ALEXMOBSID +"flutter",ALEXMOBSID +"gelada_monkey",ALEXMOBSID +"jerboa",
            ALEXMOBSID +"terrapin",ALEXMOBSID +"comb_jelly",ALEXMOBSID +"cosmic_cod",ALEXMOBSID +"bunfungus",ALEXMOBSID +"bison",
            ALEXMOBSID +"giant_squid",ALEXMOBSID +"squid_grapple",ALEXMOBSID +"sea_bear",ALEXMOBSID +"devils_hole_pupfish",ALEXMOBSID +"catfish",
            ALEXMOBSID +"flying_fish",ALEXMOBSID +"rain_frog",ALEXMOBSID +"potoo",ALEXMOBSID +"mudskipper",ALEXMOBSID +"rhinoceros",
            ALEXMOBSID +"sugar_glider"






    );


    public static final List<String> DEFAULT_MONSTER = ImmutableList.of(MCID +"enderman",MCID +"zombified_piglin",MCID +"spider",MCID +"cave_spider",MCID +"blaze",
            MCID +"creeper",MCID +"drowned",MCID +"guardian",MCID +"endermite",MCID +"ghast",MCID +"husk",
            MCID +"magma_cube",MCID +"phantom",MCID +"piglin",MCID +"piglin_brute",MCID +"pillager",MCID +"ravager",
            MCID +"shulker",MCID +"silverfish",MCID +"skeleton",MCID +"slime",MCID +"stray",
            MCID +"vex",MCID +"vindicator",MCID +"witch",MCID +"wither_skeleton",MCID +"zoglin",
            MCID +"zombie",MCID +"zombie_villager",


            ALEXMOBSID +"bone_serpent",ALEXMOBSID +"crimson_mosquito",ALEXMOBSID +"mimicube",ALEXMOBSID +"soul_vulture",ALEXMOBSID +"spectre",
            ALEXMOBSID +"guster",ALEXMOBSID +"straddler",ALEXMOBSID +"stradpole",ALEXMOBSID +"enderiophage",ALEXMOBSID +"froststalker",
            ALEXMOBSID +"laviathan",ALEXMOBSID +"cosmaw",ALEXMOBSID +"rocky_roller",ALEXMOBSID +"skelewag"


    );

    public static final List<String> DEFAULT_MINIBOSSES = ImmutableList.of(MCID +"elder_guardian",MCID +"ravager",MCID +"evoker",
            ALEXMOBSID+"warped_mosco"


    );

    public static final List<String> DEFAULT_BOSSES = ImmutableList.of(MCID +"wither",MCID +"ender_dragon",
    ALEXMOBSID +"void_worm"

    );



}
