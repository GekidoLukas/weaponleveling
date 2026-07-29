package net.weaponleveling.neoforge.mixin;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.LevelingLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public class MixinPlayerNeoForge {

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedCritXP(Entity victim, CallbackInfo ci, float f, ItemStack itemstack, DamageSource damagesource, float f1, float f2, boolean flag4, boolean crit) {
        LivingEntity attacker = ((LivingEntity) ((Object) this));
        if(victim instanceof LivingEntity living) {
            DamageSource source = living.damageSources().playerAttack((Player) attacker);
            LevelingLogic.updateForHit(living, source, crit, null);
        }
    }
}
