package net.weaponleveling.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.weaponleveling.WeaponLevelingModClient;

public class WeaponLevelingModClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WeaponLevelingModClient.init();
    }
}
