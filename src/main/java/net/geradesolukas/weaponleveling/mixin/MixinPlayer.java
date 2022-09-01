package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public class MixinPlayer {

    //@ModifyExpressionValue(
    //        method = "attack",
    //        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F")),
    //        at = @At(value = "STORE", ordinal = 0)
    //)
    //private float injectedDamage(float bonus, CallbackInfo <ItemStack, MobType> callbackInfo) {
    //    float weaponlevelamount = stack.getOrCreateTag().getInt("level");
//
    //    weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get();
    //    bonus += weaponlevelamount;
    //    return bonus;
    //}

    //@WrapOperation(
    //        method = "attack",
    //        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"))
    //private float injectedDamage(ItemStack stack, MobType type, Operation<Float> original) {
    //    float weaponlevelamount = 0;
    //    if (UpdateLevels.isAcceptedWeapon(stack) && !(stack.getItem() instanceof ProjectileWeaponItem || UpdateLevels.isNoMelee(stack))) {
    //        weaponlevelamount = stack.getOrCreateTag().getInt("level");
    //        weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get();
    //    }
    //    return original.call(stack, type) + weaponlevelamount;
    //}

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(Entity target, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5, ItemStack stack, Entity entity) {
        if(UpdateLevels.isAcceptedWeapon(stack) && !(stack.getItem() instanceof ProjectileWeaponItem || UpdateLevels.isNoMelee(stack))) {
            var player = ((Player) ((Object)this) );
            UpdateLevels.applyXPOnAttack(stack,  player, target);

        }
    }

}
