package net.weaponleveling.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    @Inject(method = "renderItem",
            at = @At("HEAD"), cancellable = true)
    private void dontRenderItem(LivingEntity livingEntity, ItemStack stack, ItemDisplayContext itemDisplayContext,
                                boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if(ModUtils.isBroken(stack)) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(
            method = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0))
    private static boolean modifyPose(boolean original, AbstractClientPlayer abstractClientPlayer, float f, float g,
                                      InteractionHand interactionHand, float h, ItemStack stack,
                                      float i, PoseStack poseStack, MultiBufferSource multiBufferSource, int j) {
        return original || ModUtils.isBroken(stack);
    }
}
