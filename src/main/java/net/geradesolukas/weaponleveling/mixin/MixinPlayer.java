package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.entity.Entity;
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

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDamage(Entity target, CallbackInfo ci, float f, float f1, float f2, boolean flag, boolean flag1, float i, boolean flag2, CriticalHitEvent hitResult, boolean flag3, double d0, float f4, boolean flag4, int j, Vec3 vec3, boolean flag5, ItemStack stack, Entity entity) {
        if(UpdateLevels.isAcceptedMeleeWeapon(stack) && !(stack.getItem() instanceof ProjectileWeaponItem || UpdateLevels.isAcceptedProjectileWeapon(stack))) {
            var player = ((Player) ((Object)this) );
            UpdateLevels.applyXPOnItemStack(stack,  player, target);

        }
    }

}
