package net.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/world/inventory/InventoryMenu$1")
public class MixinInventoryMenu {


    @ModifyExpressionValue(
            method = "mayPickup",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;hasBindingCurse(Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean bindingPickup(boolean original) {
        ItemStack stack = ((Slot) ((Object) this)).getItem();
        if(ItemUtils.isBroken(stack)) return false;
        else return original;
    }

}
