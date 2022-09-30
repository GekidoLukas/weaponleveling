package net.geradesolukas.weaponleveling.server;

import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.event.LivingHurtCallback;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import static net.geradesolukas.weaponleveling.util.UpdateLevels.updateProgressItem;

public class ServerEvents {




    public static void onLivingDeath(DamageSource source, Entity dyingEntity) {
        Entity killer = source.getSource();
        if (source.isExplosive()) return;
        if (source.isMagic()) return;
        if(killer instanceof PlayerEntity player) {
            ItemStack stack = player.getMainHandStack();
            int xpamount = UpdateLevels.getXPForEntity(dyingEntity);
            if (source.isProjectile()) {
                updateProgressItem(player, stack, xpamount);
            }
            UpdateLevels.applyXPForArmor(player,xpamount);
        }
    }


    public static float onLivingHurt(DamageSource damageSource, Entity target, float damageamount) {
        Entity source = damageSource.getSource();
        if (source == null) {
            return damageamount;
        }
        World world = source.getWorld();

        if (world.isClient) {
            return damageamount;
        }

        if (!(source instanceof PlayerEntity player)) {
            return damageamount;
        }
        ItemStack hand = player.getMainHandStack();
        if (damageSource.isMagic()|| damageSource.isExplosive() ) {
            return damageamount;
        }

        UpdateLevels.applyXPForArmor(player,UpdateLevels.getXPForHit());
        //Apply Damage + Bow XP
        if(UpdateLevels.isAcceptedMeleeWeaponStack(hand) || UpdateLevels.isAcceptedProjectileWeapon(hand)) {
            if(damageSource.isProjectile()) {
                int xpamount = 0;
                int amount = WeaponlevelingConfig.Server.value_hit_xp_amount.get();
                if (UpdateLevels.shouldGiveHitXP(WeaponlevelingConfig.Server.value_hit_percentage.get())) {xpamount = amount;}
                updateProgressItem(player, hand, xpamount);
            }

            NbtCompound nbttag = hand.getOrCreateNbt();

            if(nbttag.contains("level")) {
                int level = nbttag.getInt("level") ;
                double extradamage = level;
                extradamage *= WeaponlevelingConfig.Server.value_damage_per_level.get();


                if (damageSource.isProjectile()) {
                    if (damageSource.getAttacker() instanceof PersistentProjectileEntity) {
                        return damageamount;
                    } else {
                        //For guns
                        return (damageamount + (float)extradamage);
                    }
                } else  {
                    return damageamount;
                }

            }

        }
        return damageamount;
    }


    public static float onPlayerHurt(DamageSource damageSource, Entity target, float damageamount) {
        World world = target.getWorld();
        if (world.isClient) {
            return damageamount;
        }
        if (target instanceof PlayerEntity player) {
            return (UpdateLevels.reduceDamageArmor(player,damageamount));
        }
        return damageamount;
    }

    //public static void onPlayerHurt() {
    //    LivingHurtCallback.EVENT.register(((entity, source, damage) -> {
    //        World world = entity.getWorld();
    //        if(world.isClient) {
    //            return ActionResult.PASS;
    //        }
    //        if(entity instanceof PlayerEntity player) {
    //            damage = (UpdateLevels.reduceDamageArmor(player,damage));
    //            return
    //        }
    //        return ActionResult.PASS;
    //    }));
    //}
}
