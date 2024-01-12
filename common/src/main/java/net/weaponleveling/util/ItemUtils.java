package net.weaponleveling.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.data.LevelableItem;
import net.weaponleveling.data.LevelableItemsLoader;

import java.util.concurrent.atomic.AtomicBoolean;

public class ItemUtils {

    public static void modifyAttributeModifier(Multimap<Attribute, AttributeModifier> multimap, Attribute attribute, double amount) {
        if(multimap.get(attribute).stream().findFirst().isPresent()) {
            AttributeModifier modifier = multimap.get(attribute).stream().findFirst().get();
            AttributeModifier newmodifier = new AttributeModifier(modifier.getId(),modifier.getName(),modifier.getAmount()+ amount,modifier.getOperation());
            multimap.remove(attribute,modifier);
            multimap.put(attribute,newmodifier);
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
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        return LevelableItemsLoader.isValid(stack.getItem()) && !levelableitem.isDisabled();
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
        String name = Registry.ITEM.getKey(stack.getItem()).toString();
        boolean isClassItem = (stack.getItem() instanceof SwordItem ||
                stack.getItem() instanceof AxeItem) &&
                !WLPlatformGetter.getDisableUnlistedItems();

        return (isClassItem || WLPlatformGetter.getMeleeItems().contains(name));
    }



    private static boolean isFallbackProjectile(ItemStack stack) {
        String name = Registry.ITEM.getKey(stack.getItem()).toString();
        boolean isClassItem = (stack.getItem() instanceof ProjectileWeaponItem) &&
                !WLPlatformGetter.getDisableUnlistedItems();

        return (isClassItem || (WLPlatformGetter.getProjectedItems().contains(name)));
    }
    private static boolean isFallbackArmor(ItemStack stack) {
        String name = Registry.ITEM.getKey(stack.getItem()).toString();
        boolean isClassItem = (stack.getItem() instanceof ArmorItem) &&
                !WLPlatformGetter.getDisableUnlistedItems();

        return (isClassItem || (WLPlatformGetter.getArmorItems().contains(name)));
    }

    public static boolean isBroken(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().getBoolean("isBroken");
    }

    public static boolean shouldBeUnbreakable(ItemStack stack) {
        String name = Registry.ITEM.getKey(stack.getItem()).toString();
        AtomicBoolean isInTag = new AtomicBoolean(false);
        if(WLPlatformGetter.getLevelableIsUnbreakable() && isLevelableItem(stack )&& !WLPlatformGetter.getUnbreakableBlacklist().contains(name)) isInTag.set(true);
        if(WLPlatformGetter.getUnbreakableWhitelist().contains(name) && !WLPlatformGetter.getUnbreakableBlacklist().contains(name)) isInTag.set(true);
        Registry.ITEM.getTags().forEach(tagKeyNamedPair -> {
            TagKey<Item> tagKey = tagKeyNamedPair.getFirst();
            if(WLPlatformGetter.getUnbreakableWhitelist().contains("#" +tagKey.location().toString())) isInTag.set(true);
            if(WLPlatformGetter.getUnbreakableBlacklist().contains("#" +tagKey.location().toString())) isInTag.set(false);
        });
        return isInTag.get();
    }

    public static boolean isAcceptedMeleeWeaponStack(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isMelee")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isMelee");
        return ((isLevelableJSON(stack) && levelableitem.isMelee()) || (isFallbackMelee(stack)) || nbtadded) && !isDisabled(stack);
    }

    public static boolean isAcceptedArmor(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isArmor")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isArmor");
        return ((isLevelableJSON(stack) && levelableitem.isArmor()) || (isFallbackArmor(stack))|| nbtadded) && !isDisabled(stack);
    }

    public static boolean isAcceptedProjectileWeapon(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isProjectile")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isProjectile");
        return ((isLevelableJSON(stack) && levelableitem.isProjectile()) || (isFallbackProjectile(stack))|| nbtadded) && !isDisabled(stack);
    }

    public static boolean isDisabled(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        String name = Registry.ITEM.getKey(stack.getItem()).toString();
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("disabled")) return stack.getTag().getCompound("levelable").getBoolean("disabled");
        if (LevelableItemsLoader.isValid(stack.getItem())) return levelableitem.isDisabled() ||  WLPlatformGetter.getBlacklistedItems().contains(name);
        else return WLPlatformGetter.getBlacklistedItems().contains(name);

    }

    public static int getMaxLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("maxLevel")) return stack.getTag().getCompound("levelable").getInt("maxLevel");
        else if (isLevelableJSON(stack)) return levelableitem.getMaxLevel();
        else return WLPlatformGetter.getMaxLevel();

    }
    public static int getLevelModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelModifier")) return stack.getTag().getCompound("levelable").getInt("levelModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getLevelModifier();
        else return WLPlatformGetter.getLevelModifier();
    }
    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelStartAmount")) return stack.getTag().getCompound("levelable").getInt("levelStartAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getLevelStartAmount();
        else return WLPlatformGetter.getStartingLevelAmount();
    }


    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPAmount")) return stack.getTag().getCompound("levelable").getInt("hitXPAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getHitXPAmount();
        else return WLPlatformGetter.getHitXPAmount();
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPChance")) return stack.getTag().getCompound("levelable").getInt("hitXPChance");
        else if (isLevelableJSON(stack)) return levelableitem.getHitXPChance();
        else return WLPlatformGetter.getHitXPPercentage();
    }

    public static int getCritXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("critXPAmount")) return stack.getTag().getCompound("levelable").getInt("critXPAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getCritXPAmount();
        else return WLPlatformGetter.getCritXPAmount();
    }
    public static int getCritXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("critXPChance")) return stack.getTag().getCompound("levelable").getInt("critXPChance");
        else if (isLevelableJSON(stack)) return levelableitem.getCritXPChance();
        else return WLPlatformGetter.getCritXPPercentage();
    }


    public static double getWeaponDamagePerLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("weaponDamagePerLevel")) return stack.getTag().getCompound("levelable").getDouble("weaponDamagePerLevel");
        else if (isLevelableJSON(stack)) return levelableitem.getWeaponDamagePerLevel();
        else return WLPlatformGetter.getDamagePerLevel();
    }
    public static double getBowlikeModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("bowlikeModifier")) return stack.getTag().getCompound("levelable").getDouble("bowlikeModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getBowlikeModifier();
        else return WLPlatformGetter.getBowlikeModifier();
    }

    public static double getArmorMaxDamageReduction(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("armorMaxDamageReduction")) return stack.getTag().getCompound("levelable").getDouble("armorMaxDamageReduction");
        else if (isLevelableJSON(stack)) return levelableitem.getArmorMaxDamageReduction();
        else return WLPlatformGetter.getMaxDamageReduction();
    }

    public static int getArmorXPRNGModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(Registry.ITEM.getKey(stack.getItem()));
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("armorXPRNGModifier")) return stack.getTag().getCompound("levelable").getInt("armorXPRNGModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getArmorXPRNGModifier();
        else return WLPlatformGetter.getArmorXPRNGModifier();
    }
}
