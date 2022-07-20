package net.geradesolukas.weaponleveling.server;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ModLists {
    private static final String MCID = "minecraft:";

    public static final List<String> DEFAULT_ITEM_BLACKLIST = ImmutableList.of();
    public static final List<String> DEFAULT_ITEM_WHITELIST = ImmutableList.of(MCID +"trident",MCID +"bow",MCID +"crossbow");
    //
}
