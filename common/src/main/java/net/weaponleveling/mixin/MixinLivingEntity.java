package net.weaponleveling.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WLConfigGetter;
import net.weaponleveling.server.ServerEvents;
import net.weaponleveling.util.ItemUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
            int amount = ItemUtils.getHitXPAmount(hand);
            if (UpdateLevels.shouldGiveHitXP(ItemUtils.getCritXPChance(hand))) {xpamount = amount;}
            updateProgressItem(player, hand, xpamount);
        }
        //amount = ServerEvents.onLivingHurt(source,((Entity) ((Object)this)),amount);

    }
}
