package net.weaponleveling.data.levelable_item.type;

import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.api.registry.LevelingTypeRegistry;

public class LevelingTypes {



    public static final EmptyType EMPTY = new EmptyType();
    public static final MeleeType MELEE = new MeleeType();
    public static final RangedType RANGED = new RangedType();
    public static final WornType WORN = new WornType();


    public static void register() {
        LevelingTypeRegistry.register(WeaponLevelingMod.id("empty"),EMPTY);
        LevelingTypeRegistry.register(WeaponLevelingMod.id("melee"),MELEE);
        LevelingTypeRegistry.register(WeaponLevelingMod.id("ranged"),RANGED);
        LevelingTypeRegistry.register(WeaponLevelingMod.id("worn"),WORN);
    }

}
