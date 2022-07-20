package net.geradesolukas.weaponleveling.server;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ModLists {
    private static final String MCID = "minecraft:";

    public static final List<String> DEFAULT_ITEM_BLACKLIST = ImmutableList.of();
    public static final List<String> DEFAULT_ITEM_WHITELIST = ImmutableList.of(MCID +"trident");



    public static final List<String> DEFAULT_ANIMALS = ImmutableList.of(MCID +"pig",MCID +"cow",MCID +"sheep",MCID +"chicken",MCID +"mooshroom",
            MCID +"axolotl",MCID +"bee",MCID +"cat",MCID +"donkey",MCID +"fox",MCID +"goat",
            MCID +"horse",MCID +"llama",MCID +"mule",MCID +"ocelot",MCID +"panda",MCID +"rabbit",
            MCID +"strider",MCID +"hoglin",MCID +"trader_llama",MCID +"turtle",MCID +"wolf",
            MCID +"squid",MCID +"glow_squid",MCID +"dolphin",MCID +"polar_bear",MCID +"pufferfish",
            MCID +"salmon",MCID +"cod",MCID +"tropical_fish");

    public static final List<String> DEFAULT_MONSTER = ImmutableList.of(MCID +"enderman",MCID +"zombified_piglin",MCID +"spider",MCID +"cave_spider",MCID +"blaze",
            MCID +"creeper",MCID +"drowned",MCID +"guardian",MCID +"endermite",MCID +"ghast",MCID +"husk",
            MCID +"magma_cube",MCID +"phantom",MCID +"piglin",MCID +"piglin_brute",MCID +"pillager",MCID +"ravager",
            MCID +"shulker",MCID +"silverfish",MCID +"skeleton",MCID +"slime",MCID +"stray",
            MCID +"vex",MCID +"vindicator",MCID +"witch",MCID +"wither_skeleton",MCID +"zoglin",
            MCID +"zombie",MCID +"zombie_villager");

    public static final List<String> DEFAULT_MINIBOSSES = ImmutableList.of(MCID +"elder_guardian",MCID +"ravager",MCID +"evoker");

    public static final List<String> DEFAULT_BOSSES = ImmutableList.of(MCID +"wither",MCID +"ender_dragon");
}
