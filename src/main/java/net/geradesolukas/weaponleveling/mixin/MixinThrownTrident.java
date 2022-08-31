package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrownTrident.class)
public abstract class MixinThrownTrident {

    private static final Logger LOGGER = LogManager.getLogger();
    @Shadow
    private ItemStack tridentItem;

    @ModifyVariable(
            method = "onHitEntity",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/EntityHitResult;getEntity()Lnet/minecraft/world/entity/Entity;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F")
            ),
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0
    )
    private float getAttackDamage(float f, EntityHitResult result) {
        //return baseDamage + BetterImpaling.getAttackDamage(this.tridentStack, result.getEntity());
        if(UpdateLevels.isAcceptedWeapon(tridentItem)) {
            double weaponlevelamount = tridentItem.getOrCreateTag().getInt("level");
            weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get();
            return f += weaponlevelamount;
        } else return f;

    }


    @Inject(
            method = "onHitEntity",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(EntityHitResult result, CallbackInfo ci, Entity entity, float f, Entity entity1, DamageSource damagesource, SoundEvent soundevent, float f1) {
        if(UpdateLevels.isAcceptedWeapon(tridentItem)) {
            UpdateLevels.applyXPForTrident(tridentItem, (Player) entity1, entity);
        }
    }



    //@Inject(
    //        method = "onHitEntity",
    //        at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //private void injectedDamage(EntityHitResult result, CallbackInfo ci, Entity entity, float f, LivingEntity livingentity) {
    //    if(UpdateLevels.isAcceptedWeapon(tridentItem)) {
    //        double weaponlevelamount = tridentItem.getOrCreateTag().getInt("level");
    //        weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get();
    //        f += weaponlevelamount;
    //        LOGGER.info("Does Mixin" + f);
    //    }
    //}


    //@Inject(
    //        method = "releaseUsing",
    //        at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //private void injectedDamage(ItemStack stack, Level level, LivingEntity entity, int i1, CallbackInfo ci, Player player, int i, int j, ThrownTrident throwntrident) {
    //    if(UpdateLevels.isAcceptedWeapon(stack)) {
    //        double weaponlevelamount = stack.getOrCreateTag().getInt("level");
    //        weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get();
    //        throwntrident.setBaseDamage(throwntrident.getBaseDamage() + weaponlevelamount);
    //    }
    //}




}
