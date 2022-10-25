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

    public static boolean isLevelableItem(ItemStack stack) {
        return isLevelableJSON(stack) || isLevelableFallback(stack);
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
        return ((isLevelableJSON(stack) && levelableitem.isMelee()) || (isFallbackMelee(stack))) && !isDisabled(stack);
    }

    public static boolean isAcceptedArmor(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return ((isLevelableJSON(stack) && levelableitem.isArmor()) || (isFallbackArmor(stack))) && !isDisabled(stack);
    }

    public static boolean isAcceptedProjectileWeapon(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return ((isLevelableJSON(stack) && levelableitem.isProjectile()) || (isFallbackProjectile(stack))) && !isDisabled(stack);
    }

    public static boolean isDisabled(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        if (LevelableItemsLoader.isValid(stack.getItem())) return levelableitem.isDisabled() || WeaponLevelingConfig.Server.blacklist_items.get().contains(name);
        else return WeaponLevelingConfig.Server.blacklist_items.get().contains(name);

    }

    public static int getMaxLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getMaxLevel();
        else return WeaponLevelingConfig.Server.value_max_level.get();

    }
    public static int getLevelModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getLevelModifier();
        else return WeaponLevelingConfig.Server.value_level_modifier.get();
    }
    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getLevelStartAmount();
        else return WeaponLevelingConfig.Server.value_starting_level_amount.get();
    }


    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getHitXPAmount();
        else return WeaponLevelingConfig.Server.value_hit_xp_amount.get();
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getHitXPChance();
        else return WeaponLevelingConfig.Server.value_hit_percentage.get();
    }

    public static int getCritXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getCritXPAmount();
        else return WeaponLevelingConfig.Server.value_crit_xp_amount.get();
    }
    public static int getCritXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getCritXPChance();
        else return WeaponLevelingConfig.Server.value_crit_percentage.get();
    }


    public static double getWeaponDamagePerLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getWeaponDamagePerLevel();
        else return WeaponLevelingConfig.Server.value_damage_per_level.get();
    }
    public static double getBowlikeModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getBowlikeModifier();
        else return WeaponLevelingConfig.Server.value_bowlike_damage_modifier.get();
    }

    public static double getArmorMaxDamageReduction(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        if (isLevelableJSON(stack)) return levelableitem.getArmorMaxDamageReduction();
        else return WeaponLevelingConfig.Server.value_max_damage_reduction.get();
    }
}
