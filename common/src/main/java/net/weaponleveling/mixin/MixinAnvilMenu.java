package net.weaponleveling.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WLConfigGetter;
import net.weaponleveling.util.ItemUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenu extends ItemCombinerMenu {


    @Shadow public int repairItemCountCost;

    public MixinAnvilMenu(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(
            method = "createResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void unBreakItem(CallbackInfo ci, ItemStack itemStack, int i, int j, int k, ItemStack itemStack2, ItemStack itemStack3, Map map) {

        if(WLConfigGetter.getBrokenItemsDontVanish() && ItemUtils.isBroken(itemStack2) && itemStack2.getItem().isValidRepairItem(itemStack, itemStack3)) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("isBroken", false);
            itemStack2.setTag(tag);
            itemStack2.setDamageValue(itemStack2.getMaxDamage()-1);
            this.resultSlots.setItem(0, itemStack2);
            this.repairItemCountCost = 1;
           ci.cancel();
        }
    }
}
