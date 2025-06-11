package net.weaponleveling.api;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.LevelingLogic;

import java.util.UUID;

public class LevelingAPI {


    public static final UUID BASE_RANGED_DAMAGE_UUID = UUID.fromString("D52CA663-D6DA-4D85-9CA9-485E4F549499"); //TODO Make it only apply if config is correct


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
    public static int getMaxProgress(ItemStack stack) {
        return getMaxProgress(stack.getOrCreateTag().getInt("level"),stack);

    }

    /**
     * @param currentLevel The current level the item is supposed to have (Can be different from the NBT's level)
     * @param stack The ItemStack the level is queried from
     * @return The amount of XP an Item needs to level up to the next Level
     */
    public static int getMaxProgress(int currentLevel,ItemStack stack) {
        int maxProgress;
        int levelModifier = ModUtils.getLevelModifier(stack);
        int startingLevel =  ModUtils.getLevelStartAmount(stack);

        if (currentLevel != 0) {
            maxProgress = startingLevel + ((currentLevel - 1) + currentLevel) * levelModifier;
        } else {
            maxProgress = startingLevel;
        }
        return maxProgress;

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

}
