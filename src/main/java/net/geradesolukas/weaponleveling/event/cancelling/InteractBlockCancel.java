package net.geradesolukas.weaponleveling.event.cancelling;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class InteractBlockCancel implements UseBlockCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if(!world.isClient) {
            ItemStack stack = player.getStackInHand(hand);
            if(ItemUtils.isBroken(stack)) {
                return ActionResult.success(false);
            } else return ActionResult.PASS;
        }
        return ActionResult.PASS;
    }

}
