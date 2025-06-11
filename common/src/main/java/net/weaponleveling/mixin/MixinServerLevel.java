package net.weaponleveling.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.attribute.WLAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class MixinServerLevel {


    @Inject(method = "addEntity", at = @At("HEAD"))
    private void pre_spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof AbstractArrow projectile && EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
            if (projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity owner) {


                var projectileDamage = owner.getAttributeValue(WLAttributes.RANGED_DAMAGE);
                if(projectile instanceof ThrownTrident) {
                    projectile.setBaseDamage(projectileDamage);
                } else {
                    projectile.setBaseDamage(projectileDamage /4);
                }

            }
        }
    }
}
