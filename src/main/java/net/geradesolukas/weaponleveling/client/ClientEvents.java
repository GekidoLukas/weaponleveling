package net.geradesolukas.weaponleveling.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientEvents {

    private static boolean shouldExtendTooltip() {
        boolean needshift = WeaponlevelingConfig.Client.holdshift_for_tooltip.get();

        if (needshift) {
            return Screen.hasShiftDown();
        } else {
            return true;
        }
    }

    public static void onTooltipRender(ItemStack stack, List<Text> full_tooltip, TooltipContext context) {


        List<Text> tooltip = new ArrayList<>();
        DecimalFormat doubleDecimalFormat = new DecimalFormat("#.##");
        DecimalFormat fourDecimalFormat = new DecimalFormat("#.####");
        //CompoundTag compound = new CompoundTag();
        //compound.putInt();
        Style ARROW = Style.EMPTY.withColor(12517240);
        Style TEXT = Style.EMPTY.withColor(9736850);
        Style VALUES = Style.EMPTY.withColor(15422034);
        Style SHIFT = Style.EMPTY.withColor(12517240);



        if (UpdateLevels.isAcceptedMeleeWeaponStack(stack) || UpdateLevels.isAcceptedProjectileWeapon(stack)) {
            if (shouldExtendTooltip()) {
                int level = stack.getOrCreateNbt().getInt("level");
                int levelprogress = stack.getOrCreateNbt().getInt("levelprogress");
                int maxlevelprogress = UpdateLevels.getMaxLevel(level);


                tooltip.add(new TranslatableText("weaponleveling.tooltip.weaponlevel").setStyle(ARROW));

                tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                        .append(new TranslatableText("weaponleveling.tooltip.level").setStyle(TEXT))
                        .append(new LiteralText("" + level).setStyle(VALUES))
                );


                if (level < WeaponlevelingConfig.Server.value_max_level.get()) {
                    tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableText("weaponleveling.tooltip.levelprogress").setStyle(TEXT))
                            .append(new LiteralText("" + levelprogress).setStyle(VALUES))
                            .append(new LiteralText("/").setStyle(TEXT))
                            .append(new LiteralText("" + maxlevelprogress).setStyle(VALUES))
                    );
                } else if(level == WeaponlevelingConfig.Server.value_max_level.get()) {
                    tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableText("weaponleveling.tooltip.maxlevel").setStyle(VALUES))
                    );
                }else {
                    tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableText("weaponleveling.tooltip.overmaxlevel").setStyle(VALUES))
                    );
                }

                if (UpdateLevels.isAcceptedProjectileWeapon(stack) && !UpdateLevels.isAcceptedMeleeWeaponStack(stack)) {
                    double extradamage = level * WeaponlevelingConfig.Server.value_damage_per_level.get();
                    tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableText("weaponleveling.tooltip.projectile_weapon_level").setStyle(TEXT))
                            .append(new LiteralText("" + doubleDecimalFormat.format(extradamage)).setStyle(VALUES))
                    );
                }



            } else {
                tooltip.add(new TranslatableText("weaponleveling.tooltip.pressshift").setStyle(SHIFT));
            }

        }

        if (UpdateLevels.isAcceptedArmor(stack)) {
            if (shouldExtendTooltip()) {
                int level = stack.getOrCreateNbt().getInt("level");
                int levelprogress = stack.getOrCreateNbt().getInt("levelprogress");
                int maxlevelprogress = UpdateLevels.getMaxLevel(level);


                tooltip.add(new TranslatableText("weaponleveling.tooltip.armorlevel").setStyle(ARROW));

                tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                        .append(new TranslatableText("weaponleveling.tooltip.level").setStyle(TEXT))
                        .append(new LiteralText("" + level).setStyle(VALUES))
                );


                if (level < WeaponlevelingConfig.Server.value_max_level.get()) {
                    tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableText("weaponleveling.tooltip.levelprogress").setStyle(TEXT))
                            .append(new LiteralText("" + levelprogress).setStyle(VALUES))
                            .append(new LiteralText("/").setStyle(TEXT))
                            .append(new LiteralText("" + maxlevelprogress).setStyle(VALUES))
                    );
                } else if(level == WeaponlevelingConfig.Server.value_max_level.get()) {
                    tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableText("weaponleveling.tooltip.maxlevel").setStyle(VALUES))
                    );
                }else {
                    tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableText("weaponleveling.tooltip.overmaxlevel").setStyle(VALUES))
                    );
                }

                tooltip.add(new LiteralText(" ▶ ").setStyle(ARROW)
                        .append(new TranslatableText("weaponleveling.tooltip.reduction").setStyle(TEXT))
                        .append(new LiteralText( fourDecimalFormat.format(UpdateLevels.getReduction(level))+ "%").setStyle(VALUES))
                );

            } else {
                tooltip.add(new TranslatableText("weaponleveling.tooltip.pressshift").setStyle(SHIFT));
            }
        }
        if(stack.isDamageable()) {
            if (!context.isAdvanced() && ItemUtils.isBroken(stack)) {
                tooltip.add(new TranslatableText("weaponleveling.tooltip.broken").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
        }

        full_tooltip.addAll(1,tooltip);
    }
}
