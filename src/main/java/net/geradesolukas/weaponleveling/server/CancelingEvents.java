package net.geradesolukas.weaponleveling.server;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class CancelingEvents {


    public static void onBlockBreak() {
        AttackBlockCallback.EVENT.register((((player, world, hand, pos, direction) -> {
            if(!world.isClient) {
                ItemStack stack = player.getStackInHand(hand);
                    if(ItemUtils.isBroken(stack)) {
                        return ActionResult.FAIL;
                    }
                }


                return ActionResult.PASS;
        })));
    }
}
