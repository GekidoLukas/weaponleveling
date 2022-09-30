package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.server.ServerEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {

    @Inject(
            method = "onDeath",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectEvent(DamageSource source, CallbackInfo ci) {
        ServerEvents.onLivingDeath(source,((Entity) ((Object)this)));
    }
}
