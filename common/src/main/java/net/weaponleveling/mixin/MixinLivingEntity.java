package net.weaponleveling.mixin;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.util.LevelingLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {


    @Inject(
            method = "createLivingAttributes",
            at = @At(value = "RETURN"))
    private static void addAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
//        MidnightConfig.init(WeaponLevelingMod.MODID, WeaponLevelingConfig.class);
        if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
            cir.getReturnValue().add(WLAttributes.RANGED_DAMAGE);
        }
    }

    @Inject(
            method = "die",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDie(DamageSource source, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        LevelingLogic.updateForKill(victim, source, null);
    }



    @Inject(
            method = "actuallyHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedHurt(DamageSource source, float damageamount, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        LevelingLogic.updateForHit(victim, source, false, null);
    }





}
