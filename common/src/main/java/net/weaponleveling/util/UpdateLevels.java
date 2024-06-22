package net.weaponleveling.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingConfig;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class UpdateLevels {
    public static void applyXPOnItemStack(ItemStack stack, Player player, Entity target, Boolean critical) {
        if (!player.level().isClientSide) {
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
        if(!player.level().isClientSide) {
            if (player.getItemBySlot(EquipmentSlot.HEAD) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.FEET) != ItemStack.EMPTY ) {
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
                if (ModUtils.isAcceptedArmor(helmet) && !ModUtils.isBroken(helmet)) {updateProgressItem(player,helmet,armorXPAmount(value, false, helmet));}
                if (ModUtils.isAcceptedArmor(chestplate) && !ModUtils.isBroken(chestplate)) {updateProgressItem(player,chestplate,armorXPAmount(value, false, chestplate));}
                if (ModUtils.isAcceptedArmor(leggings) && !ModUtils.isBroken(leggings)) {updateProgressItem(player,leggings,armorXPAmount(value, false, leggings));}
                if (ModUtils.isAcceptedArmor(feet) && !ModUtils.isBroken(feet)) {updateProgressItem(player,feet,armorXPAmount(value, false, feet));}

            }
        }
    }



    public static void updateProgressItem(Player player, ItemStack stack, int updateamount) {

        int currentlevel = stack.getOrCreateTag().getInt("level");
        int currentprogress = stack.getOrCreateTag().getInt("levelprogress");
        currentprogress += updateamount;
        if (currentlevel < ModUtils.getMaxLevel(stack) ) {
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
        stack.getOrCreateTag().putInt("level", level);
        stack.getOrCreateTag().putInt("levelprogress", progress);
    }


    public static int getMaxLevel(int currentlevel, ItemStack stack) {
        int maxlevel;
        int levelmodifier = ModUtils.getLevelModifier(stack);
        int startinglevel =  ModUtils.getLevelStartAmount(stack);

        if (currentlevel != 0) {
            maxlevel = ((currentlevel - 1) + currentlevel) * levelmodifier + 100;
        } else {
            maxlevel = startinglevel;
        }
        return maxlevel;
    }

    public static int getXPForEntity(Entity entity) {

        int xpamount = DataGetter.getXpGeneric();
        AtomicInteger liststate = new AtomicInteger();

        //TODO Get Entity Tags here

//        BuiltInRegistries.ENTITY_TYPE.getTags().forEach(tagKeyNamedPair -> {
//            TagKey<EntityType<?>> tagKey = tagKeyNamedPair.getFirst();
//            if(BuiltInRegistries.ENTITY_TYPE.getTag(tagKey).get().contains(entity.getType().arch$holder())) {
//                if (WeaponLevelingConfig.entities_animal.contains("#" + tagKey.location().toString())) {
//                    liststate.set(1);
//                }
//                if (WeaponLevelingConfig.entities_monster.contains("#" +tagKey.location().toString())) {
//                    liststate.set(2);
//                }
//                if (WeaponLevelingConfig.entities_mini_boss.contains("#" +tagKey.location().toString())) {
//                    liststate.set(3);
//                }
//                if (WeaponLevelingConfig.entities_boss.contains("#" +tagKey.location().toString())) {
//                    liststate.set(4);
//                }
//            }
//        });


        if(entity.getType().is(DataGetter.entities_boss) || isCustomBoss(entity)|| liststate.get() == 4) {
            xpamount = DataGetter.getXpBoss();
        }

        else if(entity.getType().is(DataGetter.entities_mini_boss) || isCustomMiniBoss(entity) || liststate.get() == 3) {
            xpamount = DataGetter.getXpMiniboss();
        }

        else if(entity.getType().is(DataGetter.entities_monster) || isCustomMonster(entity) || liststate.get() == 2) {
            xpamount = DataGetter.getXpMonster();
        }

        else if(entity.getType().is(DataGetter.entities_animal) || isCustomAnimal(entity) || liststate.get() == 1) {
            xpamount = DataGetter.getXpAnimal();
        }


        return xpamount;
    }

    private static boolean isCustomAnimal(Entity entity) {
        return entity.getTags().contains("wl_animal");

    }

    private static boolean isCustomMonster(Entity entity) {
        return entity.getTags().contains("wl_monster");

    }


    private static boolean isCustomMiniBoss(Entity entity) {
        return entity.getTags().contains("wl_miniboss");
    }
    private static boolean isCustomBoss(Entity entity) {
        return entity.getTags().contains("wl_boss");

    }


    public static int getXPForHit(ItemStack stack) {
        int xpamount = 0;
        int amount = ModUtils.getHitXPAmount(stack);
        if (shouldGiveHitXP(ModUtils.getHitXPChance(stack))) {xpamount = amount;}

        return xpamount;
    }

    public static int getXPForCrit(ItemStack stack) {
        int xpamount = 0;
        int amount = ModUtils.getCritXPAmount(stack);
        if (shouldGiveHitXP(ModUtils.getCritXPChance(stack))) {xpamount = amount;}
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

    public static boolean shouldGiveHitXP(int probability) {
        double randomValue = Math.random()*100;
        return randomValue <= probability;
    }

    public static int armorXPAmount(int initialxp, boolean taxFree, ItemStack stack) {
        if (taxFree) return initialxp;

        double minamount = ((double) ModUtils.getArmorXPRNGModifier(stack))/100;
        double randomValue = minamount + (1.0 - minamount)*Math.random();

        if (randomValue < minamount) randomValue = minamount;
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
        double maxdamagereduction = getReduction(level,stack) / 100;
        double maxlevel = ModUtils.getMaxLevel(stack);
        double finaldamage = partdamage - (partdamage * (maxdamagereduction * (level/maxlevel)));

        return (float) finaldamage;
    }

    public static float getReduction(int level, ItemStack stack) {
        double maxdamagereduction = ModUtils.getArmorMaxDamageReduction(stack);
        return (float) maxdamagereduction * ((float) level/ ModUtils.getMaxLevel(stack));
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
                if(ModUtils.isAcceptedProjectileWeapon(stack)) {
                    updateProgressItem(player, stack, xpamount);
                }else if(ModUtils.isAcceptedProjectileWeapon(offhandStack)) {
                    updateProgressItem(player, offhandStack, xpamount);
                }
            } else if(ModUtils.isAcceptedMeleeWeaponStack(stack)) {
                updateProgressItem(player,stack,xpamount);
            }

            // For Armor and Potential Offhand Weapon with EFM
            UpdateLevels.applyXPForArmor(player,xpamount);
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
                if(ModUtils.isAcceptedProjectileWeapon(mainhand)) {
                    UpdateLevels.applyXPOnItemStack(mainhand, player, victim, crit);
                } else if(ModUtils.isAcceptedProjectileWeapon(offhand)) {
                    UpdateLevels.applyXPOnItemStack(offhand, player, victim, crit);
                }
            } else if(ModUtils.isAcceptedMeleeWeaponStack(stack)) {
                UpdateLevels.applyXPOnItemStack(stack, player, victim, crit);
            }

        }


    }
}
