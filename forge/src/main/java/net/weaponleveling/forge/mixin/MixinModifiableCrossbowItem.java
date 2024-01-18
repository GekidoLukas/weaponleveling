package net.weaponleveling.forge.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.weaponleveling.util.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Pseudo //targets = "slimeknights.tconstruct.tools.item.ModifiableCrossbowItem"
@Mixin(targets = "slimeknights.tconstruct.tools.item.ModifiableCrossbowItem")
public class MixinModifiableCrossbowItem {
    @Inject(
            method = "Lslimeknights/tconstruct/tools/item/ModifiableCrossbowItem;fireCrossbow(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setShotFromCrossbow(Z)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void injectedDamage(IToolStackView tool, Player player, InteractionHand hand, CompoundTag heldAmmo, CallbackInfo ci, Level level, int damage, float velocity, float inaccuracy, boolean creative, ItemStack ammo, float startAngle, int primaryIndex, int int1, AbstractArrow abstractarrow) {
        ItemStack stack = player.getItemInHand(hand);
        if(ItemUtils.isAcceptedProjectileWeapon(stack)) {
            double weaponlevelamount = stack.getOrCreateTag().getInt("level");
            weaponlevelamount *= ItemUtils.getWeaponDamagePerLevel(stack) * ItemUtils.getBowlikeModifier(stack);
            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + weaponlevelamount);
        }
    }
}
