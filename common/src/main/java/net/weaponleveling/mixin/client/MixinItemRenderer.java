package net.weaponleveling.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {


    @ModifyVariable(method = "render",
            at = @At(value = "HEAD"), index = 6, argsOnly = true)
    public int useSummonerStaffModel(int value, ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel) {
        if (ModUtils.isBroken(itemStack) && itemDisplayContext == ItemDisplayContext.GUI) {
            return 15;
        }
        return value;
    }


}
