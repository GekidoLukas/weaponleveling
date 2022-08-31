package net.geradesolukas.weaponleveling.server;


import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.server.command.ItemLevelCommand;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.jar.Attributes;

@Mod.EventBusSubscriber(modid = WeaponLeveling.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {


    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new ItemLevelCommand(event.getDispatcher());
    }
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Entity attacker = event.getEntity();


        if(attacker instanceof Player player) {
            if(!player.getLevel().isClientSide) {
                InteractionHand interhand = InteractionHand.MAIN_HAND;

                ItemStack hand = player.getItemInHand(interhand);

                UpdateLevels updateLevels = new UpdateLevels();
                if ((UpdateLevels.isAcceptedWeapon(hand) && player.getAttackStrengthScale(0.0F) == 1.0f)|| player.getItemBySlot(EquipmentSlot.HEAD) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.CHEST) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY || player.getItemBySlot(EquipmentSlot.LEGS) != ItemStack.EMPTY ) {
                    int xpamount = 0;
                    int amount = WeaponLevelingConfig.Server.value_hit_xp_amount.get();
                    if (updateLevels.shouldGiveHitXP(WeaponLevelingConfig.Server.value_hit_percentage.get())) {xpamount = amount;}
                    updateLevels.applyExperience(player,xpamount);

                }


            }
        }
    }
    @SubscribeEvent
    public static void onKillEntity(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        Entity dyingEntity = event.getEntity();
        if (event.getSource().isExplosion()) return;
        if (event.getSource().isMagic()) return;

        if(killer instanceof Player player) {

            String name = ForgeRegistries.ENTITIES.getKey(dyingEntity.getType()).toString();

            int xpamount = WeaponLevelingConfig.Server.value_kill_generic.get();
            if(WeaponLevelingConfig.Server.entities_miniboss.get().contains(name)) {
                xpamount = WeaponLevelingConfig.Server.value_kill_miniboss.get();
            } else if(WeaponLevelingConfig.Server.entities_boss.get().contains(name)) {
                xpamount = WeaponLevelingConfig.Server.value_kill_boss.get();
            } else if(WeaponLevelingConfig.Server.entities_animal.get().contains(name)) {
                xpamount = WeaponLevelingConfig.Server.value_kill_animal.get();
            }   else if(WeaponLevelingConfig.Server.entities_monster.get().contains(name)) {
                xpamount = WeaponLevelingConfig.Server.value_kill_monster.get();
            }
            UpdateLevels updateLevels = new UpdateLevels();
            updateLevels.applyExperience(player,xpamount);

        }
    }



    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
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



        //Apply Damage
        ItemStack hand = player.getMainHandItem();
        if(UpdateLevels.isAcceptedWeapon(hand)) {

            CompoundTag nbttag = hand.getOrCreateTag();

            if(nbttag.contains("level")) {
                int level = nbttag.getInt("level") ;
                float damage = event.getAmount();
                double extradamage = level;
                extradamage *= WeaponLevelingConfig.Server.value_damage_per_level.get();


                if (event.getSource().isProjectile()) {

                    
                    if (event.getSource().getDirectEntity() instanceof AbstractArrow) {
                        return;
                    } else {
                        event.setAmount(damage + (float)extradamage);
                    }


                } else if(event.getSource().isMagic()|| event.getSource().isExplosion() || hand.getItem() instanceof ProjectileWeaponItem || UpdateLevels.isNoMelee(hand)){
                    return;
                } else  {
                    event.setAmount(damage + (float)extradamage);
                }


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
