package net.weaponleveling.forge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ItemUtils;
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
}
