package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Pseudo
@Mixin(targets = "se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem")
public abstract class MixinModularCrossbowItem {


    @Inject(
            method = "Lse/mickelus/tetra/items/modular/impl/crossbow/ModularCrossbowItem;fireProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;DZ)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setShotFromCrossbow(Z)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(Level world, ItemStack stack, ItemStack ammoStack, Player player, double yaw, boolean isDupe, CallbackInfo ci, double strength, float velocityBonus, float projectileVelocity, ArrowItem ammoItem, AbstractArrow abstractarrow) {

        if(UpdateLevels.isAcceptedProjectileWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateTag().getInt("level");
            weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get() /2;
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + weaponlevelamount);
        }
    }



}
