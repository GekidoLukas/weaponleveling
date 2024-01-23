package net.weaponleveling.forge.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
@Pseudo
@Mixin(targets = "slimeknights.tconstruct.tools.item.ModifiableBowItem")
public class MixinModifiableBowItem {
    @Inject(
            method = "Lslimeknights/tconstruct/tools/item/ModifiableBowItem;releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(ItemStack stack, Level level, LivingEntity entity, int i1, CallbackInfo ci, Player player, ToolStack toolStack, boolean flag1, boolean flag2, int i2, float float1, float float2, float float3, ItemStack itemStack, ArrowItem arrowItem, float float4, float float5, int int2, int int3, AbstractArrow abstractarrow) {
        if(ModUtils.isAcceptedProjectileWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateTag().getInt("level");
            weaponlevelamount *= ModUtils.getWeaponDamagePerLevel(stack) * ModUtils.getBowlikeModifier(stack);
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + weaponlevelamount);
        }
    }
}
