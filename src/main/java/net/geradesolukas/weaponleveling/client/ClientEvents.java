package net.geradesolukas.weaponleveling.client;

import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.compat.cgm.CGMCompat;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = WeaponLeveling.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    private static boolean shouldExtendTooltip() {
        boolean needshift = WeaponLevelingConfig.Client.holdshift_for_tooltip.get();

        if (needshift) {
            return Screen.hasShiftDown();
        } else {
            return true;
        }
    }

    @SubscribeEvent
    public static void onItemColorEvent(ColorHandlerEvent.Item event) {
        //ItemColors colors = event.getItemColors();
        //if (ItemUtils.isBroken(event.)) {
        //
        //}

    }
    //private static boolean jsonitem(ItemStack stack, LevelableItemsLoader loader) {
    //    return loader.isValid(stack.getItem());
    //}
    @SubscribeEvent
    public static void onTooltipRender(ItemTooltipEvent event) {

        ItemStack stack = event.getItemStack();
        List<Component> tooltip = new ArrayList<>();
        List<Component> full_tooltip = event.getToolTip();
        DecimalFormat doubleDecimalFormat = new DecimalFormat("#.##");
        DecimalFormat fourDecimalFormat = new DecimalFormat("#.####");
        //CompoundTag compound = new CompoundTag();
        //compound.putInt();
        Style ARROW = Style.EMPTY.withColor(12517240);
        Style TEXT = Style.EMPTY.withColor(9736850);
        Style VALUES = Style.EMPTY.withColor(15422034);
        Style SHIFT = Style.EMPTY.withColor(12517240);

        //if (ItemUtils.isLevelableItem(stack)) {
        //    full_tooltip.add(new TextComponent("Is JsonItem").setStyle(VALUES));
        //}



        if (ItemUtils.isAcceptedMeleeWeaponStack(stack) || ItemUtils.isAcceptedProjectileWeapon(stack)) {
            if (shouldExtendTooltip()) {
                int level = stack.getOrCreateTag().getInt("level");
                int levelprogress = stack.getOrCreateTag().getInt("levelprogress");
                int maxlevelprogress = UpdateLevels.getMaxLevel(level,stack);


                tooltip.add(new TranslatableComponent("weaponleveling.tooltip.weaponlevel").setStyle(ARROW));

                tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                        .append(new TranslatableComponent("weaponleveling.tooltip.level").setStyle(TEXT))
                        .append(new TextComponent("" + level).setStyle(VALUES))
                        );


                if (level < ItemUtils.getMaxLevel(stack)) {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.levelprogress").setStyle(TEXT))
                            .append(new TextComponent("" + levelprogress).setStyle(VALUES))
                            .append(new TextComponent("/").setStyle(TEXT))
                            .append(new TextComponent("" + maxlevelprogress).setStyle(VALUES))
                    );
                } else if(level == ItemUtils.getMaxLevel(stack)) {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.maxlevel").setStyle(VALUES))
                    );
                }else {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.overmaxlevel").setStyle(VALUES))
                    );
                }

                if (ItemUtils.isAcceptedProjectileWeapon(stack) && !(ItemUtils.isAcceptedMeleeWeaponStack(stack) || CGMCompat.isGunItem(stack))) {
                    double extradamage = level * ItemUtils.getWeaponDamagePerLevel(stack);
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.projectile_weapon_level").setStyle(TEXT))
                            .append(new TextComponent("" + doubleDecimalFormat.format(extradamage)).setStyle(VALUES))
                    );
                }



            } else {
                tooltip.add(new TranslatableComponent("weaponleveling.tooltip.pressshift").setStyle(SHIFT));
            }

        }

        if (ItemUtils.isAcceptedArmor(stack)) {
            if (shouldExtendTooltip()) {
                int level = stack.getOrCreateTag().getInt("level");
                int levelprogress = stack.getOrCreateTag().getInt("levelprogress");
                int maxlevelprogress = UpdateLevels.getMaxLevel(level,stack);


                tooltip.add(new TranslatableComponent("weaponleveling.tooltip.armorlevel").setStyle(ARROW));

                tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                        .append(new TranslatableComponent("weaponleveling.tooltip.level").setStyle(TEXT))
                        .append(new TextComponent("" + level).setStyle(VALUES))
                );


                if (level < ItemUtils.getMaxLevel(stack)) {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.levelprogress").setStyle(TEXT))
                            .append(new TextComponent("" + levelprogress).setStyle(VALUES))
                            .append(new TextComponent("/").setStyle(TEXT))
                            .append(new TextComponent("" + maxlevelprogress).setStyle(VALUES))
                    );
                } else if(level == ItemUtils.getMaxLevel(stack)) {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.maxlevel").setStyle(VALUES))
                    );
                }else {
                    tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                            .append(new TranslatableComponent("weaponleveling.tooltip.overmaxlevel").setStyle(VALUES))
                    );
                }

                tooltip.add(new TextComponent(" ▶ ").setStyle(ARROW)
                        .append(new TranslatableComponent("weaponleveling.tooltip.reduction").setStyle(TEXT))
                        .append(new TextComponent( fourDecimalFormat.format(UpdateLevels.getReduction(level, stack))+ "%").setStyle(VALUES))
                );

            } else {
                tooltip.add(new TranslatableComponent("weaponleveling.tooltip.pressshift").setStyle(SHIFT));
            }
        }
        if(stack.isDamageableItem()) {
            if (!event.getFlags().isAdvanced() && ItemUtils.isBroken(stack)) {
                tooltip.add(new TranslatableComponent("weaponleveling.tooltip.broken").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            }
        }

        full_tooltip.addAll(1,tooltip);
    }
}
