package net.geradesolukas.weaponleveling.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = Player.class, priority = 1500)
public class MixinPlayer_BETTERCOMBAT {


    //@ModifyArg(
    //        method = "attack",
    //        at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"),index = 2)
    //private ItemStack replaceEmpty(ItemStack emptystack) {
    //    ItemStack stack = ((PlayerEntity) ((Object)this) ).getMainHandStack();
    //    stack.setDamage(stack.getMaxDamage() + 2);
    //    return stack;
    //}

    @ModifyArg(
            method = "attack",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"),index = 2)
    private ItemStack replaceEmpty(ItemStack emptystack) {
        ItemStack stack = ((Player) ((Object)this) ).getMainHandItem();
        stack.setDamageValue(stack.getMaxDamage() + 1);
        return stack;
    }

}
