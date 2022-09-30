package net.geradesolukas.weaponleveling.mixin;


import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public abstract class MixinCrossbowItem {

    @Inject(
            method = "createArrow",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setShotFromCrossbow(Z)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void injectedDamage(World world, LivingEntity entity, ItemStack crossbow, ItemStack stack, CallbackInfoReturnable<PersistentProjectileEntity> cir, ArrowItem arrowitem, PersistentProjectileEntity abstractarrow) {
        if(UpdateLevels.isAcceptedProjectileWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateNbt().getInt("level");
            weaponlevelamount *= WeaponlevelingConfig.Server.value_damage_per_level.get();
            abstractarrow.setDamage(abstractarrow.getDamage() + weaponlevelamount);
        }
    }


}
