package net.weaponleveling.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WLConfigGetter;
import net.weaponleveling.util.ItemUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.weaponleveling.util.UpdateLevels.updateProgressItem;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

    @Inject(
            method = "die",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void injectedDie(DamageSource source, CallbackInfo ci) {
        ServerPlayer victim = ((ServerPlayer) ((Object) this));
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
}
