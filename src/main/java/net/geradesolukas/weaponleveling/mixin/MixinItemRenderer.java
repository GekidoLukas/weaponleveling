package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Shadow @Final private ItemColors itemColors;

    @ModifyExpressionValue(
            method = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"))
    private boolean injectBar(boolean original, Font pFr, ItemStack pStack, int pXPosition, int pYPosition, @Nullable String pText) {
        if (ItemUtils.isBroken(pStack)) {
            return original && !ItemUtils.isBroken(pStack);
        } else return original;

    }

    //@Inject(
    //        method = "renderQuadList",
    //        at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/renderer/block/model/BakedQuad;isTinted()Z"),
    //        locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //private void injectRenderer(PoseStack pPoseStack, VertexConsumer pBuffer, List<BakedQuad> pQuads, ItemStack pItemStack, int pCombinedLight, int pCombinedOverlay, CallbackInfo ci, boolean flag, PoseStack.Pose posestack$pose, Iterator var9, BakedQuad bakedquad, int i) {
    //    if (ItemUtils.isBroken(pItemStack)) {
    //        i = itemColors.getColor(pItemStack,bakedquad.getTintIndex()) -1;
    //    }
    //}

    @Inject(
            method = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At(value = "INVOKE",  target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectRenderer(ItemStack pStack, int pX, int pY, BakedModel pBakedModel, CallbackInfo ci) {
        if (ItemUtils.isBroken(pStack)) {

        }
    }

}
