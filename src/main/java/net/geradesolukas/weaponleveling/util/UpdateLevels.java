package net.geradesolukas.weaponleveling.util;

import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class UpdateLevels {

    public void updateProgress(Player player, ItemStack stack, int updateamount) {

        int currentlevel = stack.getOrCreateTag().getInt("level");
        int currentprogress = stack.getOrCreateTag().getInt("levelprogress");
        currentprogress += updateamount;

        int maxprogress = getMaxLevel(currentlevel);

        if (currentlevel < WeaponLevelingConfig.Server.value_max_level.get()) {
            //Leveling up
            if (currentprogress >= maxprogress) {
                int levelupamount = currentprogress /= maxprogress;
                int nextprogress = currentprogress % maxprogress;

                currentlevel += levelupamount;
                currentprogress = nextprogress;


                String weaponname = stack.getDisplayName().getString();
                //Style GOLD = Style.EMPTY.withColor(16761408);
                Style ITEM = Style.EMPTY.withColor(12517240);
                Style TEXT = Style.EMPTY.withColor(9736850);
                Style VALUES = Style.EMPTY.withColor(15422034);
                player.displayClientMessage((new TextComponent(weaponname + "").setStyle(ITEM)).append(new TranslatableComponent("weaponleveling.actionbar.levelup").setStyle(TEXT)).append(new TextComponent("" + currentlevel).setStyle(VALUES)), true);

                //player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F ,1.5F);
                Level world = player.getLevel();
                world.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 0.8F);


            }


            stack.getOrCreateTag().putInt("level", currentlevel);
            stack.getOrCreateTag().putInt("levelprogress", currentprogress);
        }






    }


    public static int getMaxLevel(int currentlevel) {
        int maxlevel;
        int levelmodifier = WeaponLevelingConfig.Server.value_level_modifier.get();
        int startinglevel = WeaponLevelingConfig.Server.value_starting_level_amount.get();

        if (currentlevel != 0) {
              maxlevel = ((currentlevel - 1) + currentlevel) * levelmodifier + 100;
        } else {
            maxlevel = startinglevel;
        }
        return maxlevel;
    }


    public static boolean isAcceptedItem(ItemStack stack) {
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        return (stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || WeaponLevelingConfig.Server.added_items.get().contains(name)) && !WeaponLevelingConfig.Server.blacklist_items.get().contains(name) ;
    }


}
