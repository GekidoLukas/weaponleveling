package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrownTrident.class)
public abstract class MixinThrownTrident {
    @Shadow
    private ItemStack tridentItem;

    @Inject(
            method = "onHitEntity",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(EntityHitResult result, CallbackInfo ci, Entity entity, float f, Entity entity1, DamageSource damagesource, SoundEvent soundevent, float f1) {
        if(UpdateLevels.isAcceptedWeapon(tridentItem) && entity1 instanceof Player) {
            UpdateLevels.applyXPOnItemStack(tridentItem, (Player) entity1, entity);
        }
    }


}
