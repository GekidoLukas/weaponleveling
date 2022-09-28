package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Accessor(value = "itemColors")
    public abstract ItemColors getItemColors();

    //@WrapOperation(
    //        method = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V",
    //        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"))
    //private void injectRenderer(float pRed, float pGreen, float pBlue, float pAlpha, Operation<Void> original, ItemStack pStack, int pX, int pY, BakedModel pBakedModel) {
    //    if (ItemUtils.isBroken(pStack)) {
    //        original.call( 0.0f, pGreen, pBlue, pAlpha);
    //    }
    //    else original.call( pRed, pGreen, pBlue, pAlpha);
    //}

    //@WrapOperation(
    //        method = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderQuadList(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Ljava/util/List;Lnet/minecraft/world/item/ItemStack;II)V",
    //        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/item/ItemColors;getColor(Lnet/minecraft/world/item/ItemStack;I)I"))
    //private int injectRenderer(ItemColors instance ,ItemStack pStack, int tint, Operation<Integer> original) {
    //    if (ItemUtils.isBroken(pStack)) {
    //        return original.call(instance, pStack, 2829099);
    //    }
    //    else return original.call(instance, pStack, tint);
    //}

    @ModifyExpressionValue(
            method = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"))
    private boolean injectBar(boolean original, Font pFr, ItemStack pStack, int pXPosition, int pYPosition, @Nullable String pText) {
        if (ItemUtils.isBroken(pStack)) {
            return original && !ItemUtils.isBroken(pStack);
        } else return original;

    }

}
