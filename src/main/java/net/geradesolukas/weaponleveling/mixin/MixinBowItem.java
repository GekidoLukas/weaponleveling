package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public abstract class MixinBowItem {


    @Inject(
            method = "releaseUsing",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(ItemStack stack, Level level, LivingEntity entity, int i1, CallbackInfo ci, Player player, boolean flag, ItemStack itemstack, int i, float f, boolean flag1, ArrowItem arrowitem, AbstractArrow abstractarrow) {
        if(UpdateLevels.isAcceptedWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateTag().getInt("level");
            weaponlevelamount *= WeaponLevelingConfig.Server.value_damage_per_level.get() /2;
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + weaponlevelamount);
        }
    }




}
