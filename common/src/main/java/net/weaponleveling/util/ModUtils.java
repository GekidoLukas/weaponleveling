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

    public static boolean isNBTLevelable(ItemStack stack) {
        if(stack.getTag() == null) return false;
        CompoundTag tag = stack.getTag().getCompound("levelable");
        if(isNBTDisabled(stack)) return false;

        return ((tag.contains("attributes", Tag.TAG_LIST)  && LevelableAttribute.hasValidInNBT(tag.getList("attributes",Tag.TAG_COMPOUND)))) //&& !tag.getList("attributes",Tag.TAG_COMPOUND).isEmpty()
                &&
                ((tag.contains("leveling_types",Tag.TAG_LIST) && LevelingTypeRegistry.hasValidInNBT(tag.getList("leveling_types",Tag.TAG_COMPOUND))))  //&& !tag.getList("leveling_types",Tag.TAG_COMPOUND).isEmpty())
                ;
    }

    public static boolean isLevelableItem(ItemStack stack) {
        return isJSONLevelable(stack) || isNBTLevelable(stack);
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
        boolean isLeveling = false;

        if(stack.getTag() != null && isNBTLevelable(stack)) {
            if(stack.getTag().getCompound("levelable").contains("leveling_types") && !stack.getTag().getCompound("levelable").getList("leveling_types", Tag.TAG_COMPOUND).isEmpty()) {
                for(Tag tag : stack.getTag().getCompound("levelable").getList("leveling_types", Tag.TAG_COMPOUND)) {
                    if(tag instanceof CompoundTag leveling_types && leveling_types.contains("type")) {
                        LevelingType levelingType = LevelingTypeRegistry.fromNBT(leveling_types);
                        if(levelingType instanceof WornType wornType && wornType.getSlots().contains(slot)) {
                            isLeveling = true;
                            break;
                        }
                    }
                }
            }

        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            if(levelableitem.hasType(LevelingTypes.WORN)) {
                for(var type : levelableitem.getTypes()) {
                    if(type instanceof WornType wornType && wornType.getSlots().contains(slot)) {
                        isLeveling = true;
                        break;
                    }
                }
            }
        }


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
        boolean isLeveling = false;

        if(stack.getTag() != null && isNBTLevelable(stack)) {
            CompoundTag levelableTag = stack.getTag().getCompound("levelable");

            if(levelableTag.contains("leveling_types") && !levelableTag.getList("leveling_types", Tag.TAG_COMPOUND).isEmpty()) {
                for(Tag tag : levelableTag.getList("leveling_types", Tag.TAG_COMPOUND)) {
                    if(tag instanceof CompoundTag leveling_type) {
                        LevelingType levelingType = LevelingTypeRegistry.fromNBT(leveling_type);
                        if(levelingType.equals(type) && extraCondition.test(levelingType)) {
                            isLeveling = true;
                        }
                    }
                }
            }

        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            if(levelableitem.hasType(type)) {
                for(var levelingType : levelableitem.getTypes()) {
                    if(levelingType.equals(type) && extraCondition.test(levelingType)) {
                        isLeveling = true;
                        break;
                    }
                }
            }
        }



        return isLeveling;
    }



    public static int getMaxLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("maxLevel")) return stack.getTag().getCompound("levelable").getInt("maxLevel");
        else if (isJSONLevelable(stack)) return levelableitem.getMaxLevel();
        else return WeaponLevelingConfig.max_item_level;

    }


    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelStartAmount")) return stack.getTag().getCompound("levelable").getInt("levelStartAmount");
        else if (isJSONLevelable(stack)) return levelableitem.getLevelStartAmount();
        else return WeaponLevelingConfig.starting_xp_amount;
    }

    public static LevelingFunction getLevelingFunction(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));

        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("leveling_function")) {
            CompoundTag functionTag = stack.getTag().getCompound("levelable").getCompound("leveling_function");
            LevelingFunction levelingFunction = LevelingFunctionRegistry.fromNBT(functionTag);
            if(levelingFunction != null) {
                return levelingFunction;
            }
        }
        if (isJSONLevelable(stack)) return levelableitem.getFunction();
        return LevelingFunctions.LINEAR;
    }
    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPAmount")) return stack.getTag().getCompound("levelable").getInt("hitXPAmount");
        else if (isJSONLevelable(stack)) return levelableitem.getHitXPAmount();
        else return WeaponLevelingConfig.hit_xp_chance;
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPChance")) return stack.getTag().getCompound("levelable").getInt("hitXPChance");
        else if (isJSONLevelable(stack)) return levelableitem.getHitXPChance();
        else return WeaponLevelingConfig.hit_xp_chance;
    }
    public static int getWornXPRNGModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("armorXPRNGModifier")) return stack.getTag().getCompound("levelable").getInt("armorXPRNGModifier");
        else if (isJSONLevelable(stack)) return levelableitem.getArmorXPRNGModifier();
        else return WeaponLevelingConfig.xp_apply_chance;
    }
}
