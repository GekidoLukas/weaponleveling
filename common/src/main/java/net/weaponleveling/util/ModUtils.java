package net.weaponleveling.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.api.event.ItemReplaceBrokenEvent;
import net.weaponleveling.api.registry.LevelingFunctionRegistry;
import net.weaponleveling.data.levelable_item.*;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.function.LevelingFunctions;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.data.levelable_item.type.LevelingTypes;
import net.weaponleveling.api.registry.LevelingTypeRegistry;
import net.weaponleveling.data.levelable_item.type.WornType;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
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
        if(stack.getTag() == null) return false;
        CompoundTag tag = stack.getTag().getCompound("levelable");
        return tag.contains("disabled") && tag.getBoolean("disabled");
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
                for(var levelingType : nbtLevelable.getTypes()) {
                    if(levelingType.equals(type) && extraCondition.test(levelingType)) {
                        return true;
                    }
                }
            }
        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            if(levelableitem.hasType(type)) {
                for(var levelingType : levelableitem.getTypes()) {
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
        if (nbtLevelable != null) return nbtLevelable.getMaxLevel();
        else if (isJSONLevelable(stack)) return levelableitem.getMaxLevel();
        else return WeaponLevelingConfig.max_item_level;

    }


    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.getLevelStartAmount();
        else if (isJSONLevelable(stack)) return levelableitem.getLevelStartAmount();
        else return WeaponLevelingConfig.starting_xp_amount;
    }

    public static LevelingFunction getLevelingFunction(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.getFunction();
        if (isJSONLevelable(stack)) return levelableitem.getFunction();
        return LevelingFunctions.LINEAR;
    }
    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.getHitXPAmount();
        else if (isJSONLevelable(stack)) return levelableitem.getHitXPAmount();
        else return WeaponLevelingConfig.hit_xp_chance;
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.getHitXPChance();
        else if (isJSONLevelable(stack)) return levelableitem.getHitXPChance();
        else return WeaponLevelingConfig.hit_xp_chance;
    }
    public static int getWornXPRNGModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        LevelableItem nbtLevelable = LevelableItem.fromNBT(stack);
        if (nbtLevelable != null) return nbtLevelable.getArmorXPRNGModifier();
        else if (isJSONLevelable(stack)) return levelableitem.getArmorXPRNGModifier();
        else return WeaponLevelingConfig.xp_apply_chance;
    }

    public static long getLevelProgress(ItemStack stack) {
        if(stack.hasTag()) {
            return stack.getOrCreateTag().getTagType("levelprogress") == Tag.TAG_LONG ? stack.getOrCreateTag().getLong("levelprogress") : (long) stack.getOrCreateTag().getInt("levelprogress");
        }
        return 0;
    }
    public static int getLevel(ItemStack stack) {
        if(stack.hasTag()) {
            return stack.getOrCreateTag().getInt("level");
        }
        return 0;
    }

    public static void updateLevelProgress(ItemStack stack, long amount) {
        stack.getOrCreateTag().putLong("levelprogress", amount);
    }
    public static void updateLevel(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt("level", amount);
    }
}
