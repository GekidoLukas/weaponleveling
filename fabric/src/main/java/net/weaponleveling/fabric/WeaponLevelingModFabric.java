package net.weaponleveling.fabric;


import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.fabricmc.api.ModInitializer;
import net.weaponleveling.fabric.config.WeaponLevelingConfigFabric;

public class WeaponLevelingModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ModLoadingContext.registerConfig(WeaponLevelingMod.MODID, ModConfig.Type.SERVER, WeaponLevelingConfigFabric.Server.SPEC, "weaponleveling-server.toml");
        ModLoadingContext.registerConfig(WeaponLevelingMod.MODID, ModConfig.Type.CLIENT, WeaponLevelingConfigFabric.Client.SPEC, "weaponleveling-client.toml");
        WeaponLevelingMod.init();
    }
}
