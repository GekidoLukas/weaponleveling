package net.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WLConfigGetter;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.server.ServerEvents;
import net.weaponleveling.util.ItemUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.weaponleveling.util.UpdateLevels.updateProgressItem;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(
            method = "die",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void injectedDie(DamageSource source, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        Entity killer = source.getEntity();
        if (source.isExplosion()) ci.cancel();
        if (source.isMagic()) ci.cancel();

        if (killer instanceof Player player) {
            ItemStack stack = WLConfigGetter.getAttackItem(player);
            if(ItemUtils.isAcceptedMeleeWeaponStack(stack) ) {
                int xpamount = UpdateLevels.getXPForEntity(victim);
                updateProgressItem(player,stack,xpamount);
            }

            int xpamount = UpdateLevels.getXPForEntity(victim);

            if (source.isProjectile() && ItemUtils.isAcceptedProjectileWeapon(stack)) {
                updateProgressItem(player, stack, xpamount);
            }
            UpdateLevels.applyXPForArmor(player,xpamount);
        }

    }



    @Inject(
            method = "actuallyHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void injectedHurt(DamageSource source, float damageamount, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        Entity killer = source.getEntity();
        if(source.isProjectile() && killer instanceof Player player) {
            ItemStack hand = WLConfigGetter.getAttackItem(player);
            int xpamount = 0;
            int amount = UpdateLevels.getXPForHit(hand);
            if (UpdateLevels.shouldGiveHitXP(ItemUtils.getCritXPChance(hand))) {xpamount = amount;}
            updateProgressItem(player, hand, xpamount);
        }

        //damageamount = UpdateLevels.reduceDamageArmor(victim,damageamount);

        //amount = ServerEvents.onLivingHurt(source,((Entity) ((Object)this)),amount);

    }

    //@ModifyExpressionValue(
    //        method = "getDamageAfterArmorAbsorb",
    //        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;isBypassArmor()Z"))
    //private boolean passByArmor(boolean original) {
    //    LivingEntity livingEntity = ((LivingEntity) ((Object) this));
    //    return original;
    //}

    @WrapOperation( method = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    private void reduceDamage(LivingEntity instance, float originalcall, Operation<Float> original, DamageSource damageSource, float f) {
        //Player player = ((Player) ((Object)this));
        //WeaponLevelingMod.LOGGER.info("Damage reduced: "+ UpdateLevels.reduceDamageArmor(instance,f)+ "; Original: " + f);

        original.call(instance, instance.getHealth() - UpdateLevels.reduceDamageArmor(instance,f));
    }

    @Inject(
            method = "startUsingItem",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void preventAttack(InteractionHand interactionHand, CallbackInfo ci) {
        LivingEntity entity = ((LivingEntity) ((Object) this));
        if(ItemUtils.isBroken(entity.getItemInHand(interactionHand))) {
            ci.cancel();
        }
    }
}
