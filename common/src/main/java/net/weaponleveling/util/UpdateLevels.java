package net.weaponleveling.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.data.mob_xp.MobXP;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import org.jetbrains.annotations.Nullable;

import java.util.Random;


public class UpdateLevels {
    public static void applyXPOnItemStack(ItemStack stack, Player player, Entity target, Boolean critical) {
        if (player.level().isClientSide) {
            return;
        }
        int xpamountcrit = 0;
        int xpamounthit = UpdateLevels.getXPForHit(stack);
        int xpamount = 0;


        WLPlatformGetter.updateEpicFight(player, xpamount + xpamounthit + xpamountcrit);
        UpdateLevels.applyXPForWorn(player,xpamount+ xpamounthit + xpamountcrit);
        updateProgressItem(player, stack, xpamount + xpamounthit + xpamountcrit);


    }

    public static void applyXPForWorn(Player player, int value) {
        if (player.level().isClientSide) {
            return;
        }
        if (player.getItemBySlot(EquipmentSlot.HEAD) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.FEET) != ItemStack.EMPTY ) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            if (ModUtils.isWornLeveling(helmet)) {updateProgressItem(player,helmet,armorXPAmount(value, false, helmet));}
            if (ModUtils.isWornLeveling(chestplate)) {updateProgressItem(player,chestplate,armorXPAmount(value, false, chestplate));}
            if (ModUtils.isWornLeveling(leggings)) {updateProgressItem(player,leggings,armorXPAmount(value, false, leggings));}
            if (ModUtils.isWornLeveling(feet)) {updateProgressItem(player,feet,armorXPAmount(value, false, feet));}

        }
    }



    public static void updateProgressItem(Player player, ItemStack stack, int updateamount) {

        int currentlevel = stack.getOrCreateTag().getInt("level");
        int currentprogress = stack.getOrCreateTag().getInt("levelprogress");
        currentprogress += updateamount;
        if (currentlevel < ModUtils.getMaxLevel(stack) ) {
            int maxprogress = getMaxProgress(currentlevel, stack);
            if (currentprogress >= maxprogress) {
                while (currentprogress >= maxprogress) {
                    currentprogress -= maxprogress;
                    currentlevel++;
                    maxprogress = getMaxProgress(currentlevel, stack);
                }
                sendLevelUpNotification(player, stack, currentlevel);
            }
            stack.getOrCreateTag().putInt("level", currentlevel);
            stack.getOrCreateTag().putInt("levelprogress", currentprogress);
        }
    }


    public static int getMaxProgress(int currentLevel, ItemStack stack) {
        int maxProgress;
        int levelModifier = ModUtils.getLevelModifier(stack);
        int startingLevel =  ModUtils.getLevelStartAmount(stack);

        if (currentLevel != 0) {
            maxProgress = startingLevel + ((currentLevel - 1) + currentLevel) * levelModifier;
        } else {
            maxProgress = startingLevel;
        }
        return maxProgress;
    }

    public static int getXPForEntity(Entity entity) {

        int xpamount = WeaponLevelingConfig.kill_xp;

        if(MobXPLoader.isValid(entity.getType())) {
            MobXP mobXP = MobXPLoader.get(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()));
            xpamount = mobXP.getAmount();
        }

        if(entity.getType().is(DataGetter.entities_blacklist)) {
            xpamount = 0;
        }

        return xpamount;
    }


    public static int getXPForHit(ItemStack stack) {
        int xpamount = 0;
        int amount = ModUtils.getHitXPAmount(stack);
        if (shouldGiveXP(ModUtils.getHitXPChance(stack))) {xpamount = amount;}

        return xpamount;
    }



    public static void sendLevelUpNotification(Player player,ItemStack stack, int level) {
        if(WeaponLevelingConfig.level_up_type == ToastHelper.LevelUpType.TOAST) {
            ToastHelper.sendToast((ServerPlayer) player,stack,level);
        } else {
            Style ITEM = Style.EMPTY.withColor(12517240);
            Style TEXT = Style.EMPTY.withColor(9736850);
            Style VALUES = Style.EMPTY.withColor(15422034);
            String itemname = stack.getDisplayName().getString();
            player.displayClientMessage((Component.literal(itemname + " ").setStyle(ITEM)).append(Component.translatable("weaponleveling.levelup").setStyle(TEXT)).append(Component.literal("" + level).setStyle(VALUES)), true);
        }


        Level world = player.level();
        world.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.7F, 2.0f);
    }

    public static boolean shouldGiveXP(int probability) {
        Random random = new Random();
        double randomValue = random.nextDouble(1,100);
        return randomValue <= probability;
    }

    public static int armorXPAmount(int initialxp, boolean taxFree, ItemStack stack) {
        if (taxFree) return initialxp;

        double minamount = ((double) ModUtils.getWornXPRNGModifier(stack))/100;
        double randomValue = minamount + (1.0 - minamount)*Math.random();

        if (randomValue < minamount) randomValue = minamount;
        return (int)(initialxp * randomValue);
    }


    public static void updateForKill(LivingEntity victim, DamageSource source, @Nullable ItemStack specificStack) {
        Entity killer = source.getEntity();

        if (source.is(DamageTypes.EXPLOSION)) return;
        if (source.is(DamageTypes.MAGIC)) return;

        if (killer instanceof Player player) {
            ItemStack stack = WLPlatformGetter.getAttackItem(player);
            ItemStack offhandStack = player.getOffhandItem();

            int xpamount = UpdateLevels.getXPForEntity(victim);

            if(specificStack != null) {
                updateProgressItem(player, specificStack, xpamount);
            } else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                if(ModUtils.isRangedLeveling(stack)) {
                    updateProgressItem(player, stack, xpamount);
                }else if(ModUtils.isRangedLeveling(offhandStack)) {
                    updateProgressItem(player, offhandStack, xpamount);
                }
            } else if(ModUtils.isMeleeLeveling(stack)) {
                updateProgressItem(player,stack,xpamount);
            }

            // For Armor and Potential Offhand Weapon with EFM
            UpdateLevels.applyXPForWorn(player,xpamount);
            WLPlatformGetter.updateEpicFight(player, xpamount);
        }

    }

    public static void updateForHit(LivingEntity victim, DamageSource source, boolean crit, @Nullable ItemStack specificStack) {
        Entity killer = source.getEntity();


        if (source.is(DamageTypes.EXPLOSION)) return;
        if (source.is(DamageTypes.MAGIC)) return;

        if(killer instanceof Player player) {
            ItemStack stack = WLPlatformGetter.getAttackItem(player);
            if(specificStack != null) {
                UpdateLevels.applyXPOnItemStack(specificStack, player, victim, crit);
            } else if(source.is(DamageTypeTags.IS_PROJECTILE)) {
                ItemStack mainhand = player.getMainHandItem();
                ItemStack offhand = player.getOffhandItem();
                if(ModUtils.isRangedLeveling(mainhand)) {
                    UpdateLevels.applyXPOnItemStack(mainhand, player, victim, crit);
                } else if(ModUtils.isRangedLeveling(offhand)) {
                    UpdateLevels.applyXPOnItemStack(offhand, player, victim, crit);
                }
            } else if(ModUtils.isMeleeLeveling(stack)) {
                UpdateLevels.applyXPOnItemStack(stack, player, victim, crit);
            }

        }


    }
}
