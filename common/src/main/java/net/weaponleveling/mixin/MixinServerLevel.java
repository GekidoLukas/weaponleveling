package net.weaponleveling.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.util.AbstractArrowAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class MixinServerLevel {


    @Inject(method = "addEntity", at = @At("HEAD"))
    private void pre_spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        ServerLevel level = (ServerLevel) (Object)this;
        if(entity instanceof AbstractArrow projectile && EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
            if (projectile.getOwner() != null && projectile.getOwner() instanceof LivingEntity owner) {


                var projectileDamage = owner.getAttributeValue(WLAttributes.RANGED_DAMAGE);
                if(projectile instanceof ThrownTrident) {
                    projectile.setBaseDamage(projectileDamage);
                } else {
                    ItemStack stack = ((AbstractArrowAccessor)projectile).getSourceWeapon();
                    if(stack != null) {
                        var registryAccess = level.registryAccess();
                        Holder<Enchantment> powerHolder = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                                .getOrThrow(Enchantments.POWER);

                        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(powerHolder, stack);

                        projectile.setBaseDamage((projectileDamage /4) + (double) (powerLevel + 1) /2);
                    } else {
                        projectile.setBaseDamage(projectileDamage /4);
                    }

                }

            }
        }
    }
}
