package net.geradesolukas.weaponleveling.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class MixinInGameHud {


    @Shadow @Final protected ItemRenderer itemRenderer;

    @Inject(
            method = "renderHotbarItem",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/render/item/ItemRenderer;renderInGuiWithOverrides(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectRenderer(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci, MatrixStack matrixStack) {
        if (ItemUtils.isBroken(stack)) {
            this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.BARRIER),x,y);
            RenderSystem.setShaderColor(0.0f,0.0f,0.0f,1.0f);
        }
    }
}
