package net.geradesolukas.weaponleveling.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.ItemRenderer;
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

@OnlyIn(Dist.CLIENT)
@Mixin(Gui.class)
public class MixinGui {


    @Shadow @Final protected ItemRenderer itemRenderer;
    @OnlyIn(Dist.CLIENT)
    @Inject(
            method = "renderSlot",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderAndDecorateItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectRenderer(int pX, int pY, float pPartialTick, Player pPlayer, ItemStack pStack, int int1, CallbackInfo ci, PoseStack poseStack) {
        if (ItemUtils.isBroken(pStack)) {
            this.itemRenderer.renderAndDecorateFakeItem(new ItemStack(Items.BARRIER),pX,pY);
            RenderSystem.setShaderColor(0.0f,0.0f,0.0f,1.0f);
        }
    }
}
