package net.geradesolukas.weaponleveling.util;

import net.minecraft.world.item.ItemStack;

public class ItemUtils {

    public static boolean isBroken(ItemStack stack) {
        return stack.getDamageValue() >= (stack.getMaxDamage() + 1);
    }
}
