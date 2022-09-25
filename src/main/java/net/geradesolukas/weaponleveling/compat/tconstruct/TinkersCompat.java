package net.geradesolukas.weaponleveling.compat.tconstruct;

import net.geradesolukas.weaponleveling.compat.tetra.ItemUpgradeRegistryHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TinkersCompat {

    public static final String modId = "tconstruct";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static boolean isTinkersItem(ItemStack stack) {
        if(isLoaded) {
            return ItemChecks.isTinkersItem(stack);
        }
        return false;
    }
}
