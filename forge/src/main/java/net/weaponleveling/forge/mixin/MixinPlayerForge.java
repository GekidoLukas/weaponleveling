package net.weaponleveling.forge.mixin;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.util.ItemUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public class MixinPlayerForge {

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSprinting()Z", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedCritXP(Entity target, CallbackInfo ci, float f, float g, float h, boolean bl, boolean bl2, float i, boolean crit) {
        Player player = ((Player) ((Object) this));
        ItemStack stack = player.getMainHandItem();
        if(ItemUtils.isAcceptedMeleeWeaponStack(stack) && crit) {
            UpdateLevels.applyXPOnItemStack(stack,  player, target, crit);
            UpdateLevels.applyXPForArmorItem(player, crit, stack);
        }
    }
}
