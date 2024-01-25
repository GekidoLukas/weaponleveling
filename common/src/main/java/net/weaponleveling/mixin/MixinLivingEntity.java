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
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void injectedHurt(DamageSource source, float damageamount, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        UpdateLevels.updateForHit(victim, source, false, null);
    }


    @WrapOperation( method = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    private void reduceDamage(LivingEntity instance, float originalcall, Operation<Float> original, DamageSource damageSource, float f) {
        original.call(instance, instance.getHealth() - UpdateLevels.reduceDamageArmor(instance,f));
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
