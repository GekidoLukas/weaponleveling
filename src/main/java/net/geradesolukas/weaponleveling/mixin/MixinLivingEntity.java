package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.event.LivingHurtCallback;
import net.geradesolukas.weaponleveling.server.ServerEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(
            method = "onDeath",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectEvent(DamageSource source, CallbackInfo ci) {
        ServerEvents.onLivingDeath(source,((Entity) ((Object)this)));
    }

    @Inject(
            method = "applyDamage",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/LivingEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectEventHurt(DamageSource source, float amount, CallbackInfo ci) {
        amount = ServerEvents.onLivingHurt(source,((Entity) ((Object)this)),amount);
        //LivingHurtCallback.EVENT.invoker().hurt(((LivingEntity) (Object) this), source, amount);
        //if (amount <= 0) return;
    }
}
