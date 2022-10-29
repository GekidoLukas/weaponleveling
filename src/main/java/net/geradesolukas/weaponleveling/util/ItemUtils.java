package net.geradesolukas.weaponleveling.util;

import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.data.LevelableItem;
import net.geradesolukas.weaponleveling.data.LevelableItemsLoader;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemUtils {

    public static boolean isBroken(ItemStack stack) {
        return stack.getDamageValue() >= (stack.getMaxDamage() + 1);
    }


    public static boolean isLevelableJSON(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
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
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        boolean isClassItem = (stack.getItem() instanceof SwordItem ||
                stack.getItem() instanceof AxeItem) &&
                !WeaponLevelingConfig.Server.disable_unlisted_items.get();

        return (isClassItem || WeaponLevelingConfig.Server.melee_items.get().contains(name));
    }



    private static boolean isFallbackProjectile(ItemStack stack) {
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        boolean isClassItem = (stack.getItem() instanceof ProjectileWeaponItem) &&
                !WeaponLevelingConfig.Server.disable_unlisted_items.get();

        return (isClassItem || (WeaponLevelingConfig.Server.projectile_items.get().contains(name)));
    }
    private static boolean isFallbackArmor(ItemStack stack) {
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        boolean isClassItem = (stack.getItem() instanceof ArmorItem) &&
                !WeaponLevelingConfig.Server.disable_unlisted_items.get();

        return (isClassItem || (WeaponLevelingConfig.Server.armor_items.get().contains(name)));
    }

    public static boolean isAcceptedMeleeWeaponStack(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isMelee")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isMelee");
        return ((isLevelableJSON(stack) && levelableitem.isMelee()) || (isFallbackMelee(stack)) || nbtadded) && !isDisabled(stack);
    }

    public static boolean isAcceptedArmor(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isArmor")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isArmor");
        return ((isLevelableJSON(stack) && levelableitem.isArmor()) || (isFallbackArmor(stack))|| nbtadded) && !isDisabled(stack);
    }

    public static boolean isAcceptedProjectileWeapon(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        boolean nbtadded = false;
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("isProjectile")) nbtadded = stack.getTag().getCompound("levelable").getBoolean("isProjectile");
        return ((isLevelableJSON(stack) && levelableitem.isProjectile()) || (isFallbackProjectile(stack))|| nbtadded) && !isDisabled(stack);
    }

    public static boolean isDisabled(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("disabled")) return stack.getTag().getCompound("levelable").getBoolean("disabled");
        if (LevelableItemsLoader.isValid(stack.getItem())) return levelableitem.isDisabled() || WeaponLevelingConfig.Server.blacklist_items.get().contains(name);
        else return WeaponLevelingConfig.Server.blacklist_items.get().contains(name);

    }

    public static int getMaxLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("maxLevel")) return stack.getTag().getCompound("levelable").getInt("maxLevel");
        else if (isLevelableJSON(stack)) return levelableitem.getMaxLevel();
        else return WeaponLevelingConfig.Server.value_max_level.get();

    }
    public static int getLevelModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelModifier")) return stack.getTag().getCompound("levelable").getInt("levelModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getLevelModifier();
        else return WeaponLevelingConfig.Server.value_level_modifier.get();
    }
    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("levelStartAmount")) return stack.getTag().getCompound("levelable").getInt("levelStartAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getLevelStartAmount();
        else return WeaponLevelingConfig.Server.value_starting_level_amount.get();
    }


    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPAmount")) return stack.getTag().getCompound("levelable").getInt("hitXPAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getHitXPAmount();
        else return WeaponLevelingConfig.Server.value_hit_xp_amount.get();
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("hitXPChance")) return stack.getTag().getCompound("levelable").getInt("hitXPChance");
        else if (isLevelableJSON(stack)) return levelableitem.getHitXPChance();
        else return WeaponLevelingConfig.Server.value_hit_percentage.get();
    }

    public static int getCritXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("critXPAmount")) return stack.getTag().getCompound("levelable").getInt("critXPAmount");
        else if (isLevelableJSON(stack)) return levelableitem.getCritXPAmount();
        else return WeaponLevelingConfig.Server.value_crit_xp_amount.get();
    }
    public static int getCritXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("critXPChance")) return stack.getTag().getCompound("levelable").getInt("critXPChance");
        else if (isLevelableJSON(stack)) return levelableitem.getCritXPChance();
        else return WeaponLevelingConfig.Server.value_crit_percentage.get();
    }


    public static double getWeaponDamagePerLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("weaponDamagePerLevel")) return stack.getTag().getCompound("levelable").getDouble("weaponDamagePerLevel");
        else if (isLevelableJSON(stack)) return levelableitem.getWeaponDamagePerLevel();
        else return WeaponLevelingConfig.Server.value_damage_per_level.get();
    }
    public static double getBowlikeModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("bowlikeModifier")) return stack.getTag().getCompound("levelable").getDouble("bowlikeModifier");
        else if (isLevelableJSON(stack)) return levelableitem.getBowlikeModifier();
        else return WeaponLevelingConfig.Server.value_bowlike_damage_modifier.get();
    }

    public static double getArmorMaxDamageReduction(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (stack.getTag() != null && stack.getTag().getCompound("levelable").contains("armorMaxDamageReduction")) return stack.getTag().getCompound("levelable").getDouble("armorMaxDamageReduction");
        else if (isLevelableJSON(stack)) return levelableitem.getArmorMaxDamageReduction();
        else return WeaponLevelingConfig.Server.value_max_damage_reduction.get();
    }
}
