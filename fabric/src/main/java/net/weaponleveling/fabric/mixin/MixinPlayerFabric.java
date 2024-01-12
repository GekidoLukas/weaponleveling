package net.weaponleveling.fabric.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public class MixinPlayerFabric {

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedCritXP(Entity victim, CallbackInfo ci, float f, float g, boolean bl, boolean bl2, int i, boolean crit) {
        LivingEntity attacker = ((LivingEntity) ((Object) this));
        DamageSource source = new EntityDamageSource("player", attacker);
        if(victim instanceof LivingEntity living) {
            UpdateLevels.updateForHit(living, source, crit, null);
        }
    }
}
