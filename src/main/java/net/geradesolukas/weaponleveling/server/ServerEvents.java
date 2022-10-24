package net.geradesolukas.weaponleveling.server;


import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.compat.tconstruct.TinkersCompat;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.data.LevelableItem;
import net.geradesolukas.weaponleveling.data.LevelableItemsLoader;
import net.geradesolukas.weaponleveling.data.gen.LevelableJsonProvider;
import net.geradesolukas.weaponleveling.server.command.ItemLevelCommand;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.UUID;

import static net.geradesolukas.weaponleveling.util.UpdateLevels.updateProgressItem;

@Mod.EventBusSubscriber(modid = WeaponLeveling.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    @SubscribeEvent
    public static void onreloadListen(AddReloadListenerEvent event) {
        event.addListener(new LevelableItemsLoader());
    }
    @SubscribeEvent
    public static void onDataGather(GatherDataEvent event) {
        //event.getGenerator().addProvider(new LevelableJsonProvider());
        }
    @SubscribeEvent
    public static void onAttribute(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();


        EquipmentSlot slot = event.getSlotType();
        if (TinkersCompat.isTinkersItem(stack)) {
            double extradamage = stack.getOrCreateTag().getInt("level") * ItemUtils.getWeaponDamagePerLevel(stack);
            if (event.getSlotType() == EquipmentSlot.MAINHAND) {
                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString(WeaponLeveling.DAMAGEUUID),"leveldamage", extradamage, AttributeModifier.Operation.ADDITION));
            }
        }
    }
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntity() instanceof  Player player) {
            Entity target = event.getTarget();
            ItemStack stack = player.getMainHandItem();

            if(ItemUtils.isAcceptedMeleeWeaponStack(stack) && TinkersCompat.isTinkersItem(stack)) {
                UpdateLevels.applyXPOnItemStack(stack,  player, target, false);
            }
        }

    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new ItemLevelCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onKillEntity(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        Entity dyingEntity = event.getEntity();
        if (event.getSource().isExplosion()) return;
        if (event.getSource().isMagic()) return;


        if(killer instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            if(ItemUtils.isAcceptedMeleeWeaponStack(stack) && TinkersCompat.isTinkersItem(stack)) {
                int xpamount = UpdateLevels.getXPForEntity(dyingEntity);
                updateProgressItem(player,stack,xpamount);
            }

            int xpamount = UpdateLevels.getXPForEntity(dyingEntity);
            if (event.getSource().isProjectile() && ItemUtils.isAcceptedProjectileWeapon(stack)) {
                updateProgressItem(player, stack, xpamount);
            }
            UpdateLevels.applyXPForArmor(player,xpamount);
        }
    }



    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        Entity target = event.getEntityLiving();
        if (source == null) {
            return;
        }
        Level world = source.getCommandSenderWorld();

        if (world.isClientSide) {
            return;
        }

        if (!(source instanceof Player player)) {
            return;
        }
        ItemStack hand = player.getMainHandItem();
        //source.sendMessage(new TextComponent("The damage is " + event.getAmount()), source.getUUID());
        if (event.getSource().isMagic()|| event.getSource().isExplosion() ) {
            return;
        }

        UpdateLevels.applyXPForArmor(player,UpdateLevels.getXPForHit(hand));
        //Apply Damage + Bow XP
        if(ItemUtils.isAcceptedMeleeWeaponStack(hand) || ItemUtils.isAcceptedProjectileWeapon(hand)) {
            if(event.getSource().isProjectile()) {
                int xpamount = 0;
                int amount = ItemUtils.getHitXPAmount(hand);
                if (UpdateLevels.shouldGiveHitXP(ItemUtils.getCritXPChance(hand))) {xpamount = amount;}
                updateProgressItem(player, hand, xpamount);
            }

            CompoundTag nbttag = hand.getOrCreateTag();

            if(nbttag.contains("level")) {
                int level = nbttag.getInt("level") ;
                float damage = event.getAmount();
                double extradamage = level;
                extradamage *= ItemUtils.getWeaponDamagePerLevel(hand);


                //if (event.getSource().isProjectile()) {
                //    if (event.getSource().getDirectEntity() instanceof AbstractArrow) {
                //        return;
                //    } else {
                //        //For guns
                //        //event.setAmount(damage + (float)extradamage);
                //    }
                //} else  {
                //    return;
                //}

            }

        }

    }
    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        Entity target = event.getEntity();
        Level world = target.getCommandSenderWorld();
        float damage = event.getAmount();
        if (world.isClientSide) {
            return;
        }
        if (target instanceof Player player) {
            event.setAmount(UpdateLevels.reduceDamageArmor(player,damage));
        }
    }

}
