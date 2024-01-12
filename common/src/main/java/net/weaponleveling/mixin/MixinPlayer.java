package net.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.util.ItemUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.weaponleveling.util.UpdateLevels.updateProgressItem;

@Mixin(Player.class)
public class MixinPlayer {


    @Inject(
            method = "attack",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedXP(Entity target, CallbackInfo ci) {
        //Player player = ((Player) ((Object) this));
        //ItemStack stack = WLPlatformGetter.getAttackItem(player);
        //if(ItemUtils.isAcceptedMeleeWeaponStack(stack)) {
        //    UpdateLevels.applyXPOnItemStack(stack,  player, target, false);
        //    UpdateLevels.applyXPForArmorItem(player, false, stack);
        //}
    }


    @Inject(
            method = "die",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDie(DamageSource source, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        UpdateLevels.updateForKill(victim, source, null);
    }


    //todo check wtf this is
    @Inject(
            method = "actuallyHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void injectedHurt(DamageSource source, float damageamount, CallbackInfo ci) {
        //WeaponLevelingMod.LOGGER.info("this fires for Player");
        //Player victim = ((Player) ((Object) this));
        //Entity killer = source.getEntity();
        //if(source.isProjectile() && killer instanceof Player player) {
        //    ItemStack hand = WLPlatformGetter.getAttackItem(player);
        //    int xpamount = 0;
        //    int amount = UpdateLevels.getXPForHit(hand);
        //    if (UpdateLevels.shouldGiveHitXP(ItemUtils.getCritXPChance(hand))) {xpamount = amount;}
        //    updateProgressItem(player, hand, xpamount);
        //}
    }


    @Inject(
            method = "blockActionRestricted",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void preventBlockTarget(Level level, BlockPos blockPos, GameType gameType, CallbackInfoReturnable<Boolean> cir) {
        Player player = ((Player) ((Object) this));
        if(ItemUtils.isBroken(WLPlatformGetter.getAttackItem(player))) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "attack",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void preventAttack(Entity target, CallbackInfo ci) {
        Player player = ((Player) ((Object) this));
        if(ItemUtils.isBroken(WLPlatformGetter.getAttackItem(player))) {
            ci.cancel();
        }
    }

    @Inject(
            method = "interactOn",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void preventInteract(Entity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = ((Player) ((Object) this));
        if(ItemUtils.isBroken(player.getMainHandItem())) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean preventCooldown(boolean original) {
        Player player = ((Player) ((Object) this));
        return original || ItemUtils.isBroken(WLPlatformGetter.getAttackItem(player));
    }






    @WrapOperation( method = "Lnet/minecraft/world/entity/player/Player;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setHealth(F)V"))
    private void reduceDamage(Player instance,float originalcall, Operation<Float> original, DamageSource damageSource, float f) {
        original.call(instance, instance.getHealth() - UpdateLevels.reduceDamageArmor(instance,f));
    }


}
