package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Pseudo
@Mixin(targets = "com.theishiopian.parrying.Mechanics.ArmorPenetrationMechanic")
public abstract class MixinArmorPenetrationMechanic {


    @Inject(
            method = "Lcom/theishiopian/parrying/Mechanics/ArmorPenetrationMechanic;PostAttackHelper(Lnet/minecraft/world/entity/player/Player;FFZLnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;F)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void injectedDamage(Player player, float boost, float attackStrength, boolean critical, Entity target, ItemStack stack, float damageDone, CallbackInfo ci, boolean attackScale) {
        if(UpdateLevels.isAcceptedMeleeWeaponStack(stack)) {
            UpdateLevels.applyXPOnItemStack(stack,  player, target, critical);
            if (critical) {UpdateLevels.applyXPForArmor(player,UpdateLevels.getXPForCrit());}

        }
    }



}
