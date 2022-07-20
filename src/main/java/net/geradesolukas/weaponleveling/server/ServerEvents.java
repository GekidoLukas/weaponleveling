package net.geradesolukas.weaponleveling.server;


import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

@Mod.EventBusSubscriber(modid = WeaponLeveling.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {
    //private static final Logger LOGGER = LogManager.getLogger();


    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Entity attacker = event.getEntity();


        if(attacker instanceof Player player) {
            if(!player.getLevel().isClientSide) {
                InteractionHand interhand = InteractionHand.MAIN_HAND;
                //if (ModList.get().isLoaded("epicfight") ) {
                //    interhand = InteractionHand.MAIN_HAND
                //}

                ItemStack hand = player.getItemInHand(interhand);
                if (UpdateLevels.isAcceptedItem(hand) && player.getAttackStrengthScale(0.0F) == 1.0f) {


                    UpdateLevels updateLevels = new UpdateLevels();
                    Random random = new Random();
                    int damageamount = 0;
                    int chance = 100/WeaponLevelingConfig.value_hit_percentage.get() - 1;
                    int amount = WeaponLevelingConfig.value_hit_xp_amount.get();

                    if (random.nextInt(chance) == 0) {damageamount = amount;}
                    updateLevels.updateProgress(player,hand,damageamount);

                }

            }
        }
    }


    @SubscribeEvent
    public static void onKillEntity(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        Entity dyingEntity = event.getEntity();



        if(killer instanceof Player player) {

            String name = ForgeRegistries.ENTITIES.getKey(dyingEntity.getType()).toString();

            int xpamount = WeaponLevelingConfig.value_kill_generic.get();
            if(WeaponLevelingConfig.entities_miniboss.get().contains(name)) {
                xpamount = WeaponLevelingConfig.value_kill_miniboss.get();
            } else if(WeaponLevelingConfig.entities_boss.get().contains(name)) {
                xpamount = WeaponLevelingConfig.value_kill_boss.get();
            } else if(WeaponLevelingConfig.entities_animal.get().contains(name)) {
                xpamount = WeaponLevelingConfig.value_kill_animal.get();
            }   else if(WeaponLevelingConfig.entities_monster.get().contains(name)) {
                xpamount = WeaponLevelingConfig.value_kill_monster.get();
            }

            ItemStack hand = player.getMainHandItem();
            if(UpdateLevels.isAcceptedItem(hand)) {
                UpdateLevels updateLevels = new UpdateLevels();
                updateLevels.updateProgress(player,hand,xpamount);
            }

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

        ItemStack hand = player.getMainHandItem();

        if(UpdateLevels.isAcceptedItem(hand)) {

            CompoundTag nbttag = hand.getOrCreateTag();

            if(nbttag.contains("level")) {
                int level = nbttag.getInt("level") ;
                float damage = event.getAmount();
                double extradamage = level;
                extradamage *= WeaponLevelingConfig.value_damage_per_level.get();
                event.setAmount(damage + (float)extradamage);

                //player.sendMessage(new TextComponent("Damage is: " + extradamage), player.getUUID());


            }

        }
    }



}
