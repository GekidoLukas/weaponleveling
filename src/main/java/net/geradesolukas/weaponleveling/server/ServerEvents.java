package net.geradesolukas.weaponleveling.server;


import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.server.command.ItemLevelCommand;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponLeveling.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {


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
            int xpamount = UpdateLevels.getXPForEntity(dyingEntity);
            if (event.getSource().isProjectile()) {
                ItemStack stack = player.getMainHandItem();
                UpdateLevels.updateProgressItem(player, stack, xpamount);
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
        UpdateLevels.applyXPForArmor(player,UpdateLevels.getXPForHit());
        //Apply Damage + Bow XP
        if(UpdateLevels.isAcceptedWeapon(hand)) {
            if(event.getSource().isProjectile()) {
                int xpamount = 0;
                int amount = WeaponLevelingConfig.Server.value_hit_xp_amount.get();
                if (UpdateLevels.shouldGiveHitXP(WeaponLevelingConfig.Server.value_hit_percentage.get())) {xpamount = amount;}
                UpdateLevels.updateProgressItem(player, hand, xpamount);
            }

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
                        //For guns
                        //event.setAmount(damage + (float)extradamage);
                    }
                } else  {
                    return;
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
