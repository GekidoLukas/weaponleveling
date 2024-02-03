package net.weaponleveling.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ModUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphics.class)
public class MixinGuiGraphics {


    @ModifyExpressionValue(
            method = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"))
    private boolean injectBar(boolean original, Font font, ItemStack stack, int i, int j, @Nullable String string) {
        if (ModUtils.isBroken(stack)) {
            return original && !ModUtils.isBroken(stack);
        } else return original;
    }
}
