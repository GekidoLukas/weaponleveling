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
@Mixin(targets = "se.mickelus.tetra.items.modular.impl.bow.ModularBowItem")
public abstract class MixinModularBowItem {


    @Inject(
            method = "Lse/mickelus/tetra/items/modular/impl/bow/ModularBowItem;fireArrow(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(ItemStack stack, Level world, LivingEntity entity, int timeLeft, CallbackInfo ci, Player player, ItemStack ammoStack, boolean playerInfinite, int drawProgress, double strength, float velocityBonus, int suspendLevel, float projectileVelocity, ArrowItem ammoItem, boolean infiniteAmmo, int count, double spread, int powerLevel, int punchLevel, int flameLevel, int piercingLevel, int i, double yaw, AbstractArrow abstractarrow) {
        if(UpdateLevels.isAcceptedProjectileWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateTag().getInt("level");
            weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get() /2;
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + weaponlevelamount);
        }
    }




}
