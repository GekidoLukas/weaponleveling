package net.geradesolukas.weaponleveling.event.cancelling;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class InteractItemCancel implements UseItemCallback {


    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(!world.isClient) {
            if(ItemUtils.isBroken(stack)) {
                return TypedActionResult.success(stack,false);
            } else return TypedActionResult.pass(stack);
        }
        return TypedActionResult.pass(stack);
    }
}
