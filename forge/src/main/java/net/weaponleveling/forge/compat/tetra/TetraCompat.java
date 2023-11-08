package net.weaponleveling.forge.compat.tetra;

import net.minecraftforge.fml.ModList;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TetraCompat {
    public static final String modId = "tetra";
    public static final Boolean isLoaded = ModList.get().isLoaded(modId);

    public static void init() {
        if(isLoaded) {
            ItemUpgradeRegistryHelper.init();
        }
    }
}
