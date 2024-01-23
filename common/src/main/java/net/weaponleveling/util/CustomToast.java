package net.weaponleveling.util;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingMod;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CustomToast implements Toast {


    public static final ResourceLocation CUSTOM_TEXTURE = new ResourceLocation(WeaponLevelingMod.MODID +":textures/gui/toasts.png");
    ItemStack stack;
    int level;
    public CustomToast(ItemStack stack, int level) {
        this.stack = stack;
        this.level = level;
    }

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent component, long visibilityTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation(WeaponLevelingMod.MODID +":textures/gui/toasts.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        Style ITEM = Style.EMPTY.withColor(12517240);
        Style TEXT = Style.EMPTY.withColor(9736850);
        Style VALUES = Style.EMPTY.withColor(15422034);

        guiGraphics.blit(CUSTOM_TEXTURE, 0, 0, 0, 0, this.width(), this.height());

        if (stack != null) {

            List<FormattedCharSequence> subtitle = component.getMinecraft().font.split(Component.translatable("weaponleveling.levelup").setStyle(TEXT).append(Component.literal(""+level).setStyle(VALUES)), 125);
            List<FormattedCharSequence> title = component.getMinecraft().font.split(Component.literal(stack.getHoverName().getString()).setStyle(ITEM), 125);

            int i = 16776960;

            if (subtitle.size() == 1) {
                guiGraphics.drawString(component.getMinecraft().font, title.get(0), 30, 7, i | -16777216, false);
                guiGraphics.drawString(component.getMinecraft().font, title.get(0), 30, 7, i | -16777216, false);
                guiGraphics.drawString(component.getMinecraft().font, subtitle.get(0), 30, 18, -1);
                guiGraphics.drawString(component.getMinecraft().font, subtitle.get(0),30, 18, -1);
            } else {
                if (visibilityTime < 1500L) {
                    int k = Mth.floor(Mth.clamp((float)(1500L - visibilityTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    guiGraphics.drawString(component.getMinecraft().font, FrameType.GOAL.getDisplayName(), 30, 11, i | k);
                } else {
                    int i1 = Mth.floor(Mth.clamp((float)(visibilityTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int l = this.height() / 2 - subtitle.size() * 9 / 2;

                    for(FormattedCharSequence formattedcharsequence : subtitle) {
                        guiGraphics.drawString(component.getMinecraft().font, formattedcharsequence, 30, l, 16777215 | i1);
                        l += 9;
                    }
                }
            }



            guiGraphics.renderFakeItem(stack, 8, 8);
            return visibilityTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        } else {
            return Toast.Visibility.HIDE;
        }
    }


}
