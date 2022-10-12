package net.geradesolukas.weaponleveling.compat.tconstruct;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

public class TinkersChecks {

    public static boolean isTinkersItem(ItemStack stack) {
        if(TinkersCompat.isLoaded) {
            if (stack.getItem() instanceof ModifiableItem) {
                return true;
            } else return false;

        }

        return false;
    }

}
