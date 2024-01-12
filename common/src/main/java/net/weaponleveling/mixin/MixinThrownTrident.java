package net.weaponleveling.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.weaponleveling.util.ItemUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrownTrident.class)
public class MixinThrownTrident {
    @Shadow
    private ItemStack tridentItem;

    @Inject(
            method = "onHitEntity",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedLevel(EntityHitResult result, CallbackInfo ci, Entity entity, float f, Entity entity1, DamageSource source, SoundEvent soundevent, float f1) {
        if(entity instanceof LivingEntity living) {
            UpdateLevels.updateForHit(living, source, false, tridentItem);
            if(!living.isAlive()) {
                UpdateLevels.updateForKill(living, source, tridentItem);
            }
        }
    }

    @ModifyArg(
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    public float hurtModify(float f) {
        if(ItemUtils.isAcceptedProjectileWeapon(tridentItem)) {
            double weaponlevelamount = tridentItem.getOrCreateTag().getInt("level");
            weaponlevelamount *= ItemUtils.getWeaponDamagePerLevel(tridentItem);
            return f + (float) weaponlevelamount;
        }
        return f;
    }

}
