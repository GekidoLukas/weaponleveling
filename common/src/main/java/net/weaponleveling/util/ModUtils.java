package net.weaponleveling.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.data.levelable_item.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModUtils {

    public static final UUID BASE_RANGED_DAMAGE_UUID = UUID.fromString("D52CA663-D6DA-4D85-9CA9-485E4F549499");


    
    public static void modifyAttributeModifier(Multimap<Attribute, AttributeModifier> multimap, Attribute attribute, double amount) {
        if(multimap.get(attribute).stream().findFirst().isPresent()) {
            AttributeModifier modifier = multimap.get(attribute).stream().findFirst().get();

            if (modifier.getAmount() > 0) {
                AttributeModifier newmodifier = new AttributeModifier(modifier.getId(),modifier.getName(),modifier.getAmount()+ amount,modifier.getOperation());
                multimap.remove(attribute,modifier);
                multimap.put(attribute,newmodifier);
            }
        }
    }


    public static boolean isLevelableJSON(ItemStack stack) {
        return LevelableItemsLoader.isValid(stack.getItem());
    }


    private static boolean isLevelableNBT(ItemStack stack) {
        boolean isMelee = false;
        boolean isProjectile = false;
        boolean isArmor = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isMelee")) isMelee = stack.getTag().getCompound("levelable").getBoolean("isMelee");
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isProjectile")) isProjectile = stack.getTag().getCompound("levelable").getBoolean("isProjectile");
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isArmor")) isArmor = stack.getTag().getCompound("levelable").getBoolean("isArmor");

        return isMelee || isProjectile || isArmor;
    }

    public static boolean isLevelableItem(ItemStack stack) {
        return isLevelableJSON(stack);// || isLevelableFallback(stack) || isLevelableNBT(stack); //TODO Add NBT Override again
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
            if(stack.getTag().getCompound("levelable").contains("attributes") && !stack.getTag().getCompound("levelable").getList("attributes", Tag.TAG_COMPOUND).isEmpty()) {
                for(Tag tag : stack.getTag().getCompound("levelable").getList("attributes", Tag.TAG_COMPOUND)) {
                    if(tag instanceof CompoundTag attribute) {
                        if(NBTLevelableAttributeHelper.getLevelingType(attribute) == LevelingType.MELEE) {
                            isLeveling = true;
                        }
                    }
                }
            }

        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            for(LevelableAttribute attribute : levelableitem.getAttributes()) {
                if(attribute.getLevelingType() == LevelingType.MELEE) {
                    isLeveling = true;
                    break;
                }
            }
        }


        return isLeveling;
    }

    public static boolean isWornLeveling(ItemStack stack) {
        boolean isLeveling = false;

        if(stack.getTag() != null && stack.getTag().contains("levelable")) {
            if(stack.getTag().getCompound("levelable").contains("attributes") && !stack.getTag().getCompound("levelable").getList("attributes", Tag.TAG_COMPOUND).isEmpty()) {
                for(Tag tag : stack.getTag().getCompound("levelable").getList("attributes", Tag.TAG_COMPOUND)) {
                    if(tag instanceof CompoundTag attribute) {
                        if(NBTLevelableAttributeHelper.getLevelingType(attribute) == LevelingType.WEARING) {
                            isLeveling = true;
                        }
                    }
                }
            }

        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            for(LevelableAttribute attribute : levelableitem.getAttributes()) {
                if(attribute.getLevelingType() == LevelingType.WEARING) {
                    isLeveling = true;
                    break;
                }
            }
        }


        return isLeveling;
    }

    public static boolean isRangedLeveling(ItemStack stack) {
        boolean isLeveling = false;

        if(stack.getTag() != null && stack.getTag().contains("levelable")) {
            if(stack.getTag().getCompound("levelable").contains("attributes") && !stack.getTag().getCompound("levelable").getList("attributes", Tag.TAG_COMPOUND).isEmpty()) {
                for(Tag tag : stack.getTag().getCompound("levelable").getList("attributes", Tag.TAG_COMPOUND)) {
                    if(tag instanceof CompoundTag attribute) {
                        if(NBTLevelableAttributeHelper.getLevelingType(attribute) == LevelingType.RANGED) {
                            isLeveling = true;
                        }
                    }
                }
            }

        } else {
            LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            if(levelableitem == null) return false;
            for(LevelableAttribute attribute : levelableitem.getAttributes()) {
                if(attribute.getLevelingType() == LevelingType.RANGED) {
                    isLeveling = true;
                    break;
                }
            }
        }


        return isLeveling;
    }

    public static boolean isDisabled(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("disabled")) return stack.getTag().getCompound("levelable").getBoolean("disabled");
        if (LevelableItemsLoader.isValid(stack.getItem())) return stack.is(DataGetter.blacklist_items);
        else return stack.is(DataGetter.blacklist_items);

    }

    public static int getMaxLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("maxLevel")) return stack.getTag().getCompound("levelable").getInt("maxLevel");
        else if (isLevelableJSON(stack)) return levelableitem.getMaxLevel();
        else return WeaponLevelingConfig.max_item_level;

    }
    public static int getLevelModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelModifier")) return stack.getTag().getCompound("levelable").getInt("levelModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getLevelModifier();
        else return WeaponLevelingConfig.level_modifier;
    }
    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelStartAmount")) return stack.getTag().getCompound("levelable").getInt("levelStartAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getLevelStartAmount();
        else return WeaponLevelingConfig.starting_xp_amount;
    }


    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPAmount")) return stack.getTag().getCompound("levelable").getInt("hitXPAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getHitXPAmount();
        else return WeaponLevelingConfig.hit_xp_chance;
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPChance")) return stack.getTag().getCompound("levelable").getInt("hitXPChance");
        else if (isLevelableJSON(stack)) return levelableitem.getHitXPChance();
        else return WeaponLevelingConfig.hit_xp_chance;
    }








    public static int getWornXPRNGModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("armorXPRNGModifier")) return stack.getTag().getCompound("levelable").getInt("armorXPRNGModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getXPRNGModifier();
        else return WeaponLevelingConfig.xp_apply_chance;
    }
}
