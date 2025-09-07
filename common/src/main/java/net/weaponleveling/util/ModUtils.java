package net.weaponleveling.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.data.levelable_item.*;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.data.levelable_item.type.LevelingTypes;
import net.weaponleveling.api.registry.LevelingTypeRegistry;
import net.weaponleveling.data.levelable_item.type.WornType;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModUtils {


    public static boolean isJSONLevelable(ItemStack stack) {
        return LevelableItemsLoader.isValid(stack.getItem()) && !stack.is(DataGetter.blacklist_items);
    }

    public static boolean isNBTLevelable(ItemStack stack) {
        if(stack.getTag() == null) return false;
        CompoundTag tag = stack.getTag().getCompound("levelable");

        return ((tag.contains("attributes", Tag.TAG_LIST)  && LevelableAttribute.hasValidInNBT(tag.getList("attributes",Tag.TAG_COMPOUND)))) //&& !tag.getList("attributes",Tag.TAG_COMPOUND).isEmpty()
                &&
                ((tag.contains("leveling_types",Tag.TAG_LIST) && LevelingTypeRegistry.hasValidInNBT(tag.getList("leveling_types",Tag.TAG_COMPOUND))))  //&& !tag.getList("leveling_types",Tag.TAG_COMPOUND).isEmpty())
                ;
    }

    public static boolean isLevelableItem(ItemStack stack) {
        return isJSONLevelable(stack) || isNBTLevelable(stack);// || isLevelableFallback(stack) || ;
    }



    public static boolean shouldBeUnbreakable(ItemStack stack) {
        AtomicBoolean isInTag = new AtomicBoolean(false);
        if(DataGetter.getLevelableAutoUnbreakable() && isLevelableItem(stack )&& !stack.is(DataGetter.non_vanish_items_blacklist)) isInTag.set(true);
        if(stack.is(DataGetter.non_vanish_items_whitelist) && !stack.is(DataGetter.non_vanish_items_blacklist)) isInTag.set(true);

        return isInTag.get();
    }

    public static boolean isMeleeLeveling(ItemStack stack) {
        boolean isLeveling = false;

        if(stack.getTag() != null && stack.getTag().contains("levelable")) {
            if(stack.getTag().getCompound("levelable").contains("leveling_types") && !stack.getTag().getCompound("levelable").getList("leveling_types", Tag.TAG_COMPOUND).isEmpty()) {
                for(Tag tag : stack.getTag().getCompound("levelable").getList("leveling_types", Tag.TAG_COMPOUND)) {
                    if(tag instanceof CompoundTag leveling_types) {
                        if(leveling_types.contains("type") && leveling_types.getString("type").equals("weaponleveling:melee")) {
                            isLeveling = true;
                        }
                    }
                }
            }

        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            if(levelableitem.hasType(LevelingTypes.MELEE)) {
                isLeveling = true;
            }
        }


        return isLeveling;
    }

    public static boolean isWornLeveling(ItemStack stack, EquipmentSlot slot) {
        boolean isLeveling = false;

        if(stack.getTag() != null && stack.getTag().contains("levelable")) {
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


        return isLeveling;
    }

    public static boolean isRangedLeveling(ItemStack stack) {
        boolean isLeveling = false;

        if(stack.getTag() != null && stack.getTag().contains("levelable")) {
            if(stack.getTag().getCompound("levelable").contains("leveling_types") && !stack.getTag().getCompound("levelable").getList("leveling_types", Tag.TAG_COMPOUND).isEmpty()) {
                for(Tag tag : stack.getTag().getCompound("levelable").getList("leveling_types", Tag.TAG_COMPOUND)) {
                    if(tag instanceof CompoundTag leveling_types) {
                        if(leveling_types.contains("type") && leveling_types.getString("type").equals("weaponleveling:ranged")) {
                            isLeveling = true;
                        }
                    }
                }
            }

        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            if(levelableitem.hasType(LevelingTypes.RANGED)) {
                isLeveling = true;
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
    public static int getLevelModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelModifier")) return stack.getTag().getCompound("levelable").getInt("levelModifier");
        else if (isJSONLevelable(stack)) return levelableitem.getLevelModifier();
        else return WeaponLevelingConfig.level_modifier;
    }
    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelStartAmount")) return stack.getTag().getCompound("levelable").getInt("levelStartAmount");
        else if (isJSONLevelable(stack)) return levelableitem.getLevelStartAmount();
        else return WeaponLevelingConfig.starting_xp_amount;
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
        else if (isJSONLevelable(stack)) return levelableitem.getXPRNGModifier();
        else return WeaponLevelingConfig.xp_apply_chance;
    }
}
