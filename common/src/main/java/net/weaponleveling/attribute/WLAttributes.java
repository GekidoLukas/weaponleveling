package net.weaponleveling.attribute;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.item.BrokenItem;

public class WLAttributes {


//    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(WeaponLevelingMod.MODID, Registries.ATTRIBUTE);

    public static final Attribute RANGED_DAMAGE =  Registry.register(BuiltInRegistries.ATTRIBUTE,"weaponleveling:ranged_damage", new RangedAttribute("attribute.name.generic.ranged_damage", 2.0, 0.0, 2048.0).setSyncable(true));

    public static void register() {

//        ATTRIBUTES.register();

    }
}
