package net.weaponleveling.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.WeaponLevelingModClient;
import net.weaponleveling.fabric.config.WeaponLevelingConfigFabric;

public class WeaponLevelingModClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WeaponLevelingModClient.init();
    }
}
