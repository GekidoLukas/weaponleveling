package net.weaponleveling.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.weaponleveling.WeaponLevelingModClient;
import net.weaponleveling.forge.config.WeaponLevelingConfigForge;

@Mod(WeaponLevelingMod.MODID)
public class WeaponLevelingModForge {
    public WeaponLevelingModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(WeaponLevelingMod.MODID, FMLJavaModLoadingContext.get().getModEventBus());

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WeaponLevelingConfigForge.Server.SPEC, "weaponleveling-server.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WeaponLevelingConfigForge.Client.SPEC, "weaponleveling-client.toml");
        WeaponLevelingMod.init();

        if(Platform.getEnv() == Dist.CLIENT) {
            WeaponLevelingModClient.init();
        }
    }
}
