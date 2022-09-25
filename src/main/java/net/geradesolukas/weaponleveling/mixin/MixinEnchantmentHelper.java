package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.compat.tconstruct.TinkersCompat;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {

    @Inject(
            method = "getDamageBonus",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;runIterationOnItem(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/world/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void injectedDamage(ItemStack stack, MobType p_44835_, CallbackInfoReturnable<Float> cir, MutableFloat mutablefloat) {
         float weaponlevelamount = stack.getOrCreateTag().getInt("level");
        weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get();
        if(UpdateLevels.isAcceptedMeleeWeaponStack(stack) && !TinkersCompat.isTinkersItem(stack)) {
            mutablefloat.add(weaponlevelamount);
        }
    }
}
