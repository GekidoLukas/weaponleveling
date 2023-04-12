package net.weaponleveling.forge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.weaponleveling.util.ItemUtils;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "com.mrcrayfish.guns.common.Gun")
public abstract class MixinGun {


    @ModifyReturnValue(
            method = "Lcom/mrcrayfish/guns/common/Gun;getAdditionalDamage(Lnet/minecraft/world/item/ItemStack;)F",
            at = @At(value = "RETURN"))
    private static float injectedDamage(float original, ItemStack stack) {
        if(ItemUtils.isAcceptedProjectileWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateTag().getInt("level");
            weaponlevelamount *= ItemUtils.getWeaponDamagePerLevel(stack);
            return original += weaponlevelamount;
        }
        return original;
    }

    //@ModifyReturnValue(
    //        method = "Lcom/mrcrayfish/guns/util/GunModifierHelper;getModifiedProjectileDamage(Lnet/minecraft/world/item/ItemStack;F)F",
    //        at = @At(value = "RETURN"))
    //private static float injectedDamage(float original, ItemStack stack, float damage) {
    //    if(ItemUtils.isAcceptedProjectileWeapon(stack)) {
    //        double weaponlevelamount = stack.getOrCreateTag().getInt("level");
    //        weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get();
    //        return original += weaponlevelamount;
    //    }
    //    return original;
    //}

    //@Inject(
    //        method = "Lcom/mrcrayfish/guns/util/GunModifierHelper;getModifiedProjectileDamage(Lnet/minecraft/world/item/ItemStack;F)F",
    //        at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //private static void injectedDamage(ItemStack stack, float damage, CallbackInfoReturnable<Float> cir) {
    //    if(ItemUtils.isAcceptedMeleeWeaponStack(stack) && !TinkersCompat.isTinkersItem(stack)) {
    //        float weaponlevelamount = stack.getOrCreateTag().getInt("level");
    //        weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get();
//
    //        damage += weaponlevelamount;
    //    }
    //}

}
