package net.weaponleveling.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.weaponleveling.util.ItemUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrownTrident.class)
public class MixinThrownTrident {
    @Shadow
    private ItemStack tridentItem;

    @Inject(
            method = "onHitEntity",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedLevel(EntityHitResult result, CallbackInfo ci, Entity entity, float f, Entity entity1, DamageSource damagesource, SoundEvent soundevent, float f1) {
        if(ItemUtils.isAcceptedProjectileWeapon(tridentItem) && entity1 instanceof Player) {
            UpdateLevels.applyXPOnItemStack(tridentItem, (Player) entity1, entity, false);
        }
    }

    @ModifyArg(
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    public float hurtModify(float f) {
        if(ItemUtils.isAcceptedProjectileWeapon(tridentItem)) {
            double weaponlevelamount = tridentItem.getOrCreateTag().getInt("level");
            weaponlevelamount *= ItemUtils.getWeaponDamagePerLevel(tridentItem);
            return f + (float) weaponlevelamount;
        }
        return f;
    }
    //@Inject(
    //        method = "onHitEntity",
    //        at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //private void injectedDamage(EntityHitResult result, CallbackInfo ci, Entity entity, float f) {
    //    Entity entity1 = ((ThrownTrident) ((Object)this) ).getOwner();
    //    if(ItemUtils.isAcceptedProjectileWeapon(tridentItem) && entity1 instanceof Player) {
    //        double weaponlevelamount = tridentItem.getOrCreateTag().getInt("level");
    //        weaponlevelamount *= ItemUtils.getWeaponDamagePerLevel(tridentItem);
    //        f += weaponlevelamount;
    //    }
    //}
}
