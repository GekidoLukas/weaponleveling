package net.weaponleveling.data.levelable_item;

import net.minecraft.nbt.CompoundTag;

public class NBTLevelableAttributeHelper {


    public static LevelingType getLevelingType(CompoundTag tag) {
        if(tag.contains("levelingType")) {
            String upperCase = tag.getString("levelingType").toUpperCase();
            if(upperCase.equals("RANGED")) return LevelingType.RANGED;
            if(upperCase.equals("WEARING")) return LevelingType.WEARING;
        }

        return LevelingType.MELEE;
    }
}
