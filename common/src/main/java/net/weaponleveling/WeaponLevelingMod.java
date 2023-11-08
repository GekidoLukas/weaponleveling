package net.weaponleveling;

import com.google.common.base.Suppliers;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.data.LevelableItemsLoader;
import net.weaponleveling.networking.Networking;
import net.weaponleveling.server.command.ItemLevelCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class WeaponLevelingMod {
    public static final String MODID = "weaponleveling";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MODID));
    // Registering a new creative tab
    //public static final CreativeModeTab EXAMPLE_TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "example_tab"), () ->
    //        new ItemStack(WeaponLevelingMod.EXAMPLE_ITEM.get()));
    //
    //public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
    //public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () ->
    //        new Item(new Item.Properties().tab(WeaponLevelingMod.EXAMPLE_TAB)));
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static void init() {
        //ITEMS.register();
        CommandRegistrationEvent.EVENT.register(ItemLevelCommand::register);


        Networking.registerC2SPackets();
        //System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
        ReloadListenerRegistry.register(PackType.SERVER_DATA,LevelableItemsLoader.INSTANCE);
    }


    // X PROJECTILEWEAPONS - X Datapack system - X Generalize Armor and Weapon level to Item Level in tooltip - X applying xp - X Better multi-levelup/levelup - mod compats
    // X Critical Damage XP - X Tax Free Armor XP
    //Forge Compats: X BetterCombat ; X CGM ; X Tinkers ; X Parry This ; XAlex's mobs ; X Tetra
    //Fabric Compats: X BetterCombat ;
    //Bugs: X Tinkers no hit nor crit xp with Hand Axe, Mattock | X Crossbows and bows no extra damage
    //X Armor damage reduction
    //TODO Tag lists loading after levelable items
    //XEntities always show up as boss because I use all of the type tags existing instead of the entities' type tags
}
