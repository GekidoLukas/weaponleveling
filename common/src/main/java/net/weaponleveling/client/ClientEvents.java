package net.weaponleveling.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.UpdateLevels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ClientEvents {

    private static boolean shouldExtendTooltip() {
        boolean needshift = WLPlatformGetter.getHoldingShift();

        if (needshift) {
            return Screen.hasShiftDown();
        } else {
            return true;
        }
    }

    public static void onTooltipRender(ItemStack stack, List<Component> full_tooltip, TooltipFlag tooltipFlag) {


        List<Component> tooltip = new ArrayList<>();

        DecimalFormat doubleDecimalFormat = new DecimalFormat("#.##");
        DecimalFormat fourDecimalFormat = new DecimalFormat("#.####");

        Style ARROW = Style.EMPTY.withColor(12517240);
        Style TEXT = Style.EMPTY.withColor(9736850);
        Style VALUES = Style.EMPTY.withColor(15422034);
        Style SHIFT = Style.EMPTY.withColor(12517240);



        if(ModUtils.isBroken(stack)) {
            tooltip.add(new TranslatableComponent("weaponleveling.tooltip.broken").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        }
        if (ModUtils.isLevelableItem(stack)) {
            if (shouldExtendTooltip()) {
                int level = stack.getOrCreateTag().getInt("level");
                int levelprogress = stack.getOrCreateTag().getInt("levelprogress");
                int maxlevelprogress = UpdateLevels.getMaxLevel(level,stack);


                tooltip.add(new TranslatableComponent("weaponleveling.tooltip.itemlevel").setStyle(ARROW));

                tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                        .append(new TranslatableComponent("weaponleveling.tooltip.level").setStyle(TEXT))
                        .append(new TextComponent("" + level).setStyle(VALUES))
                );


                if (level < ModUtils.getMaxLevel(stack)) {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.levelprogress").setStyle(TEXT))
                            .append(new TextComponent("" + levelprogress).setStyle(VALUES))
                            .append(new TextComponent("/").setStyle(TEXT))
                            .append(new TextComponent("" + maxlevelprogress).setStyle(VALUES))
                    );
                } else if(level == ModUtils.getMaxLevel(stack)) {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.maxlevel").setStyle(VALUES))
                    );
                }else {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.overmaxlevel").setStyle(VALUES))
                    );
                }

                if (ModUtils.isAcceptedProjectileWeapon(stack) && !(ModUtils.isAcceptedMeleeWeaponStack(stack) || WLPlatformGetter.isCGMGunItem(stack))) {

                    double extradamage = level * ModUtils.getWeaponDamagePerLevel(stack);
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.projectile_weapon_level").setStyle(TEXT))
                            .append(new TextComponent("" + doubleDecimalFormat.format(extradamage)).setStyle(VALUES))
                    );
                }

                if (ModUtils.isAcceptedArmor(stack)) {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.reduction").setStyle(TEXT))
                            .append(new TextComponent( fourDecimalFormat.format(UpdateLevels.getReduction(level, stack))+ "%").setStyle(VALUES))
                    );
                }



            } else {
                tooltip.add(new TranslatableComponent("weaponleveling.tooltip.pressshift").setStyle(SHIFT));
            }
        }
        full_tooltip.addAll(1,tooltip);
    }
}
