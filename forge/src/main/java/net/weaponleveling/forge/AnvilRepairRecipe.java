package net.weaponleveling.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.util.ModUtils;

@Mod.EventBusSubscriber(modid = WeaponLevelingMod.MODID)
public class AnvilRepairRecipe {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if(WLPlatformGetter.getBrokenItemsDontVanish() && ModUtils.isBroken(event.getLeft()) && event.getLeft().getItem().isValidRepairItem(event.getLeft(), event.getRight())) {
            ItemStack newResult = event.getLeft().copy();
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("isBroken", false);
            newResult.setTag(tag);
            newResult.setDamageValue(newResult.getMaxDamage()-1);
            event.setOutput(newResult);
            event.setCost(1);
            event.setMaterialCost(1);
        }
    }
}
