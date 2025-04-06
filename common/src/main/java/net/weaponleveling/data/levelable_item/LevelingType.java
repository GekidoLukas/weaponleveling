package net.weaponleveling.data.levelable_item;

public enum LevelingType {
    MELEE,
    RANGED,
    WEARING;


    public static LevelingType fromString(String name) {
        String upperCase = name.toUpperCase();
        if(upperCase.equals("RANGED")) return RANGED;
        if(upperCase.equals("WEARING")) return WEARING;

        return MELEE;
    }
}
