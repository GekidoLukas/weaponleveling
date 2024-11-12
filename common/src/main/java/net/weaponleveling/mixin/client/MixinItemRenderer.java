package net.weaponleveling.mixin.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.item.BrokenItem;
import net.weaponleveling.item.ModItems;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {


    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "render",
            at = @At(value = "HEAD"), index = 6, argsOnly = true)
    public int useBrokenTint(int value, ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel) {
        if (itemStack.is(ModItems.BROKEN_ITEM.get()) && itemDisplayContext == ItemDisplayContext.GUI) {
            return 15;
        }
        return value;
    }


    @ModifyVariable(method = "render",
            at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useContainedModel(BakedModel value,ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
       if(itemStack.is(ModItems.BROKEN_ITEM.get()) && (BrokenItem.getContainedItem(itemStack) != null)) {
           return Minecraft.getInstance().getItemRenderer().getModel(BrokenItem.getContainedItem(itemStack),Minecraft.getInstance().level, Minecraft.getInstance().player, 15);
       }

       return value;
    }


}
