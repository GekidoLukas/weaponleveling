package net.weaponleveling.util;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.attribute.IRangedWeapon;
import net.weaponleveling.attribute.WLAttributes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TooltipHelper {

    public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = Util.make(new DecimalFormat("#.##"), decimalFormat -> decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));


    public static void updateTooltipText(ItemStack itemStack, List<Component> lines) {
        if (itemStack.getItem() instanceof IRangedWeapon) {
            mergeAttributeLines_MainHandOffHand(lines);
            replaceAttributeLines_BlueWithGreen(lines);
        }
    }

    private static void mergeAttributeLines_MainHandOffHand(List<Component> tooltip) {
        List<Component> heldInHandLines = new ArrayList<>();
        List<Component> mainHandAttributes = new ArrayList<>();
        List<Component> offHandAttributes = new ArrayList<>();
        for (int i = 0; i < tooltip.size(); i++) {
            var line = tooltip.get(i);
            var content = line.getContents();
            if (content instanceof TranslatableContents translatableText) {
                if (translatableText.getKey().startsWith("item.modifiers")) {
                    heldInHandLines.add(line);
                }
                if (translatableText.getKey().startsWith("attribute.modifier")) {
                    if (heldInHandLines.size() == 1) {
                        mainHandAttributes.add(line);
                    }
                    if (heldInHandLines.size() == 2) {
                        offHandAttributes.add(line);
                    }
                }
            }
        }
        if(heldInHandLines.size() == 2) {
            var mainHandLine = tooltip.indexOf(heldInHandLines.get(0));
            var offHandLine = tooltip.indexOf(heldInHandLines.get(1));
            tooltip.remove(mainHandLine);
            tooltip.add(mainHandLine, Component.translatable("item.modifiers.both_hands").withStyle(ChatFormatting.GRAY));
            tooltip.remove(offHandLine);

            if(tooltip.get(offHandLine -1).getString().isEmpty()) {
                tooltip.remove(offHandLine -1);
            }
//            tooltip.remove(offHandLine); //Because there is an empty line now
            for (var offhandAttribute: offHandAttributes) {
                if(mainHandAttributes.contains(offhandAttribute)) {
                    tooltip.remove(tooltip.lastIndexOf(offhandAttribute));
                }
            }

//            var lastIndex = tooltip.size() - 1;
//            var lastLine = tooltip.get(lastIndex);
//            if (lastLine.getString().isEmpty()) {
//                tooltip.remove(lastIndex);
//            }
        }
    }

    private static void replaceAttributeLines_BlueWithGreen(List<Component> tooltip) {
        var attributeTranslationKey = WLAttributes.RANGED_DAMAGE.getRegisteredName();
        for (int i = 0; i < tooltip.size(); i++)  {
            var line = tooltip.get(i);
            var content = line.getContents();
            if (content instanceof TranslatableContents translatable) {
                var isProjectileAttributeLine = false;
                var attributeValue = 0.0;
                if (translatable.getKey().startsWith("attribute.modifier.plus.0")) {
                    for (var arg: translatable.getArgs()) {
                        if (arg instanceof String string) {
                            try {
                                var number = Double.valueOf(string);
                                attributeValue = number + Minecraft.getInstance().player.getAttributeBaseValue(WLAttributes.RANGED_DAMAGE);
                            } catch (Exception ignored) { }
                        }
                        if (arg instanceof Component attributeText) {
                            if (attributeText.getContents() instanceof TranslatableContents attributeTranslatable) {
                                if (attributeTranslatable.getKey().startsWith(attributeTranslationKey)) {
                                    isProjectileAttributeLine = true;
                                }
                            }
                        }
                    }
                }

                if (isProjectileAttributeLine && attributeValue > 0) {
                    var greenAttributeLine = Component.literal(" ")
                            .append(
                                    Component.translatable("attribute.modifier.equals." + AttributeModifier.Operation.ADD_VALUE.id(),
                                            new Object[]{ ATTRIBUTE_MODIFIER_FORMAT.format(attributeValue), Component.translatable(attributeTranslationKey)})
                            )
                            .withStyle(ChatFormatting.DARK_GREEN);
                    tooltip.set(i, greenAttributeLine);
                }
            }
        }
    }
}
