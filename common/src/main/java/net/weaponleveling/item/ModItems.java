package net.weaponleveling.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingMod;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(WeaponLevelingMod.MODID, Registries.ITEM);


    public static final RegistrySupplier<Item> BROKEN_ITEM = ITEMS.register("broken", () -> new BrokenItem(new BrokenItem.Properties().stacksTo(1)));


    public static void register() {

        ITEMS.register();

    }
}
