package net.weaponleveling.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.item.BrokenItem;
import net.weaponleveling.item.ModItems;
import net.weaponleveling.util.DataGetter;
import net.weaponleveling.util.ModUtils;

@Mod.EventBusSubscriber(modid = WeaponLevelingMod.MODID)
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
