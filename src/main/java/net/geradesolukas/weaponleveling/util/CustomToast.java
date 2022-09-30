package net.geradesolukas.weaponleveling.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CustomToast implements Toast {
    ItemStack stack;
    int level;
    public CustomToast(ItemStack stack, int level) {
        this.stack = stack;
        this.level = level;
    }
    
    @Override
    public Toast.Visibility draw(MatrixStack poseStack, ToastManager component, long visibilityTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new Identifier(WeaponLeveling.MODID +":textures/gui/toasts.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        Style ITEM = Style.EMPTY.withColor(12517240);
        Style TEXT = Style.EMPTY.withColor(9736850);
        Style VALUES = Style.EMPTY.withColor(15422034);

        component.drawTexture(poseStack, 0, 0, 0, 0, this.getWidth(), this.getHeight());

        if (stack != null) {

            List<OrderedText> subtitle = component.getClient().textRenderer.wrapLines(new TranslatableText("weaponleveling.levelup").setStyle(TEXT).append(new LiteralText(""+level).setStyle(VALUES)), 125);
            List<OrderedText> title = component.getClient().textRenderer.wrapLines(new LiteralText(stack.getName().getString()).setStyle(ITEM), 125);

            int i = 16776960;

            if (subtitle.size() == 1) {
                component.getClient().textRenderer.draw(poseStack, title.get(0), 30.0F, 7.0F, i | -16777216);
                component.getClient().textRenderer.drawWithShadow(poseStack, title.get(0), 30.0F, 7.0F, i | -16777216);
                component.getClient().textRenderer.draw(poseStack, subtitle.get(0), 30.0F, 18.0F, -1);
                component.getClient().textRenderer.drawWithShadow(poseStack,subtitle.get(0),30.0F, 18.0F, -1);
            } else {
                if (visibilityTime < 1500L) {
                    int k = MathHelper.floor(MathHelper.clamp((float)(1500L - visibilityTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    component.getClient().textRenderer.draw(poseStack, AdvancementFrame.GOAL.getToastText(), 30.0F, 11.0F, i | k);
                } else {
                    int i1 = MathHelper.floor(MathHelper.clamp((float)(visibilityTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int l = this.getHeight() / 2 - subtitle.size() * 9 / 2;

                    for(OrderedText formattedcharsequence : subtitle) {
                        component.getClient().textRenderer.draw(poseStack, formattedcharsequence, 30.0F, (float)l, 16777215 | i1);
                        l += 9;
                    }
                }
            }



            component.getClient().getItemRenderer().renderGuiItemIcon(stack, 8, 8);
            return visibilityTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        } else {
            return Toast.Visibility.HIDE;
        }
    }
}
