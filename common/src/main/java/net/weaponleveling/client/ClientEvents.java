package net.weaponleveling.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.api.LevelingAPI;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.TooltipHelper;

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
        TooltipHelper.updateTooltipText(stack, full_tooltip);

        List<Component> tooltip = new ArrayList<>();

        Style TITLE = Style.EMPTY.withColor(WeaponLevelingConfig.titleColor);
        Style ARROW = Style.EMPTY.withColor(WeaponLevelingConfig.arrowColor);
        Style TEXT = Style.EMPTY.withColor(WeaponLevelingConfig.textColor);
        Style VALUES = Style.EMPTY.withColor(WeaponLevelingConfig.valuesColor);
        Style SHIFT = Style.EMPTY.withColor(WeaponLevelingConfig.shiftColor);



        if (ModUtils.isLevelableItem(stack)) {
            if (shouldExtendTooltip()) {
                int level = stack.getOrCreateTag().getInt("level");
                int levelprogress = stack.getOrCreateTag().getInt("levelprogress");
                int maxlevelprogress = LevelingAPI.getMaxProgress(stack);


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


            } else {
                tooltip.add(Component.translatable("weaponleveling.tooltip.pressshift").setStyle(SHIFT));
            }
        }
        full_tooltip.addAll(1,tooltip);
    }
}
