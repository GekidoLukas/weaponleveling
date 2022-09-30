package net.geradesolukas.weaponleveling.util;

import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateLevels {
    public static void applyXPOnItemStack(ItemStack stack, PlayerEntity player, Entity target, Boolean critical) {
        if(!player.getWorld().isClient) {
            int xpamountcrit = 0;
            int xpamounthit = UpdateLevels.getXPForHit();
            int xpamount = 0;
            if (!target.isAlive()) {
                xpamount = UpdateLevels.getXPForEntity(target);
            }
            if (critical) {
                xpamountcrit = UpdateLevels.getXPForCrit();
            }
            updateProgressItem(player,stack,xpamount+xpamounthit+xpamountcrit);
        }

    }

    public static void applyXPForArmor(PlayerEntity player, int value) {
        if(!player.getWorld().isClient) {
            if (player.getEquippedStack(EquipmentSlot.HEAD) != ItemStack.EMPTY || player.getEquippedStack(EquipmentSlot.CHEST) != ItemStack.EMPTY || player.getEquippedStack(EquipmentSlot.LEGS) != ItemStack.EMPTY || player.getEquippedStack(EquipmentSlot.LEGS) != ItemStack.EMPTY ) {
                ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
                ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
                ItemStack leggings = player.getEquippedStack(EquipmentSlot.LEGS);
                ItemStack feet = player.getEquippedStack(EquipmentSlot.FEET);
                if (isAcceptedArmor(helmet)) {updateProgressItem(player,helmet,value);}
                if (isAcceptedArmor(chestplate)) {updateProgressItem(player,chestplate,value);}
                if (isAcceptedArmor(leggings)) {updateProgressItem(player,leggings,value);}
                if (isAcceptedArmor(feet)) {updateProgressItem(player,feet,value);}

            }
        }
    }

    public static void applyExperience(PlayerEntity player, int xpamount) {
        UpdateLevels updateLevels = new UpdateLevels();
        ItemStack hand = player.getMainHandStack();
        ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack leggings = player.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = player.getEquippedStack(EquipmentSlot.FEET);

        if(UpdateLevels.isAcceptedMeleeWeaponStack(hand)) {
            updateLevels.updateProgressItem(player,hand,xpamount);
        }
        if(UpdateLevels.isAcceptedArmor(helmet) && player.getEquippedStack(EquipmentSlot.HEAD) != ItemStack.EMPTY) {
            updateLevels.updateProgressItem(player,helmet,xpamount);
        }
        if(UpdateLevels.isAcceptedArmor(chestplate) && player.getEquippedStack(EquipmentSlot.CHEST) != ItemStack.EMPTY) {
            updateLevels.updateProgressItem(player,chestplate,xpamount);
        }
        if(UpdateLevels.isAcceptedArmor(leggings) && player.getEquippedStack(EquipmentSlot.LEGS) != ItemStack.EMPTY) {
            updateLevels.updateProgressItem(player,leggings,xpamount);
        }
        if(UpdateLevels.isAcceptedArmor(feet) && player.getEquippedStack(EquipmentSlot.FEET) != ItemStack.EMPTY) {
            updateLevels.updateProgressItem(player,feet,xpamount);
        }
    }

    public static void updateProgressItem(PlayerEntity player, ItemStack stack, int updateamount) {

        int currentlevel = stack.getOrCreateNbt().getInt("level");
        int currentprogress = stack.getOrCreateNbt().getInt("levelprogress");
        currentprogress += updateamount;
        if (currentlevel < WeaponlevelingConfig.Server.value_max_level.get()) {
            updateItem(player,stack,currentlevel,currentprogress);
        }
    }


    private static void updateItem(PlayerEntity player,ItemStack stack, int level, int progress) {
        int maxprogress = getMaxLevel(level);
        if (progress >= maxprogress) {
            int levelupamount = progress /= maxprogress;
            int nextprogress = progress % maxprogress;
            level += levelupamount;
            progress = nextprogress;
            sendLevelUpNotification(player,stack,level);
        }
        stack.getOrCreateNbt().putInt("level", level);
        stack.getOrCreateNbt().putInt("levelprogress", progress);
    }


    public static int getMaxLevel(int currentlevel) {
        int maxlevel;
        int levelmodifier = WeaponlevelingConfig.Server.value_level_modifier.get();
        int startinglevel = WeaponlevelingConfig.Server.value_starting_level_amount.get();

        if (currentlevel != 0) {
            maxlevel = ((currentlevel - 1) + currentlevel) * levelmodifier + 100;
        } else {
            maxlevel = startinglevel;
        }
        return maxlevel;
    }
    private static final Logger LOGGER = LogManager.getLogger();
    public static int getXPForEntity(Entity entity) {
        String name = Registry.ENTITY_TYPE.getId(entity.getType()).toString();
        int xpamount = WeaponlevelingConfig.Server.value_kill_generic.get();
        if(WeaponlevelingConfig.Server.entities_miniboss.get().contains(name) || isCustomMiniBoss(entity)) {
            xpamount = WeaponlevelingConfig.Server.value_kill_miniboss.get();
        } else if(WeaponlevelingConfig.Server.entities_boss.get().contains(name)|| isCustomBoss(entity)) {
            xpamount = WeaponlevelingConfig.Server.value_kill_boss.get();
        } else if(WeaponlevelingConfig.Server.entities_animal.get().contains(name)) {
            xpamount = WeaponlevelingConfig.Server.value_kill_animal.get();
        }   else if(WeaponlevelingConfig.Server.entities_monster.get().contains(name)) {
            xpamount = WeaponlevelingConfig.Server.value_kill_monster.get();
        }
        //LOGGER.info("Lefthanded Stuff "+ entity.getEntityData().get());
        // entity.getPersistentData().getBoolean("LeftHanded")

        return xpamount;
    }

    private static boolean isCustomMiniBoss(Entity entity) {
        if (entity.getScoreboardTags().contains("wl_miniboss")) {
            return true;
        }

        return false;
    }
    private static boolean isCustomBoss(Entity entity) {
        if (entity.getScoreboardTags().contains("wl_boss")) {
            return true;
        } else {
            return false;
        }

    }


    public static int getXPForHit() {
        int xpamount = 0;
        int amount = WeaponlevelingConfig.Server.value_hit_xp_amount.get();
        if (shouldGiveHitXP(WeaponlevelingConfig.Server.value_hit_percentage.get())) {xpamount = amount;}
        return xpamount;
    }

    public static int getXPForCrit() {
        int xpamount = 0;
        int amount = WeaponlevelingConfig.Server.value_crit_xp_amount.get();
        if (shouldGiveHitXP(WeaponlevelingConfig.Server.value_crit_percentage.get())) {xpamount = amount;}
        return xpamount;
    }

    public static boolean isAcceptedMeleeWeaponStack(ItemStack stack) {
        String name = Registry.ITEM.getId(stack.getItem()).toString();
        return (stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem || WeaponlevelingConfig.Server.melee_items.get().contains(name)) && !WeaponlevelingConfig.Server.blacklist_items.get().contains(name) ;
    }


    public static boolean isAcceptedArmor(ItemStack stack) {
        String name = Registry.ITEM.getId(stack.getItem()).toString();
        return (stack.getItem() instanceof ArmorItem) && !WeaponlevelingConfig.Server.blacklist_items.get().contains(name);
    }

    public static boolean isAcceptedProjectileWeapon(ItemStack stack) {
        String name = Registry.ITEM.getId(stack.getItem()).toString();
        return (stack.getItem() instanceof RangedWeaponItem || (WeaponlevelingConfig.Server.projectile_items.get().contains(name))) && !WeaponlevelingConfig.Server.blacklist_items.get().contains(name);
    }

    public static void sendLevelUpNotification(PlayerEntity player,ItemStack stack, int level) {
        if(WeaponlevelingConfig.Server.levelup_type.get() == WeaponlevelingConfig.Server.LevelUpType.TOAST) {
            ToastHelper.sendToast((ServerPlayerEntity) player,stack,level);
        } else {
            Style ITEM = Style.EMPTY.withColor(12517240);
            Style TEXT = Style.EMPTY.withColor(9736850);
            Style VALUES = Style.EMPTY.withColor(15422034);
            String itemname = stack.getName().getString();
            player.sendMessage((new LiteralText(itemname + " ").setStyle(ITEM)).append(new TranslatableText("weaponleveling.levelup").setStyle(TEXT)).append(new LiteralText("" + level).setStyle(VALUES)), true);
        }


        World world = player.getWorld();
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 0.8F);
    }

    public static boolean shouldGiveHitXP(int probability) {
        double randomValue = Math.random()*100;
        return randomValue <= probability;
    }
    public static float reduceDamageArmor(PlayerEntity player, float damage) {
        ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack leggings = player.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);

        int helmetlevel = helmet.getOrCreateNbt().getInt("level");
        int chestplatelevel = chestplate.getOrCreateNbt().getInt("level");
        int leggingslevel = leggings.getOrCreateNbt().getInt("level");
        int bootslevel = boots.getOrCreateNbt().getInt("level");

        if (ItemUtils.isBroken(helmet)) helmetlevel = 0;
        if (ItemUtils.isBroken(chestplate)) chestplatelevel = 0;
        if (ItemUtils.isBroken(leggings)) leggingslevel = 0;
        if (ItemUtils.isBroken(boots)) bootslevel = 0;

        double maxdamagereduction = WeaponlevelingConfig.Server.value_max_damage_reduction.get() / 100;

        double maxlevel = WeaponlevelingConfig.Server.value_max_level.get();

        double partdamage = (damage / 4);
        double helmetdamage = partdamage - (partdamage * (maxdamagereduction * (helmetlevel/maxlevel)));
        double chestplatedamage = partdamage - (partdamage * (maxdamagereduction * (chestplatelevel/maxlevel)));
        double leggingsdamage = partdamage - (partdamage * (maxdamagereduction * (leggingslevel/maxlevel)));
        double bootsdamage = partdamage - (partdamage * (maxdamagereduction * (bootslevel/maxlevel)));


        return (float) (helmetdamage + chestplatedamage + leggingsdamage + bootsdamage);
    }

    public static float getReduction(int level) {
        double maxdamagereduction = WeaponlevelingConfig.Server.value_max_damage_reduction.get();
        return (float) maxdamagereduction * ((float) level/WeaponlevelingConfig.Server.value_max_level.get());
    }


}
