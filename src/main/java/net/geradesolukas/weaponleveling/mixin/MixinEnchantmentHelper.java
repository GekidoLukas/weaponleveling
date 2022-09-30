package net.geradesolukas.weaponleveling.mixin;


import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {

    @Inject(
            method = "getAttackDamage",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/enchantment/EnchantmentHelper;forEachEnchantment(Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;Lnet/minecraft/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void injectedDamage(ItemStack stack, EntityGroup entityGroup, CallbackInfoReturnable<Float> cir, MutableFloat mutablefloat) {
         float weaponlevelamount = stack.getOrCreateNbt().getInt("level");
        weaponlevelamount *= WeaponlevelingConfig.Server.value_damage_per_level.get();
        if(UpdateLevels.isAcceptedMeleeWeaponStack(stack)) {
            mutablefloat.add(weaponlevelamount);
        }
    }
}
