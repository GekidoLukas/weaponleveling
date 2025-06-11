package net.weaponleveling.util;

import net.minecraft.world.item.ItemStack;

public interface AbstractArrowAccessor {


    ItemStack getSourceWeapon();
    void setSourceWeapon(ItemStack stack);
}
