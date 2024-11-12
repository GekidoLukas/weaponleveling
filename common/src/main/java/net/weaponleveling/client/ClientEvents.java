package net.weaponleveling.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.UpdateLevels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ClientEvents {

    private static boolean shouldExtendTooltip() {
        boolean needshift = WeaponLevelingConfig.holdShiftToShow;

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

        Style TITLE = Style.EMPTY.withColor(WeaponLevelingConfig.titleColor);
        Style ARROW = Style.EMPTY.withColor(WeaponLevelingConfig.arrowColor);
        Style TEXT = Style.EMPTY.withColor(WeaponLevelingConfig.textColor);
        Style VALUES = Style.EMPTY.withColor(WeaponLevelingConfig.valuesColor);
        Style SHIFT = Style.EMPTY.withColor(WeaponLevelingConfig.shiftColor);




        if (ModUtils.isLevelableItem(stack)) {
            if (shouldExtendTooltip()) {
                int level = stack.getOrCreateTag().getInt("level");
                int levelprogress = stack.getOrCreateTag().getInt("levelprogress");
                int maxlevelprogress = UpdateLevels.getMaxLevel(level,stack);


                tooltip.add(Component.translatable("weaponleveling.tooltip.itemlevel").setStyle(TITLE));

                tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                        .append(Component.translatable("weaponleveling.tooltip.level").setStyle(TEXT))
                        .append(Component.literal("" + level).setStyle(VALUES))
                );


                if (level < ModUtils.getMaxLevel(stack)) {
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.levelprogress").setStyle(TEXT))
                            .append(Component.literal("" + levelprogress).setStyle(VALUES))
                            .append(Component.literal("/").setStyle(TEXT))
                            .append(Component.literal("" + maxlevelprogress).setStyle(VALUES))
                    );
                } else if(level == ModUtils.getMaxLevel(stack)) {
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.maxlevel").setStyle(VALUES))
                    );
                }else {
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.overmaxlevel").setStyle(VALUES))
                    );
                }

                if (ModUtils.isAcceptedProjectileWeapon(stack) && !(ModUtils.isAcceptedMeleeWeaponStack(stack) || WLPlatformGetter.isCGMGunItem(stack))) {

                    double extradamage = level * ModUtils.getWeaponDamagePerLevel(stack);
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.projectile_weapon_level").setStyle(TEXT))
                            .append(Component.literal("" + doubleDecimalFormat.format(extradamage)).setStyle(VALUES))
                    );
                }



            } else {
                tooltip.add(Component.translatable("weaponleveling.tooltip.pressshift").setStyle(SHIFT));
            }
        }
        full_tooltip.addAll(1,tooltip);
    }
}
