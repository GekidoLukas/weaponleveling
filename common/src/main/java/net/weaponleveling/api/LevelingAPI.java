package net.weaponleveling.api;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.LevelableAttribute;
import net.weaponleveling.data.levelable_item.LevelableItem;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.util.AbstractArrowAccessor;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.LevelingLogic;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.Predicate;

public class LevelingAPI {


    public static final ResourceLocation BASE_RANGED_DAMAGE_ID = ResourceLocation.withDefaultNamespace("base_ranged_damage");


    /**
     * This Method allows you to apply XP to an Item in General.
     * It bypasses events like {@link net.weaponleveling.api.event.HitXPGainEvent HitXPGainEvent} or {@link net.weaponleveling.api.event.KillXPGainEvent KillXPGainEvent}
     * @param player The player using the item.
     * @param stack The ItemStack xp needs to be applied to.
     * @param amount The amount of xp points added to the item
     */
    public static void applyXPToItem(Player player, ItemStack stack, int amount) {
        LevelingLogic.updateProgressItem(player,stack,amount);
    }


    /**
     * @param stack The ItemStack the level is queried from
     * @return The amount of XP an Item needs to level up to the next Level
     */
    public static long getMaxProgress(ItemStack stack) {
        return getMaxProgress(getLevel(stack),stack);
    }

    /**
     * @param currentLevel The current level the item is supposed to have (Can be different from the NBT's level)
     * @param stack The ItemStack the level is queried from
     * @return The amount of XP an Item needs to level up to the next Level
     */
    public static long getMaxProgress(int currentLevel, ItemStack stack) {
        int startingPoints =  ModUtils.getLevelStartAmount(stack);
        LevelingFunction function = ModUtils.getLevelingFunction(stack);

        return function.calculateProgress(currentLevel,startingPoints);
    }

    /**
     * @param stack the ItemStack that needs to be checked
     * @return the current level of an item
     */
    public static long getLevelProgress(ItemStack stack) {
        return ModUtils.getLevelProgress(stack);
    }

    /**
     * @param stack the ItemStack that needs to be checked
     * @return the current level progress of an item
     */
    public static int getLevel(ItemStack stack) {
        return ModUtils.getLevel(stack);
    }

    /**
     * Only use that if you want to modify the NBT/Data Component directly. In all other cases use {@link LevelingAPI#applyXPToItem}
     * @param stack the ItemStack that is to be modified
     */
    @ApiStatus.Internal
    public static void updateLevelProgress(ItemStack stack, long amount) {
        ModUtils.updateLevelProgress(stack,amount);
    }

    /**
     * Only use that if you want to modify the NBT/Data Component directly. In all other cases use {@link LevelingAPI#applyXPToItem}
     * @param stack the ItemStack that is to be modified
     */
    @ApiStatus.Internal
    public static void updateLevel(ItemStack stack, int amount) {
        ModUtils.updateLevel(stack,amount);
    }



    @Deprecated
    public static void modifyAttributeModifier(Multimap<Attribute, AttributeModifier> multimap, Attribute attribute, double amount) {
        WeaponLevelingMod.LOGGER.error("Cannot modify Attribute, deprecated API is used! Check Changelog!");
    }

    @Deprecated
    public static void modifyAttributeModifier(List<ItemAttributeModifiers.Entry> entries, Holder<Attribute> attribute, int level, LevelableAttribute levelableAttribute, EquipmentSlotGroup equipmentSlot) {
        WeaponLevelingMod.LOGGER.error("Cannot modify Attribute, deprecated API is used! Check Changelog!");
    }

    public static double calculateModifierAmount(double originalAmount, int level, LevelableAttribute levelableAttribute) {
        double percentOfOriginal = originalAmount * levelableAttribute.valuePerLevel();
        return levelableAttribute.isPercent() ? percentOfOriginal * level: levelableAttribute.valuePerLevel() * level;
    }

    @Deprecated
    public static void setAttributeModifierAmount(List<ItemAttributeModifiers.Entry> entries, Holder<Attribute> attribute, ResourceLocation modifierID, double amount) {
        WeaponLevelingMod.LOGGER.error("Cannot set Attribute, deprecated API is used! Check Changelog!");
    }

    public static void referenceItemStackOnArrowEntity(AbstractArrow arrow, ItemStack source) {
        ((AbstractArrowAccessor) arrow).setSourceWeapon(source);
    }


    public static boolean isLevelableItem(ItemStack stack) {
        return ModUtils.isLevelableItem(stack);
    }

    public static boolean isNBTLevelable(ItemStack stack) {
        return LevelableItem.fromNBT(stack) != null;
    }

    public static boolean isJSONLevelable(ItemStack stack) {
        return ModUtils.isJSONLevelable(stack);
    }

    public static boolean isNBTDisabled(ItemStack stack) {
        return ModUtils.isNBTDisabled(stack);
    }

    public static boolean isLevelingAsType(ItemStack stack, LevelingType type) {
        return ModUtils.isLevelingAsType(stack,type);
    }

    public static boolean isLevelingAsType(ItemStack stack, LevelingType type, Predicate<LevelingType> extraCondition) {
        return ModUtils.isLevelingAsType(stack,type,extraCondition);
    }

    public static LevelableItem getLevelableItem(ItemStack stack) {
        return ModUtils.getLevelableItem(stack);
    }
}
