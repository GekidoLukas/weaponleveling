package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TridentEntity.class)
public abstract class MixinTridentEntity {


    @Shadow private ItemStack tridentStack;

    @Inject(
            method = "onEntityHit",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/projectile/TridentEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(EntityHitResult entityHitResult, CallbackInfo ci, Entity entity, float f, Entity entity2, DamageSource damageSource, SoundEvent soundEvent, float g) {
        if(UpdateLevels.isAcceptedProjectileWeapon(tridentStack) && entity instanceof PlayerEntity) {
            UpdateLevels.applyXPOnItemStack(tridentStack, (PlayerEntity) entity, entity, false);
        }
    }


}
