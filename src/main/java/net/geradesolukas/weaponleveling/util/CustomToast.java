package net.geradesolukas.weaponleveling.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sun.jna.platform.unix.X11;
import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
@OnlyIn(Dist.CLIENT)
public class CustomToast extends X11.Drawable implements Toast {
    ItemStack stack;
    int level;
    public CustomToast(ItemStack stack, int level) {
        this.stack = stack;
        this.level = level;
    }

    @Override
    public Toast.Visibility render(PoseStack poseStack, ToastComponent component, long visibilityTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation(WeaponLeveling.MODID +":textures/gui/toasts.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        Style ITEM = Style.EMPTY.withColor(12517240);
        Style TEXT = Style.EMPTY.withColor(9736850);
        Style VALUES = Style.EMPTY.withColor(15422034);

        component.blit(poseStack, 0, 0, 0, 0, this.width(), this.height());

        if (stack != null) {

            List<FormattedCharSequence> subtitle = component.getMinecraft().font.split(new TranslatableComponent("weaponleveling.levelup").setStyle(TEXT).append(new TextComponent(""+level).setStyle(VALUES)), 125);
            List<FormattedCharSequence> title = component.getMinecraft().font.split(new TextComponent(stack.getHoverName().getString()).setStyle(ITEM), 125);

            int i = 16776960;

            if (subtitle.size() == 1) {
                component.getMinecraft().font.draw(poseStack, title.get(0), 30.0F, 7.0F, i | -16777216);
                component.getMinecraft().font.drawShadow(poseStack, title.get(0), 30.0F, 7.0F, i | -16777216);
                component.getMinecraft().font.draw(poseStack, subtitle.get(0), 30.0F, 18.0F, -1);
                component.getMinecraft().font.drawShadow(poseStack,subtitle.get(0),30.0F, 18.0F, -1);
            } else {
                if (visibilityTime < 1500L) {
                    int k = Mth.floor(Mth.clamp((float)(1500L - visibilityTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    component.getMinecraft().font.draw(poseStack, FrameType.GOAL.getDisplayName(), 30.0F, 11.0F, i | k);
                } else {
                    int i1 = Mth.floor(Mth.clamp((float)(visibilityTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int l = this.height() / 2 - subtitle.size() * 9 / 2;

                    for(FormattedCharSequence formattedcharsequence : subtitle) {
                        component.getMinecraft().font.draw(poseStack, formattedcharsequence, 30.0F, (float)l, 16777215 | i1);
                        l += 9;
                    }
                }
            }



            component.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(stack, 8, 8);
            return visibilityTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        } else {
            return Toast.Visibility.HIDE;
        }
    }
}
