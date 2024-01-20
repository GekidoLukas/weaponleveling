package net.weaponleveling.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.util.ItemUtils;
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



        if(ItemUtils.isBroken(stack)) {
            tooltip.add(Component.translatable("weaponleveling.tooltip.broken").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        }
        if (ItemUtils.isLevelableItem(stack)) {
            if (shouldExtendTooltip()) {
                int level = stack.getOrCreateTag().getInt("level");
                int levelprogress = stack.getOrCreateTag().getInt("levelprogress");
                int maxlevelprogress = UpdateLevels.getMaxLevel(level,stack);


                tooltip.add(Component.translatable("weaponleveling.tooltip.itemlevel").setStyle(ARROW));

                tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                        .append(Component.translatable("weaponleveling.tooltip.level").setStyle(TEXT))
                        .append(Component.literal("" + level).setStyle(VALUES))
                );


                if (level < ItemUtils.getMaxLevel(stack)) {
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.levelprogress").setStyle(TEXT))
                            .append(Component.literal("" + levelprogress).setStyle(VALUES))
                            .append(Component.literal("/").setStyle(TEXT))
                            .append(Component.literal("" + maxlevelprogress).setStyle(VALUES))
                    );
                } else if(level == ItemUtils.getMaxLevel(stack)) {
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.maxlevel").setStyle(VALUES))
                    );
                }else {
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.overmaxlevel").setStyle(VALUES))
                    );
                }

                if (ItemUtils.isAcceptedProjectileWeapon(stack) && !(ItemUtils.isAcceptedMeleeWeaponStack(stack) || WLPlatformGetter.isCGMGunItem(stack))) {

                    double extradamage = level * ItemUtils.getWeaponDamagePerLevel(stack);
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.projectile_weapon_level").setStyle(TEXT))
                            .append(Component.literal("" + doubleDecimalFormat.format(extradamage)).setStyle(VALUES))
                    );
                }

                if (ItemUtils.isAcceptedArmor(stack)) {
                    tooltip.add(Component.literal(" ▶ ").setStyle(ARROW)
                            .append(Component.translatable("weaponleveling.tooltip.reduction").setStyle(TEXT))
                            .append(Component.literal( fourDecimalFormat.format(UpdateLevels.getReduction(level, stack))+ "%").setStyle(VALUES))
                    );
                }



            } else {
                tooltip.add(Component.translatable("weaponleveling.tooltip.pressshift").setStyle(SHIFT));
            }
        }
        full_tooltip.addAll(1,tooltip);
    }
}
