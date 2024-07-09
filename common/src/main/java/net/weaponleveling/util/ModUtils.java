package net.weaponleveling.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.weaponleveling.data.LevelableItem;
import net.weaponleveling.data.LevelableItemsLoader;

import java.util.concurrent.atomic.AtomicBoolean;

public class ModUtils {



    
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

    public static void removeAttributeModifier(Multimap<Attribute, AttributeModifier> multimap, Attribute attribute) {
        if(multimap.get(attribute).stream().findFirst().isPresent()) {
            AttributeModifier modifier = multimap.get(attribute).stream().findFirst().get();
            AttributeModifier newmodifier = new AttributeModifier(modifier.getId(),modifier.getName(),0d,modifier.getOperation());
            multimap.remove(attribute,modifier);
            multimap.put(attribute,newmodifier);
        }
    }

    public static boolean isLevelableJSON(ItemStack stack) {
        return LevelableItemsLoader.isValid(stack.getItem());
    }

    public static boolean isLevelableFallback(ItemStack stack) {
        return isFallbackMelee(stack) || isFallbackProjectile(stack) || isFallbackArmor(stack);
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
        return isLevelableJSON(stack) || isLevelableFallback(stack) || isLevelableNBT(stack);
    }

    private static boolean isFallbackMelee(ItemStack stack) {
        boolean isClassItem = (stack.getItem() instanceof SwordItem ||
                stack.getItem() instanceof AxeItem) &&
                !DataGetter.getDisableUnlisted();

        return (isClassItem || stack.is(DataGetter.melee_items));
    }



    private static boolean isFallbackProjectile(ItemStack stack) {
        boolean isClassItem = (stack.getItem() instanceof ProjectileWeaponItem) &&
                !DataGetter.getDisableUnlisted();

        return (isClassItem || stack.is(DataGetter.projectile_items));
    }
    private static boolean isFallbackArmor(ItemStack stack) {
        boolean isClassItem = (stack.getItem() instanceof ArmorItem) &&
                !DataGetter.getDisableUnlisted();

        return (isClassItem || stack.is(DataGetter.armor_items));
    }

    public static boolean isBroken(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().getBoolean("isBroken");
    }

    public static boolean shouldBeUnbreakable(ItemStack stack) {
        AtomicBoolean isInTag = new AtomicBoolean(false);
        if(DataGetter.getLevelableAutoUnbreakable() && isLevelableItem(stack )&& !stack.is(DataGetter.non_vanish_items_blacklist)) isInTag.set(true);
        if(stack.is(DataGetter.non_vanish_items_whitelist) && !stack.is(DataGetter.non_vanish_items_blacklist)) isInTag.set(true);

//        BuiltInRegistries.ITEM.getTags().forEach(tagKeyNamedPair -> {
//            TagKey<Item> tagKey = tagKeyNamedPair.getFirst();
//            if(WeaponLevelingConfig.unbreakable_items_whitelist.contains("#" +tagKey.location().toString())) isInTag.set(true);
//            if(WeaponLevelingConfig.unbreakable_items_blacklist.contains("#" +tagKey.location().toString())) isInTag.set(false);
//        });
        return isInTag.get();
    }

    public static boolean isAcceptedMeleeWeaponStack(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isMelee")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isMelee");
        return ((isLevelableJSON(stack) && levelableitem.isMelee()) || (isFallbackMelee(stack)) || nbtadded) && !isDisabled(stack);
    }

    public static boolean isAcceptedArmor(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isArmor")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isArmor");
        return ((isLevelableJSON(stack) && levelableitem.isArmor()) || (isFallbackArmor(stack))|| nbtadded) && !isDisabled(stack);
    }

    public static boolean isAcceptedProjectileWeapon(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isProjectile")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isProjectile");
        return ((isLevelableJSON(stack) && levelableitem.isProjectile()) || (isFallbackProjectile(stack))|| nbtadded) && !isDisabled(stack);
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
        else return DataGetter.getMaxLevel();

    }
    public static int getLevelModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelModifier")) return stack.getTag().getCompound("levelable").getInt("levelModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getLevelModifier();
        else return DataGetter.getLevelModifier();
    }
    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelStartAmount")) return stack.getTag().getCompound("levelable").getInt("levelStartAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getLevelStartAmount();
        else return DataGetter.getStartingXp();
    }


    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPAmount")) return stack.getTag().getCompound("levelable").getInt("hitXPAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getHitXPAmount();
        else return DataGetter.getHitXpAmount();
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPChance")) return stack.getTag().getCompound("levelable").getInt("hitXPChance");
        else if (isLevelableJSON(stack)) return levelableitem.getHitXPChance();
        else return DataGetter.getHitPercentage();
    }

    public static int getCritXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("critXPAmount")) return stack.getTag().getCompound("levelable").getInt("critXPAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getCritXPAmount();
        else return DataGetter.getCritXpAmount();
    }
    public static int getCritXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("critXPChance")) return stack.getTag().getCompound("levelable").getInt("critXPChance");
        else if (isLevelableJSON(stack)) return levelableitem.getCritXPChance();
        else return DataGetter.getCritPercentage();
    }


    public static double getWeaponDamagePerLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("weaponDamagePerLevel")) return stack.getTag().getCompound("levelable").getDouble("weaponDamagePerLevel");
        else if (isLevelableJSON(stack)) return levelableitem.getWeaponDamagePerLevel();
        else return DataGetter.getDamagePerLevel();
    }
    public static double getArmorPerLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("armorPerLevel")) return stack.getTag().getCompound("levelable").getDouble("armorPerLevel");
        else if (isLevelableJSON(stack)) return levelableitem.getArmorPerLevel();
        else return DataGetter.getArmorPerLevel();
    }
    public static double getToughnessPerLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("toughnessPerLevel")) return stack.getTag().getCompound("levelable").getDouble("toughnessPerLevel");
        else if (isLevelableJSON(stack)) return levelableitem.getToughnessPerLevel();
        else return DataGetter.getToughnessPerLevel();
    }
    public static double getBowlikeModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("bowlikeModifier")) return stack.getTag().getCompound("levelable").getDouble("bowlikeModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getBowlikeModifier();
        else return DataGetter.getBowLikeModifier();
    }


    public static int getArmorXPRNGModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("armorXPRNGModifier")) return stack.getTag().getCompound("levelable").getInt("armorXPRNGModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getArmorXPRNGModifier();
        else return DataGetter.getArmorRng();
    }
}
