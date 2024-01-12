package net.weaponleveling.forge.compat.epicfight;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.UpdateLevels;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class EpicFightMethods {





    public static void updateEpicItem (Player player, int xp) {
        ServerPlayerPatch serverPlayerPatch = (ServerPlayerPatch) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);
        if(serverPlayerPatch.isOffhandItemValid()) {
            ItemStack offhandItem = player.getOffhandItem();
            UpdateLevels.updateProgressItem(player,offhandItem,xp);

        }
    }
}
