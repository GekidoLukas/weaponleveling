package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Either;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public abstract class MixinPlayer {


    @Inject(
            method = "attack",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(Entity target, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5, ItemStack stack, Entity entity) {
        if(UpdateLevels.isAcceptedMeleeWeaponStack(stack)) {
            var player = ((Player) ((Object)this) );
            UpdateLevels.applyXPOnItemStack(stack,  player, target);
        }
    }

    //@Unique
    //private Player storedPlayerAttack = null;
    //@Unique
    //private ItemStack storedItemStackAttack = null;
    //@Unique
    //private InteractionHand storedInteractionHandAttack = null;

    //@WrapOperation(
    //        method = "attack",
    //        at = @At(value = "INVOKE",  target = "Lnet/minecraftforge/event/ForgeEventFactory;onPlayerDestroyItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)V"))
    //private void wrapEventFactory(Player player, ItemStack stack, InteractionHand hand, Operation<Void> operation ) {
    //    storedPlayerAttack = player;
    //    storedItemStackAttack = stack;
    //    storedInteractionHandAttack = hand;
    //}

    //@Inject(
    //        method = "attack",
    //        at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //private void injectEventFactory(Entity pTarget, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5, ItemStack itemstack1, Entity entity, ItemStack copy) {
//
    //}
    @WrapOperation(
            method = "attack",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"))
    private void wrapReplaceItem(Player instance, InteractionHand hand, ItemStack emptystack, Operation<Void> original, Entity pTarget) {
        if (!WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, hand, emptystack);
        } else {
            ItemStack stack = instance.getItemInHand(hand);
            stack.setDamageValue(stack.getMaxDamage() + 1);
            instance.setItemInHand(hand, stack);
        }

        //,  float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5, ItemStack itemstack1, Entity entity, ItemStack copy
    }

    @WrapOperation(
            method = "hurtCurrentlyUsedShield",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V", ordinal = 0))
    private void wrapReplaceShield1(Player instance, EquipmentSlot slot, ItemStack emptystack, Operation<Void> original, float pDamage) {
        if (!WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, slot, emptystack);
        } else {
            ItemStack stack = instance.getItemBySlot(slot);
            stack.setDamageValue(stack.getMaxDamage() + 1);
            instance.setItemSlot(slot, stack);
        }
    }

    @WrapOperation(
            method = "hurtCurrentlyUsedShield",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V", ordinal = 1))
    private void wrapReplaceShield2(Player instance, EquipmentSlot slot, ItemStack emptystack, Operation<Void> original, float pDamage) {
        if (!WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, slot, emptystack);
        } else {
            ItemStack stack = instance.getItemBySlot(slot);
            stack.setDamageValue(stack.getMaxDamage() + 1);
            instance.setItemSlot(slot, stack);
        }
    }

}
