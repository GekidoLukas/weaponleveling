package net.geradesolukas.weaponleveling.mixin;


import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public abstract class MixinBowItem {


    @Inject(
            method = "onStoppedUsing",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, PlayerEntity playerEntity, boolean bl, ItemStack itemStack, int i, float f, boolean bl2, ArrowItem arrowItem, PersistentProjectileEntity persistentProjectileEntity) {
        if(UpdateLevels.isAcceptedProjectileWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateNbt().getInt("level");
            weaponlevelamount *= WeaponlevelingConfig.Server.value_damage_per_level.get() /2;
            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + weaponlevelamount);
        }
    }

    @Inject(
            method = "onStoppedUsing",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;getArrowType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectStop(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, PlayerEntity playerEntity, boolean bl) {
        if(ItemUtils.isBroken(stack)) return;
    }




}
