package net.geradesolukas.weaponleveling.compat.cgm;

import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.world.item.ItemStack;

public class CGMChecks {

    public static boolean isGunItem(ItemStack stack) {
        if(CGMCompat.isLoaded) {
            if (stack.getItem() instanceof GunItem) {
                return true;
            } else return false;

        }

        return false;
    }

}
