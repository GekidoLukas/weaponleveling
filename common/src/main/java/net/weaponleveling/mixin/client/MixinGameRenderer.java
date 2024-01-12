package net.weaponleveling.mixin.client;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.weaponleveling.util.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {


    @Inject(
            method = "shouldRenderBlockOutline",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void preventRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        GameRenderer renderer = ((GameRenderer) ((Object) this));
        Entity entity = renderer.getMinecraft().getCameraEntity();
        if(entity instanceof Player player) {
            if(ItemUtils.isBroken(player.getMainHandItem())) {
                cir.setReturnValue(false);
            }
        }

    }
}
