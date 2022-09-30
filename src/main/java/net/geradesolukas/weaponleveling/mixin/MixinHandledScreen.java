package net.geradesolukas.weaponleveling.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public class MixinHandledScreen extends Screen {


    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Inject(
            method = "drawSlot",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/render/item/ItemRenderer;renderInGuiWithOverrides(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectRenderer(MatrixStack matrices, Slot slot, CallbackInfo ci, int i, int j, ItemStack itemStack, boolean bl, boolean bl2, ItemStack itemStack2, String string) {
        if (ItemUtils.isBroken(itemStack)) {
            this.itemRenderer.renderInGuiWithOverrides(new ItemStack(Items.BARRIER),i,j);
            RenderSystem.setShaderColor(0.0f,0.0f,0.0f,0.0f);
        }
    }

}
