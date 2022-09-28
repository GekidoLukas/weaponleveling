package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @ModifyExpressionValue(
            method = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"))
    private boolean injectBar(boolean original, Font pFr, ItemStack pStack, int pXPosition, int pYPosition, @Nullable String pText) {
        if (ItemUtils.isBroken(pStack)) {
            return original && !ItemUtils.isBroken(pStack);
        } else return original;

    }

}
