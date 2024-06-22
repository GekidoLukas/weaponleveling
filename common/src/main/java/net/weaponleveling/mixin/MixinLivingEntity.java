package net.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(
            method = "die",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDie(DamageSource source, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        UpdateLevels.updateForKill(victim, source, null);
    }



    @Inject(
            method = "actuallyHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedHurt(DamageSource source, float damageamount, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        UpdateLevels.updateForHit(victim, source, false, null);
    }


    @Inject(
            method = "startUsingItem",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void preventAttack(InteractionHand interactionHand, CallbackInfo ci) {
        LivingEntity entity = ((LivingEntity) ((Object) this));
        if(ModUtils.isBroken(entity.getItemInHand(interactionHand))) {
            ci.cancel();
        }
    }
}
