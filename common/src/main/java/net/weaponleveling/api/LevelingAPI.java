package net.weaponleveling.api;

import com.google.common.collect.Multimap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.api.registry.LevelingTypeRegistry;
import net.weaponleveling.data.levelable_item.LevelableItem;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.levelable_item.function.LevelingFunction;
import net.weaponleveling.data.levelable_item.type.LevelingType;
import net.weaponleveling.util.AbstractArrowAccessor;
import net.weaponleveling.util.DataGetter;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.LevelingLogic;

import java.util.UUID;
import java.util.function.Predicate;

public class LevelingAPI {


    public static final UUID BASE_RANGED_DAMAGE_UUID = UUID.fromString("D52CA663-D6DA-4D85-9CA9-485E4F549499");


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
        return getMaxProgress(stack.getOrCreateTag().getInt("level"),stack);
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




    public static void modifyAttributeModifier(Multimap<Attribute, AttributeModifier> multimap, Attribute attribute, double amount) {
        if(multimap.get(attribute).stream().findFirst().isPresent()) {
            AttributeModifier modifier = multimap.get(attribute).stream().findFirst().get();

            if (modifier.getAmount() > 0) {
                AttributeModifier newModifier = new AttributeModifier(modifier.getId(),modifier.getName(),modifier.getAmount()+ amount,modifier.getOperation());
                multimap.remove(attribute,modifier);
                multimap.put(attribute,newModifier);
            }
        }
    }


    public static void referenceItemStackOnArrowEntity(AbstractArrow arrow, ItemStack source) {
        ((AbstractArrowAccessor) arrow).setSourceWeapon(source);
    }


    public static boolean isLevelableItem(ItemStack stack) {
        return ModUtils.isLevelableItem(stack);
    }

    public static boolean isNBTLevelable(ItemStack stack) {
        return ModUtils.isNBTLevelable(stack);
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
}
