package net.weaponleveling.forge.mixin;

import net.bettercombat.api.WeaponAttributes;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Pseudo
@Mixin(targets = "net.bettercombat.logic.WeaponRegistry")
public class MixinWeaponRegistry {

    @Inject(
            method = "Lnet/bettercombat/logic/WeaponRegistry;getAttributes(Lnet/minecraft/world/item/ItemStack;)Lnet/bettercombat/api/WeaponAttributes;",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private static void preventAttackAnimation(ItemStack itemStack, CallbackInfoReturnable<WeaponAttributes> cir) {
        if(ItemUtils.isBroken(itemStack)) {
            cir.setReturnValue(null);
        }
    }
}
