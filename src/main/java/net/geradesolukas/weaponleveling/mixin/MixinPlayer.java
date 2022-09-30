package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.geradesolukas.weaponleveling.WeaponLeveling;
import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.event.LivingHurtCallback;
import net.geradesolukas.weaponleveling.server.ServerEvents;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class MixinPlayer {

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/item/ItemStack;postHit(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/player/PlayerEntity;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(Entity target, CallbackInfo ci, float f, float g, float h, boolean bl, boolean bl2, int i, boolean bl3, boolean bl4, double d, float j,boolean bl5, int k,boolean bl6, ItemStack stack, Entity entity) {
        if(UpdateLevels.isAcceptedMeleeWeaponStack(stack)) {
            var player = ((PlayerEntity) ((Object)this) );
            UpdateLevels.applyXPOnItemStack(stack,  player, target, bl3);
            if (bl3) {UpdateLevels.applyXPForArmor(player,UpdateLevels.getXPForCrit());}

        }
    }

    @WrapOperation(
            method = "attack",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"))
    private void wrapReplaceItem(PlayerEntity instance, Hand hand, ItemStack emptystack, Operation<Void> original, Entity pTarget) {
        if (!WeaponlevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, hand, emptystack);
        } else {
            ItemStack stack = instance.getStackInHand(hand);
            stack.setDamage(stack.getMaxDamage() + 1);
            instance.setStackInHand(hand, stack);
        }

        //,  float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5, ItemStack itemstack1, Entity entity, ItemStack copy
    }

    @WrapOperation(
            method = "damageShield",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V", ordinal = 0))
    private void wrapReplaceShield1(PlayerEntity instance, EquipmentSlot slot, ItemStack emptystack, Operation<Void> original) {
        if (!WeaponlevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, slot, emptystack);
        } else {
            ItemStack stack = instance.getEquippedStack(slot);
            stack.setDamage(stack.getMaxDamage() + 1);
            instance.equipStack(slot, stack);
        }
    }

    @WrapOperation(
            method = "damageShield",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V", ordinal = 1))
    private void wrapReplaceShield2(PlayerEntity instance, EquipmentSlot slot, ItemStack emptystack, Operation<Void> original) {
        if (!WeaponlevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, slot, emptystack);
        } else {
            ItemStack stack = instance.getEquippedStack(slot);
            stack.setDamage(stack.getMaxDamage() + 1);
            instance.equipStack(slot, stack);
        }
    }

    @Inject(
            method = "onDeath",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectEventDeath(DamageSource source, CallbackInfo ci) {
        ServerEvents.onLivingDeath(source,((Entity) ((Object)this)));
    }

    @Inject(
            method = "applyDamage",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectEventHurt(DamageSource source, float amount, CallbackInfo ci) {
        WeaponLeveling.LOGGER.info("This is the Pre-Damage " + amount);

        //if (amount <= 0) return;
    }

    @Inject(
            method = "applyDamage",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;applyEnchantmentsToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectEventHurtAfterArmor(DamageSource source, float amount, CallbackInfo ci) {
        WeaponLeveling.LOGGER.info("This is the Armor Post Damage " + amount);
    }

    @Inject(
            method = "applyDamage",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;applyEnchantmentsToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F",shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectEventHurtAfterAbsorp(DamageSource source, float amount, CallbackInfo ci) {
        WeaponLeveling.LOGGER.info("This is the Absorb Post Damage " + amount);

    }
    @Inject(
            method = "applyDamage",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectEventHurt2(DamageSource source, float amount, CallbackInfo ci) {
        WeaponLeveling.LOGGER.info("This is the Post-Damage " + amount);
        //float realdamage = amount;
        //realdamage = ServerEvents.onLivingHurt(source,((Entity) ((Object)this)),amount);
        //realdamage = ServerEvents.onPlayerHurt(source,((Entity) ((Object)this)),amount);
        //LivingHurtCallback.EVENT.invoker().hurt(((LivingEntity) (Object) this), source, amount);
        WeaponLeveling.LOGGER.info("This is the Damage " + amount);

    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isAttackable()Z"))
    private boolean modifyshouldAttack(boolean original, Entity entity) {
        PlayerEntity player = ((PlayerEntity) ((Object) this));
            return original && !ItemUtils.isBroken(player.getMainHandStack());
    }


    @WrapOperation(
            method = "applyDamage",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V"))
    private void setHealthWrap(PlayerEntity instance, float health, Operation<Void> original, DamageSource source, float amount) {
        float realdamage = amount;
        realdamage = ServerEvents.onLivingHurt(source,((Entity) ((Object)this)),amount);
        realdamage = ServerEvents.onPlayerHurt(source,((Entity) ((Object)this)),amount);
        original.call(instance,instance.getHealth() - realdamage);
    }
}
