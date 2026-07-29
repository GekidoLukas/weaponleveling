package net.weaponleveling.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.api.event.ItemReplaceBrokenEvent;
import net.weaponleveling.data.levelable_item.*;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.data.levelable_item.type.LevelingTypes;
import net.weaponleveling.data.levelable_item.type.WornType;
import net.weaponleveling.item.component.ItemLevelData;
import net.weaponleveling.item.component.WLDataComponents;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class ModUtils {


    public static boolean isJSONLevelable(ItemStack stack) {
        return LevelableItemsLoader.isValid(stack.getItem()) && !stack.is(DataGetter.blacklist_items) && !isNBTDisabled(stack);
    }

    public static boolean isLevelableItem(ItemStack stack) {
        return isJSONLevelable(stack) || LevelableItem.fromNBT(stack) != null;
    }

    public static LevelableItem getLevelableItem(ItemStack stack) {
        LevelableItem fromNBT = LevelableItem.fromNBT(stack);

        return fromNBT != null ? fromNBT : LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
    }


    public static boolean isNBTDisabled(ItemStack stack) {
        if(!stack.has(WLDataComponents.DISABLE_LEVELING.get())) return false;
        return Boolean.TRUE.equals(stack.get(WLDataComponents.DISABLE_LEVELING.get()));
    }

    public static boolean shouldBeUnbreakable(ItemStack stack) {
        AtomicBoolean isInTag = new AtomicBoolean(false);
        if(DataGetter.getLevelableAutoUnbreakable() && isLevelableItem(stack )&& !stack.is(DataGetter.non_vanish_items_blacklist)) isInTag.set(true);
        if(stack.is(DataGetter.non_vanish_items_whitelist) && !stack.is(DataGetter.non_vanish_items_blacklist)) isInTag.set(true);

        if(isInTag.get()) {
            return !ItemReplaceBrokenEvent.PRE.invoker().pre(stack).isFalse();
        } else {
            return false;
        }

    }

    public static boolean isMeleeLeveling(ItemStack stack) {
        return isLevelingAsType(stack,LevelingTypes.MELEE);
    }

    public static boolean isWornLeveling(ItemStack stack, EquipmentSlot slot) {
        return isLevelingAsType(stack,LevelingTypes.WORN,levelingType -> {
            return levelingType instanceof WornType wornType && wornType.getSlots().contains(slot);
        });
    }

    public static boolean isRangedLeveling(ItemStack stack) {
        return isLevelingAsType(stack,LevelingTypes.RANGED);
    }

    public static boolean isLevelingAsType(ItemStack stack, LevelingType type) {
        return isLevelingAsType(stack,type,t -> true);
    }

    public static boolean isLevelingAsType(ItemStack stack, LevelingType type, Predicate<LevelingType> extraCondition) {

        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if(nbtLevelable != null) {
            if(nbtLevelable.hasType(type)) {
                for(var levelingType : nbtLevelable.types()) {
                    if(levelingType.equals(type) && extraCondition.test(levelingType)) {
                        return true;
                    }
                }
            }
        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            if(levelableitem.hasType(type)) {
                for(var levelingType : levelableitem.types()) {
                    if(levelingType.equals(type) && extraCondition.test(levelingType)) {
                        return true;
                    }
                }
            }
        }



        return false;
    }



    public static int getMaxLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.maxLevel();
        else if (isJSONLevelable(stack)) return levelableitem.maxLevel();
        else return WeaponLevelingConfig.max_item_level;

    }


    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.levelStartAmount();
        else if (isJSONLevelable(stack)) return levelableitem.levelStartAmount();
        else return WeaponLevelingConfig.starting_xp_amount;
    }

    public static LevelingFunction getLevelingFunction(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.function();
        if (isJSONLevelable(stack)) return levelableitem.function();
        return LevelingFunctions.LINEAR;
    }
    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.hitXPAmount();
        else if (isJSONLevelable(stack)) return levelableitem.hitXPAmount();
        else return WeaponLevelingConfig.hit_xp_chance;
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.hitXPChance();
        else if (isJSONLevelable(stack)) return levelableitem.hitXPChance();
        else return WeaponLevelingConfig.hit_xp_chance;
    }
    public static int getWornXPRNGModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.wornMinXPPercentage();
        else if (isJSONLevelable(stack)) return levelableitem.wornMinXPPercentage();
        else return WeaponLevelingConfig.xp_apply_chance;
    }

    public static long getLevelProgress(ItemStack stack) {
        if(stack.has(WLDataComponents.ITEM_LEVEL_DATA.get())) {
            return stack.get(WLDataComponents.ITEM_LEVEL_DATA.get()).levelprogress();
        }
        return 0;
    }
    public static int getLevel(ItemStack stack) {
        if(stack.has(WLDataComponents.ITEM_LEVEL_DATA.get())) {
            return stack.get(WLDataComponents.ITEM_LEVEL_DATA.get()).level();
        }
        return 0;
    }

    public static void updateLevelProgress(ItemStack stack, long amount) {
        int level = getLevel(stack);
        stack.set(WLDataComponents.ITEM_LEVEL_DATA.get(),ItemLevelData.create(level,amount));
    }
    public static void updateLevel(ItemStack stack, int amount) {
        long levelprogress = getLevelProgress(stack);
        stack.set(WLDataComponents.ITEM_LEVEL_DATA.get(),ItemLevelData.create(amount,levelprogress));
    }
}
