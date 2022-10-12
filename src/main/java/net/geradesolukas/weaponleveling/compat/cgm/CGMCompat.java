package net.geradesolukas.weaponleveling.compat.cgm;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CGMCompat {

    public static final String modId = "cgm";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static boolean isGunItem(ItemStack stack) {
        if(isLoaded) {
            return CGMChecks.isGunItem(stack);
        }
        return false;
    }
}
