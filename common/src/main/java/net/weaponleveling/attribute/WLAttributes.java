package net.weaponleveling.attribute;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.item.BrokenItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class WLAttributes {


    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(WeaponLevelingMod.MODID, Registries.ATTRIBUTE);

    public static final Holder<Attribute> RANGED_DAMAGE = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE,WeaponLevelingMod.id("ranged_damage"),new RangedAttribute("generic.ranged_damage", 2.0, 0.0, 2048.0).setSyncable(true));



    public static void register() {

    }



    public static boolean isGreenAttribute(Holder<Attribute> attribute) {
        return attribute.equals(RANGED_DAMAGE) || attribute.equals(Attributes.ATTACK_DAMAGE) || attribute.equals(Attributes.ATTACK_SPEED);
    }
}
