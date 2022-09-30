package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @ModifyExpressionValue(
            method = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"))
    private boolean injectBar(boolean original, TextRenderer renderer, ItemStack stack, int x, int y, @Nullable String countLabel) {
        if (ItemUtils.isBroken(stack)) {
            return original && !ItemUtils.isBroken(stack);
        } else return original;

    }

}
