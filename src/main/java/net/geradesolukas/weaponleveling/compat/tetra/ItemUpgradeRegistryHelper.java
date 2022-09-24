package net.geradesolukas.weaponleveling.compat.tetra;

import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.module.ItemUpgradeRegistry;

import java.util.Optional;

public class ItemUpgradeRegistryHelper {

    public static void init() {
        if(TetraCompat.isLoaded) {
            ItemUpgradeRegistry.instance.registerReplacementHook((ItemStack original, ItemStack replacement) -> {
                replacement.getOrCreateTag().putInt("level", original.getOrCreateTag().getInt("level"));
                replacement.getOrCreateTag().putInt("levelprogress", original.getOrCreateTag().getInt("levelprogress"));
                return replacement;
            });
        }
    }


}
