package net.weaponleveling.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.weaponleveling.util.LevelingLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class MixinPlayerFabric {

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z", ordinal = 1, shift = At.Shift.AFTER))
    private void injectedCritXP(Entity victim, CallbackInfo ci, @Local DamageSource source, @Local(ordinal = 0) boolean crit) {
        if(victim instanceof LivingEntity living) {
            LevelingLogic.updateForHit(living, source, crit, null);
        }
    }
}
