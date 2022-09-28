package net.geradesolukas.weaponleveling.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@OnlyIn(Dist.CLIENT)
@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen extends Screen {
    protected MixinAbstractContainerScreen(Component pTitle) {
        super(pTitle);
    }

    @OnlyIn(Dist.CLIENT)
    @Inject(
            method = "renderSlot",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderAndDecorateItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectRenderer(PoseStack pPoseStack, Slot pSlot, CallbackInfo ci, int i, int j, ItemStack itemstack) {
        if (ItemUtils.isBroken(itemstack)) {
            this.itemRenderer.renderAndDecorateFakeItem(new ItemStack(Items.BARRIER),i,j);
            RenderSystem.setShaderColor(0.0f,0.0f,0.0f,0.0f);
        }
    }

}
