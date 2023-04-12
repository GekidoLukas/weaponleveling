package net.weaponleveling.server;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ServerEvents {


    //public static float onLivingHurt(DamageSource damageSource, Entity entity, float damageamount) {
    //    Entity source = damageSource.getEntity();
    //    if (source == null) {
    //        return damageamount;
    //    }
    //    Level level = source.getLevel();
    //    if (level.isClientSide()) {
    //        return damageamount;
    //    }
    //    if (!(source instanceof Player player)) {
    //        return damageamount;
    //    }
    //    ItemStack hand = player.getMainHandItem();
    //    if (damageSource.isMagic()|| damageSource.isExplosion() ) {
    //        return damageamount;
    //    }
    //
    //}
}
