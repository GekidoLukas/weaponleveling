package net.weaponleveling.neoforge;

import net.minecraft.world.item.ItemStack;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.item.BrokenItem;
import net.weaponleveling.item.ModItems;

@EventBusSubscriber(modid = WeaponLevelingMod.MODID)
public class AnvilRepairRecipe {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if(event.getLeft().is(ModItems.BROKEN_ITEM.get()) && BrokenItem.getContainedItem(event.getLeft()).getItem().isValidRepairItem(BrokenItem.getContainedItem(event.getLeft()),event.getRight())) {
            ItemStack output = BrokenItem.getContainedItem(event.getLeft());
            output.setCount(1);
            output.setDamageValue(BrokenItem.getContainedItem(event.getLeft()).getMaxDamage()-1);
            event.setOutput(output);
            event.setCost(1);
            event.setMaterialCost(1);
        }

    }
}
