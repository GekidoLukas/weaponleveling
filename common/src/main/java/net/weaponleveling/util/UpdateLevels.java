package net.weaponleveling.util;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.weaponleveling.WLPlatformGetter;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class UpdateLevels {
    public static void applyXPOnItemStack(ItemStack stack, Player player, Entity target, Boolean critical) {
        if (!player.getLevel().isClientSide) {
            int xpamountcrit = 0;
            int xpamounthit = UpdateLevels.getXPForHit(stack);
            int xpamount = 0;

            if (critical) {
                xpamountcrit = UpdateLevels.getXPForCrit(stack);
            }

            WLPlatformGetter.updateEpicFight(player, xpamount + xpamounthit + xpamountcrit);
            UpdateLevels.applyXPForArmor(player,xpamount+ xpamounthit + xpamountcrit);
            updateProgressItem(player, stack, xpamount + xpamounthit + xpamountcrit);
        }


    }

    public static void applyXPForArmor(Player player, int value) {
        if(!player.getLevel().isClientSide) {
            if (player.getItemBySlot(EquipmentSlot.HEAD) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.FEET) != ItemStack.EMPTY ) {
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
                if (ItemUtils.isAcceptedArmor(helmet) && !ItemUtils.isBroken(helmet)) {updateProgressItem(player,helmet,armorXPAmount(value, false, helmet));}
                if (ItemUtils.isAcceptedArmor(chestplate) && !ItemUtils.isBroken(chestplate)) {updateProgressItem(player,chestplate,armorXPAmount(value, false, chestplate));}
                if (ItemUtils.isAcceptedArmor(leggings) && !ItemUtils.isBroken(leggings)) {updateProgressItem(player,leggings,armorXPAmount(value, false, leggings));}
                if (ItemUtils.isAcceptedArmor(feet) && !ItemUtils.isBroken(feet)) {updateProgressItem(player,feet,armorXPAmount(value, false, feet));}

            }
        }
    }

    public static void applyXPForArmorItem(Player player, boolean crit, ItemStack stack) {
        if(!player.getLevel().isClientSide) {
            if (player.getItemBySlot(EquipmentSlot.HEAD) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.FEET) != ItemStack.EMPTY ) {
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
                if (ItemUtils.isAcceptedArmor(helmet) && !ItemUtils.isBroken(helmet)) {updateProgressItem(player,helmet,armorXPAmount(getCritOrHit(crit,stack), true, helmet));}
                if (ItemUtils.isAcceptedArmor(chestplate) && !ItemUtils.isBroken(chestplate)) {updateProgressItem(player,chestplate,armorXPAmount(getCritOrHit(crit,stack), true, chestplate));}
                if (ItemUtils.isAcceptedArmor(leggings) && !ItemUtils.isBroken(leggings)) {updateProgressItem(player,leggings,armorXPAmount(getCritOrHit(crit,stack), true, leggings));}
                if (ItemUtils.isAcceptedArmor(feet) && !ItemUtils.isBroken(feet)) {updateProgressItem(player,feet,armorXPAmount(getCritOrHit(crit,stack), true, feet));}

            }
        }
    }

    public static int getCritOrHit(boolean crit, ItemStack stack) {
        if (crit) return ItemUtils.getCritXPAmount(stack);
        else return ItemUtils.getHitXPAmount(stack);
    }



    public static void updateProgressItem(Player player, ItemStack stack, int updateamount) {

        int currentlevel = stack.getOrCreateTag().getInt("level");
        int currentprogress = stack.getOrCreateTag().getInt("levelprogress");
        currentprogress += updateamount;
        if (currentlevel < ItemUtils.getMaxLevel(stack) ) {
            //&& !ItemUtils.isBroken(stack)
            updateItem(player,stack,currentlevel,currentprogress);
        }
    }


    private static void updateItem(Player player,ItemStack stack, int level, int progress) {
        int maxprogress = getMaxLevel(level, stack);
        if (progress >= maxprogress) {
            while (progress >= maxprogress) {
                progress -= maxprogress;
                level++;
                maxprogress = getMaxLevel(level, stack);
            }
            sendLevelUpNotification(player, stack, level);
        }

        //if (progress >= maxprogress) {
        //    int levelupamount = progress /= maxprogress;
        //    int nextprogress = progress % maxprogress;
        //    level += levelupamount;
        //    progress = nextprogress;
        //    sendLevelUpNotification(player,stack,level);
        //}
        stack.getOrCreateTag().putInt("level", level);
        stack.getOrCreateTag().putInt("levelprogress", progress);
    }


    public static int getMaxLevel(int currentlevel, ItemStack stack) {
        int maxlevel;
        int levelmodifier = ItemUtils.getLevelModifier(stack);
        int startinglevel =  ItemUtils.getLevelStartAmount(stack);

        if (currentlevel != 0) {
            maxlevel = ((currentlevel - 1) + currentlevel) * levelmodifier + 100;
        } else {
            maxlevel = startinglevel;
        }
        return maxlevel;
    }

    public static int getXPForEntity(Entity entity) {
        String name = Registry.ENTITY_TYPE.getKey(entity.getType()).toString();
        int xpamount = WLPlatformGetter.getXPKillGeneric();
        AtomicInteger liststate = new AtomicInteger();


        Registry.ENTITY_TYPE.getTags().forEach(tagKeyNamedPair -> {
            TagKey<EntityType<?>> tagKey = tagKeyNamedPair.getFirst();
            if(Registry.ENTITY_TYPE.getTag(tagKey).get().contains(entity.getType().arch$holder())) {
                if (WLPlatformGetter.getAnimalEntities().contains("#" + tagKey.location().toString())) {
                    liststate.set(1);
                }
                if (WLPlatformGetter.getMonsterEntities().contains("#" +tagKey.location().toString())) {
                    liststate.set(2);
                }
                if (WLPlatformGetter.getMinibossEntities().contains("#" +tagKey.location().toString())) {
                    liststate.set(3);
                }
                if (WLPlatformGetter.getBossEntities().contains("#" +tagKey.location().toString())) {
                    liststate.set(4);
                }
            }
        });


        if(WLPlatformGetter.getBossEntities().contains(name) || isCustomBoss(entity)|| liststate.get() == 4) {
            xpamount = WLPlatformGetter.getXPKillBoss();
        }

        else if(WLPlatformGetter.getMinibossEntities().contains(name) || isCustomMiniBoss(entity) || liststate.get() == 3) {
            xpamount = WLPlatformGetter.getXPKillMiniboss();
        }

        else if(WLPlatformGetter.getMonsterEntities().contains(name) || isCustomMonster(entity) || liststate.get() == 2) {
            xpamount = WLPlatformGetter.getXPKillMonster();
        }

        else if(WLPlatformGetter.getAnimalEntities().contains(name) || isCustomAnimal(entity) || liststate.get() == 1) {
            xpamount = WLPlatformGetter.getXPKillAnimal();
        }


        return xpamount;
    }

    private static boolean isCustomAnimal(Entity entity) {
        if (entity.getTags().contains("wl_animal")) {
            return true;
        } else {
            return false;
        }

    }

    private static boolean isCustomMonster(Entity entity) {
        if (entity.getTags().contains("wl_monster")) {
            return true;
        } else {
            return false;
        }

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


    public static int getXPForHit(ItemStack stack) {
        int xpamount = 0;
        int amount = ItemUtils.getHitXPAmount(stack);
        if (shouldGiveHitXP(ItemUtils.getHitXPChance(stack))) {xpamount = amount;}

        return xpamount;
    }

    public static int getXPForCrit(ItemStack stack) {
        int xpamount = 0;
        int amount = ItemUtils.getCritXPAmount(stack);
        if (shouldGiveHitXP(ItemUtils.getCritXPChance(stack))) {xpamount = amount;}
        return xpamount;
    }


    public static void sendLevelUpNotification(Player player,ItemStack stack, int level) {
        if(WLPlatformGetter.getLevelUpType() == ToastHelper.LevelUpType.TOAST) {
            ToastHelper.sendToast((ServerPlayer) player,stack,level);
        } else {
            Style ITEM = Style.EMPTY.withColor(12517240);
            Style TEXT = Style.EMPTY.withColor(9736850);
            Style VALUES = Style.EMPTY.withColor(15422034);
            String itemname = stack.getDisplayName().getString();
            player.displayClientMessage((new TextComponent(itemname + " ").setStyle(ITEM)).append(new TranslatableComponent("weaponleveling.levelup").setStyle(TEXT)).append(new TextComponent("" + level).setStyle(VALUES)), true);
        }


        Level world = player.getLevel();
        world.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.7F, 2.0f);
    }

    public static boolean shouldGiveHitXP(int probability) {
        double randomValue = Math.random()*100;
        return randomValue <= probability;
    }

    public static int armorXPAmount(int initialxp, boolean taxFree, ItemStack stack) {
        if (taxFree) return initialxp;

        double minamount = ((double)ItemUtils.getArmorXPRNGModifier(stack))/100;
        double randomValue = minamount + (1.0 - minamount)*Math.random();

        if (randomValue < minamount) randomValue = minamount;
        //WeaponLevelingMod.LOGGER.info("Random Value: "+ randomValue + " And XP: " + (int)(initialxp * randomValue) + " And Initial: " + initialxp);
        return (int)(initialxp * randomValue);
    }

    public static float reduceDamageArmor(LivingEntity player, float damage) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        float partdamage = (damage / 4);
        float helmetdamage = getDamagePerPiece(player,partdamage,helmet);
        float chestplatedamage = getDamagePerPiece(player,partdamage,chestplate);
        float leggingsdamage = getDamagePerPiece(player,partdamage,leggings);
        float bootsdamage = getDamagePerPiece(player,partdamage,boots);

        return (helmetdamage + chestplatedamage + leggingsdamage + bootsdamage);
    }

    public static float getDamagePerPiece(LivingEntity player, float partdamage, ItemStack stack) {
        int level = stack.getOrCreateTag().getInt("level");
        //if (ItemUtils.isBroken(stack)) level = 0;
        double maxdamagereduction = getReduction(level,stack) / 100;
        double maxlevel = ItemUtils.getMaxLevel(stack);
        double finaldamage = partdamage - (partdamage * (maxdamagereduction * (level/maxlevel)));

        return (float) finaldamage;
    }

    public static float getReduction(int level, ItemStack stack) {
        double maxdamagereduction = ItemUtils.getArmorMaxDamageReduction(stack);
        return (float) maxdamagereduction * ((float) level/ItemUtils.getMaxLevel(stack));
    }

    public static void updateForKill(LivingEntity victim, DamageSource source, @Nullable ItemStack specificStack) {
        Entity killer = source.getEntity();

        if (source.isExplosion()) return;
        if (source.isMagic()) return;

        if (killer instanceof Player player) {
            ItemStack stack = WLPlatformGetter.getAttackItem(player);
            ItemStack offhandStack = player.getOffhandItem();

            int xpamount = UpdateLevels.getXPForEntity(victim);

            if(specificStack != null) {
                updateProgressItem(player, specificStack, xpamount);
            } else if (source.isProjectile()) {
                if(ItemUtils.isAcceptedProjectileWeapon(stack)) {
                    updateProgressItem(player, stack, xpamount);
                }else if(ItemUtils.isAcceptedProjectileWeapon(offhandStack)) {
                    updateProgressItem(player, offhandStack, xpamount);
                }
            } else if(ItemUtils.isAcceptedMeleeWeaponStack(stack)) {
                updateProgressItem(player,stack,xpamount);
            }

            // For Armor and Potential Offhand Weapon with EFM
            UpdateLevels.applyXPForArmor(player,xpamount);
            WLPlatformGetter.updateEpicFight(player, xpamount);
        }

    }

    public static void updateForHit(LivingEntity victim, DamageSource source, boolean crit, @Nullable ItemStack specificStack) {
        Entity killer = source.getEntity();


        if (source.isExplosion()) return;
        if (source.isMagic()) return;

        if(killer instanceof Player player) {
            ItemStack stack = WLPlatformGetter.getAttackItem(player);
            if(specificStack != null) {
                UpdateLevels.applyXPOnItemStack(specificStack, player, victim, crit);
            } else if(source.isProjectile()) {
                ItemStack mainhand = player.getMainHandItem();
                ItemStack offhand = player.getOffhandItem();
                if(ItemUtils.isAcceptedProjectileWeapon(mainhand)) {
                    UpdateLevels.applyXPOnItemStack(mainhand, player, victim, crit);
                } else if(ItemUtils.isAcceptedProjectileWeapon(offhand)) {
                    UpdateLevels.applyXPOnItemStack(offhand, player, victim, crit);
                }
            } else if(ItemUtils.isAcceptedMeleeWeaponStack(stack)) {
                UpdateLevels.applyXPOnItemStack(stack, player, victim, crit);
            }

        }


    }
}
