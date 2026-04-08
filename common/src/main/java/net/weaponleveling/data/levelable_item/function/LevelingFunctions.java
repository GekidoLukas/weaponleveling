package net.weaponleveling.data.levelable_item.function;

import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.api.registry.LevelingFunctionRegistry;
import net.weaponleveling.api.registry.LevelingTypeRegistry;
import net.weaponleveling.data.levelable_item.type.EmptyType;
import net.weaponleveling.data.levelable_item.type.MeleeType;
import net.weaponleveling.data.levelable_item.type.RangedType;
import net.weaponleveling.data.levelable_item.type.WornType;

public class LevelingFunctions {


    public static final LinearFunction LINEAR = new LinearFunction();
    public static final QuadraticFunction QUADRATIC = new QuadraticFunction();
    public static final ExponentialFunction EXPONENTIAL = new ExponentialFunction();



    public static void register() {
        LevelingFunctionRegistry.register(WeaponLevelingMod.id("linear"),LINEAR);
        LevelingFunctionRegistry.register(WeaponLevelingMod.id("quadratic"),QUADRATIC);
        LevelingFunctionRegistry.register(WeaponLevelingMod.id("exponential"),EXPONENTIAL);
    }
}
