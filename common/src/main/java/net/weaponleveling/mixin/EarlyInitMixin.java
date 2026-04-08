package net.weaponleveling.mixin;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.server.Bootstrap;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.WeaponLevelingConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class EarlyInitMixin {
    private static boolean initialized = false;


    @Inject(method = "bootStrap", at = @At("HEAD"))
    private static void onBootstrap(CallbackInfo ci) {
        if(!initialized) {
            MidnightConfig.init("weaponleveling", WeaponLevelingConfig.class);
            EarlyConfig.init();
            initialized = true;
        }
    }
}
