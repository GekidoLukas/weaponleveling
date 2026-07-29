package net.weaponleveling.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.api.LevelingAPI;
import net.weaponleveling.api.event.ChooseAttackItemEvent;
import net.weaponleveling.api.event.HitXPGainEvent;
import net.weaponleveling.api.event.ItemLevelUpdateEvent;
import net.weaponleveling.api.event.KillXPGainEvent;
import net.weaponleveling.data.levelable_item.type.LevelingTypes;
import net.weaponleveling.data.mob_xp.MobXP;
import net.weaponleveling.data.mob_xp.MobXPLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@ApiStatus.Internal
public class LevelingLogic {
    public static void applyHitXP(ItemStack stack, LivingEntity attacker, Entity target, Boolean critical) {
        if(target.getType().is(TagKey.create(Registries.ENTITY_TYPE, WeaponLevelingMod.id("entities_blacklist")))) return;
        if (attacker.level().isClientSide) {
            return;
        }
        if(HitXPGainEvent.ITEM_PRE.invoker().preItem(stack,attacker,target,critical).isTrue()) return;


        int xpamountcrit = 0;
        int xpamounthit = LevelingLogic.getXPForHit(stack);
        int xpamount = 0;


        LevelingLogic.applyXPForWorn(attacker,xpamount+ xpamounthit + xpamountcrit);
        updateProgressItem(attacker, stack, xpamount + xpamounthit + xpamountcrit);
        HitXPGainEvent.ITEM_POST.invoker().postItem(stack,attacker,target,critical, xpamount + xpamounthit + xpamountcrit);
    }

    private static void applyXPForWorn(LivingEntity attacker, int value) {
        if (attacker.level().isClientSide) {
            return;
        }
        if (attacker.getItemBySlot(EquipmentSlot.HEAD) != ItemStack.EMPTY || attacker.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY || attacker.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY || attacker.getItemBySlot(EquipmentSlot.FEET) != ItemStack.EMPTY ) {
            ItemStack helmet = attacker.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chestplate = attacker.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack leggings = attacker.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack feet = attacker.getItemBySlot(EquipmentSlot.FEET);
            if (ModUtils.isWornLeveling(helmet, EquipmentSlot.HEAD)) updateProgressItem(attacker,helmet,armorXPAmount(value, false, helmet));
            if (ModUtils.isWornLeveling(chestplate, EquipmentSlot.CHEST)) updateProgressItem(attacker,chestplate,armorXPAmount(value, false, chestplate));
            if (ModUtils.isWornLeveling(leggings, EquipmentSlot.LEGS)) updateProgressItem(attacker,leggings,armorXPAmount(value, false, leggings));
            if (ModUtils.isWornLeveling(feet, EquipmentSlot.FEET)) updateProgressItem(attacker,feet,armorXPAmount(value, false, feet));
        }
    }

    public static void updateProgressItem(LivingEntity attacker, ItemStack stack, int updateamount) {
        if (attacker.level().isClientSide) {
            return;
        }

        if(ItemLevelUpdateEvent.PRE.invoker().pre(attacker,stack,updateamount).isTrue()) return;


        int currentlevel = LevelingAPI.getLevel(stack);
        long currentprogress = LevelingAPI.getLevelProgress(stack);
        currentprogress += updateamount;
        if (currentlevel < ModUtils.getMaxLevel(stack) ) {
            long maxprogress = LevelingAPI.getMaxProgress(currentlevel, stack);
            if (currentprogress >= maxprogress) {

                if(!ItemLevelUpdateEvent.LEVEL_UP.invoker().levelUp(attacker,stack,currentlevel,currentprogress,maxprogress).isFalse()) {
                    while (currentprogress >= maxprogress) {
                        currentprogress -= maxprogress;
                        currentlevel++;
                        maxprogress = LevelingAPI.getMaxProgress(currentlevel, stack);
                    }

                    if(!ItemLevelUpdateEvent.SEND_NOTIFICATION.invoker().send(attacker,stack,currentlevel,currentprogress,maxprogress).isFalse()) {
                        if(attacker instanceof Player player) {
                            sendLevelUpNotification(player, stack, currentlevel);
                        } else {
                            attacker.level().playSound(null, attacker.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.HOSTILE, 0.7F, 2.0f);
                        }
                    }

                }
            }
            LevelingAPI.updateLevel(stack,currentlevel);
            LevelingAPI.updateLevelProgress(stack,currentprogress);
        }
    }


    private static int getXPForEntity(LivingEntity killed) {

        int xpamount = WeaponLevelingConfig.kill_xp;

        if(MobXPLoader.isValid(killed.getType())) {
            MobXP mobXP = MobXPLoader.get(BuiltInRegistries.ENTITY_TYPE.getKey(killed.getType()));
            xpamount = mobXP.getAmount();
        }

        if(killed.getType().is(DataGetter.entities_blacklist)) {
            xpamount = 0;
        }

        KillXPGainEvent.PreKillXPGainEvent event = new KillXPGainEvent.PreKillXPGainEvent(killed,xpamount);
        KillXPGainEvent.PRE_GAIN.invoker().accept(event);
        xpamount = event.xpAmount;

        return xpamount;
    }


    private static int getXPForHit(ItemStack stack) {
        int xpamount = 0;
        int amount = ModUtils.getHitXPAmount(stack);
        if (shouldGiveXP(ModUtils.getHitXPChance(stack))) {xpamount = amount;}

        return xpamount;
    }


    private static void sendLevelUpNotification(Player player,ItemStack stack, int level) {
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

    private static boolean shouldGiveXP(int probability) {
        Random random = new Random();
        double randomValue = random.nextDouble(1,100);
        return randomValue <= probability;
    }

    private static int armorXPAmount(int initialxp, boolean taxFree, ItemStack stack) {
        if (taxFree) return initialxp;

        double minAmount = ((double) ModUtils.getWornXPRNGModifier(stack))/100;
        double randomValue = minAmount + (1.0 - minAmount)*Math.random();

        return (int)(initialxp * randomValue);
    }


    public static void updateForKill(LivingEntity victim, DamageSource source, @Nullable ItemStack specificStack) {
        Entity killer = source.getEntity();

        if (source.is(DamageTypes.EXPLOSION)) return;
        if (source.is(DamageTypes.MAGIC)) return;

        if (killer instanceof LivingEntity attacker) {
            ChooseAttackItemEvent event = new ChooseAttackItemEvent(attacker,attacker.getMainHandItem());
            ChooseAttackItemEvent.EVENT.invoker().accept(event);
            ItemStack stack = event.itemStack;

            ItemStack offhandStack = attacker.getOffhandItem();

            int xpamount = LevelingLogic.getXPForEntity(victim);

            if(specificStack != null) {
                updateProgressItem(attacker, specificStack, xpamount);
            } else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                if(ModUtils.isRangedLeveling(stack)) {
                    updateProgressItem(attacker, stack, xpamount);
                }else if(ModUtils.isRangedLeveling(offhandStack)) {
                    updateProgressItem(attacker, offhandStack, xpamount);
                }
            } else if(ModUtils.isMeleeLeveling(stack)) {
                updateProgressItem(attacker,stack,xpamount);
            }

            // For Armor and Passive
            LevelingLogic.applyXPForWorn(attacker,xpamount);
            ItemStack passiveStack = offhandStack == stack ? attacker.getMainHandItem() : offhandStack;
            if (ModUtils.isLevelingAsType(passiveStack, LevelingTypes.PASSIVE_HAND))  updateProgressItem(attacker,passiveStack,armorXPAmount(xpamount, false, passiveStack));
            if(attacker instanceof Player player) {
                ItemStack attackItem = specificStack != null ? specificStack : stack;
                for(var item : player.getInventory().items) {
                    if(item != attackItem && item != passiveStack) {
                        if (ModUtils.isLevelingAsType(item, LevelingTypes.PASSIVE_INV))  updateProgressItem(attacker,item,armorXPAmount(xpamount, false, item));
                    }
                }
            }


        }

    }

    public static void updateForHit(LivingEntity victim, DamageSource source, boolean crit, @Nullable ItemStack specificStack) {
        Entity killer = source.getEntity();


        if (source.is(DamageTypes.EXPLOSION)) return;
        if (source.is(DamageTypes.MAGIC)) return;



        if(killer instanceof LivingEntity attacker) {

            //Event that can cancel the XP Gain
            if(HitXPGainEvent.PRE.invoker().pre(attacker,victim,source,specificStack).isTrue()) return;

            ChooseAttackItemEvent event = new ChooseAttackItemEvent(attacker,attacker.getMainHandItem());
            ChooseAttackItemEvent.EVENT.invoker().accept(event);
            ItemStack stack = event.itemStack;

            if(specificStack != null) {
                LevelingLogic.applyHitXP(specificStack, attacker, victim, crit);
            } else if(source.is(DamageTypeTags.IS_PROJECTILE)) {
                ItemStack mainhand = attacker.getMainHandItem();
                ItemStack offhand = attacker.getOffhandItem();
                if(ModUtils.isRangedLeveling(mainhand)) {
                    LevelingLogic.applyHitXP(mainhand, attacker, victim, crit);
                } else if(ModUtils.isRangedLeveling(offhand)) {
                    LevelingLogic.applyHitXP(offhand, attacker, victim, crit);
                }
            } else if(ModUtils.isMeleeLeveling(stack)) {
                LevelingLogic.applyHitXP(stack, attacker, victim, crit);
            }

        }


    }
}
