package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ForgeHooks.class)
public abstract class MixinForgeHooks {

    @ModifyReturnValue(
            method = "getContainerItem",
            at = @At(value = "RETURN"),remap = false)
    private static ItemStack injectedDamage(ItemStack original, ItemStack stack) {
        if (WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) return stack;
        else return original;
    }
}
