package net.geradesolukas.weaponleveling.util;

import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateLevels {
    public static void applyXPOnItemStack(ItemStack stack, Player player, Entity target) {
        if(!player.getLevel().isClientSide) {
            int xpamounthit = UpdateLevels.getXPForHit();
            int xpamount = 0;
            if (!target.isAlive()) {
                xpamount = UpdateLevels.getXPForEntity(target);
            }
            updateProgressItem(player,stack,xpamount+xpamounthit);
        }

    }

    public static void applyXPForArmor(Player player, int value) {
        if(!player.getLevel().isClientSide) {
            if (player.getItemBySlot(EquipmentSlot.HEAD) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY ) {
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
                if (isAcceptedArmor(helmet)) {updateProgressItem(player,helmet,value);}
                if (isAcceptedArmor(chestplate)) {updateProgressItem(player,chestplate,value);}
                if (isAcceptedArmor(leggings)) {updateProgressItem(player,leggings,value);}
                if (isAcceptedArmor(feet)) {updateProgressItem(player,feet,value);}

            }
        }
    }

    public static void applyExperience(Player player, int xpamount) {
        UpdateLevels updateLevels = new UpdateLevels();
        ItemStack hand = player.getMainHandItem();
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);

        if(UpdateLevels.isAcceptedMeleeWeaponStack(hand)) {
            updateLevels.updateProgressItem(player,hand,xpamount);
        }
        if(UpdateLevels.isAcceptedArmor(helmet) && player.getItemBySlot(EquipmentSlot.HEAD) != ItemStack.EMPTY) {
            updateLevels.updateProgressItem(player,helmet,xpamount);
        }
        if(UpdateLevels.isAcceptedArmor(chestplate) && player.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY) {
            updateLevels.updateProgressItem(player,chestplate,xpamount);
        }
        if(UpdateLevels.isAcceptedArmor(leggings) && player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY) {
            updateLevels.updateProgressItem(player,leggings,xpamount);
        }
        if(UpdateLevels.isAcceptedArmor(feet) && player.getItemBySlot(EquipmentSlot.FEET) != ItemStack.EMPTY) {
            updateLevels.updateProgressItem(player,feet,xpamount);
        }
    }

    public static void updateProgressItem(Player player, ItemStack stack, int updateamount) {

        int currentlevel = stack.getOrCreateTag().getInt("level");
        int currentprogress = stack.getOrCreateTag().getInt("levelprogress");
        currentprogress += updateamount;
        if (currentlevel < WeaponLevelingConfig.Server.value_max_level.get()) {
            updateItem(player,stack,currentlevel,currentprogress);
        }
    }


    private static void updateItem(Player player,ItemStack stack, int level, int progress) {
        int maxprogress = getMaxLevel(level);
        if (progress >= maxprogress) {
            int levelupamount = progress /= maxprogress;
            int nextprogress = progress % maxprogress;
            level += levelupamount;
            progress = nextprogress;
            sendLevelUpNotification(player,stack,level);
        }
        stack.getOrCreateTag().putInt("level", level);
        stack.getOrCreateTag().putInt("levelprogress", progress);
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
    private static final Logger LOGGER = LogManager.getLogger();
    public static int getXPForEntity(Entity entity) {
        String name = ForgeRegistries.ENTITIES.getKey(entity.getType()).toString();
        int xpamount = WeaponLevelingConfig.Server.value_kill_generic.get();
        if(WeaponLevelingConfig.Server.entities_miniboss.get().contains(name) || isCustomMiniBoss(entity)) {
            xpamount = WeaponLevelingConfig.Server.value_kill_miniboss.get();
        } else if(WeaponLevelingConfig.Server.entities_boss.get().contains(name)|| isCustomBoss(entity)) {
            xpamount = WeaponLevelingConfig.Server.value_kill_boss.get();
        } else if(WeaponLevelingConfig.Server.entities_animal.get().contains(name)) {
            xpamount = WeaponLevelingConfig.Server.value_kill_animal.get();
        }   else if(WeaponLevelingConfig.Server.entities_monster.get().contains(name)) {
            xpamount = WeaponLevelingConfig.Server.value_kill_monster.get();
        }
        //LOGGER.info("Lefthanded Stuff "+ entity.getEntityData().get());
        // entity.getPersistentData().getBoolean("LeftHanded")

        return xpamount;
    }

    private static boolean isCustomMiniBoss(Entity entity) {
        if (entity.getTags().contains("wl_miniboss")) {
            return true;
        }

        return false;
    }
    private static boolean isCustomBoss(Entity entity) {
        if (entity.getTags().contains("wl_boss")) {
            return true;
        } else {
            return false;
        }

    }


    public static int getXPForHit() {
        int xpamount = 0;
        int amount = WeaponLevelingConfig.Server.value_hit_xp_amount.get();
        if (shouldGiveHitXP(WeaponLevelingConfig.Server.value_hit_percentage.get())) {xpamount = amount;}
        return xpamount;
    }

    public static boolean isAcceptedMeleeWeaponStack(ItemStack stack) {
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        return (stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || WeaponLevelingConfig.Server.melee_items.get().contains(name)) && !WeaponLevelingConfig.Server.blacklist_items.get().contains(name) ;
    }
    public static boolean isAcceptedMeleeWeaponItem(Item item) {
        String name = ForgeRegistries.ITEMS.getKey(item).toString();
        return (item instanceof SwordItem || item instanceof AxeItem || WeaponLevelingConfig.Server.melee_items.get().contains(name)) && !WeaponLevelingConfig.Server.blacklist_items.get().contains(name) ;
    }


    public static boolean isAcceptedArmor(ItemStack stack) {
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        return (stack.getItem() instanceof ArmorItem) && !WeaponLevelingConfig.Server.blacklist_items.get().contains(name);
    }

    public static boolean isAcceptedProjectileWeapon(ItemStack stack) {
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
        return (stack.getItem() instanceof ProjectileWeaponItem || (WeaponLevelingConfig.Server.projectile_items.get().contains(name))) && !WeaponLevelingConfig.Server.blacklist_items.get().contains(name);
    }

    public static void sendLevelUpNotification(Player player,ItemStack stack, int level) {
        if(WeaponLevelingConfig.Server.levelup_type.get() == WeaponLevelingConfig.Server.LevelUpType.TOAST) {
            ToastHelper.sendToast((ServerPlayer) player,stack,level);
        } else {
            Style ITEM = Style.EMPTY.withColor(12517240);
            Style TEXT = Style.EMPTY.withColor(9736850);
            Style VALUES = Style.EMPTY.withColor(15422034);
            String itemname = stack.getDisplayName().getString();
            player.displayClientMessage((new TextComponent(itemname + " ").setStyle(ITEM)).append(new TranslatableComponent("weaponleveling.levelup").setStyle(TEXT)).append(new TextComponent("" + level).setStyle(VALUES)), true);
        }


        Level world = player.getLevel();
        world.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 0.8F);
    }

    public static boolean shouldGiveHitXP(int probability) {
        double randomValue = Math.random()*100;
        return randomValue <= probability;
    }
    public static float reduceDamageArmor(Player player, float damage) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        int helmetlevel = helmet.getOrCreateTag().getInt("level");
        int chestplatelevel = chestplate.getOrCreateTag().getInt("level");
        int leggingslevel = leggings.getOrCreateTag().getInt("level");
        int bootslevel = boots.getOrCreateTag().getInt("level");

        double maxdamagereduction = WeaponLevelingConfig.Server.value_max_damage_reduction.get() / 100;

        double maxlevel = WeaponLevelingConfig.Server.value_max_level.get();

        double partdamage = (damage / 4);
        double helmetdamage = partdamage - (partdamage * (maxdamagereduction * (helmetlevel/maxlevel)));
        double chestplatedamage = partdamage - (partdamage * (maxdamagereduction * (chestplatelevel/maxlevel)));
        double leggingsdamage = partdamage - (partdamage * (maxdamagereduction * (leggingslevel/maxlevel)));
        double bootsdamage = partdamage - (partdamage * (maxdamagereduction * (bootslevel/maxlevel)));

        return (float) (helmetdamage + chestplatedamage + leggingsdamage + bootsdamage);
    }

    public static float getReduction(int level) {
        double maxdamagereduction = WeaponLevelingConfig.Server.value_max_damage_reduction.get();
        return (float) maxdamagereduction * ((float) level/WeaponLevelingConfig.Server.value_max_level.get());
    }


}
