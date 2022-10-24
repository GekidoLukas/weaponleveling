package net.geradesolukas.weaponleveling.util;

import net.geradesolukas.weaponleveling.data.LevelableItem;
import net.geradesolukas.weaponleveling.data.LevelableItemsLoader;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {

    public static boolean isBroken(ItemStack stack) {
        return stack.getDamageValue() >= (stack.getMaxDamage() + 1);
    }


    public static boolean isLevelableItem(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return LevelableItemsLoader.isValid(stack.getItem()) && !levelableitem.isDisabled();
    }


    public static boolean isAcceptedMeleeWeaponStack(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());

        return isLevelableItem(stack) && levelableitem.isMelee();
        //String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        //return (stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || WeaponLevelingConfig.Server.melee_items.get().contains(name)) && !WeaponLevelingConfig.Server.blacklist_items.get().contains(name) ;
    }

    public static boolean isAcceptedArmor(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());

        return isLevelableItem(stack) && levelableitem.isArmor();
        //String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        //return (stack.getItem() instanceof ArmorItem) && !WeaponLevelingConfig.Server.blacklist_items.get().contains(name);
    }

    public static boolean isAcceptedProjectileWeapon(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return isLevelableItem(stack) && levelableitem.isProjectile();
        //String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        //return (stack.getItem() instanceof ProjectileWeaponItem || (WeaponLevelingConfig.Server.projectile_items.get().contains(name))) && !WeaponLevelingConfig.Server.blacklist_items.get().contains(name);
    }



    public static int getMaxLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getMaxLevel();
    }
    public static int getLevelModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getLevelModifier();
    }
    public static int getLevelStartAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getLevelStartAmount();
    }


    public static int getHitXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getHitXPAmount();
    }
    public static int getHitXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getHitXPChance();
    }

    public static int getCritXPAmount(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getCritXPAmount();
    }
    public static int getCritXPChance(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getCritXPChance();
    }


    public static double getWeaponDamagePerLevel(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getWeaponDamagePerLevel();
    }
    public static double getBowlikeModifier(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getBowlikeModifier();
    }

    public static double getArmorMaxDamageReduction(ItemStack stack) {
        LevelableItem levelableitem = LevelableItemsLoader.get(stack.getItem().getRegistryName());
        return levelableitem.getArmorMaxDamageReduction();
    }
}
