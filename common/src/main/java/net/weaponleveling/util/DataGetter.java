package net.weaponleveling.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;

public class DataGetter {



    public static Boolean BROKEN_ITEMS_WONT_VANISH = null;
    public static boolean getBrokenItemsWontVanish() {
        if(BROKEN_ITEMS_WONT_VANISH == null) {
            return WeaponLevelingConfig.broken_items_wont_vanish;
        }
        return BROKEN_ITEMS_WONT_VANISH;
    }



    public static Boolean LEVELABLE_AUTO_UNBREAKABLE = null;
    public static Boolean getLevelableAutoUnbreakable() {
        if(LEVELABLE_AUTO_UNBREAKABLE == null) {
            return WeaponLevelingConfig.levelable_items_auto_unbreakable;
        }
        return LEVELABLE_AUTO_UNBREAKABLE;
    }



    public static final TagKey<Item> blacklist_items = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"blacklist_items"));
    public static final TagKey<Item> non_vanish_items_whitelist = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"non_vanish_items_whitelist"));
    public static final TagKey<Item> non_vanish_items_blacklist = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"non_vanish_items_blacklist"));

    public static final TagKey<EntityType<?>> entities_blacklist = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(WeaponLevelingMod.MODID,"entities_blacklist"));

}
