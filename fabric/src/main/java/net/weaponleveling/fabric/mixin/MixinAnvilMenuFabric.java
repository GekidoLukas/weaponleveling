package net.weaponleveling.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.item.BrokenItem;
import net.weaponleveling.item.ModItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenuFabric extends ItemCombinerMenu {


    @Shadow public int repairItemCountCost;

    public MixinAnvilMenuFabric(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(
            method = "createResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0), cancellable = true)
    private void unBreakItem(CallbackInfo ci, @Local(ordinal = 1) ItemStack left, @Local(ordinal = 2) ItemStack right) {
        ItemStack containedStack = BrokenItem.getContainedItem(left);
        if(left.is(ModItems.BROKEN_ITEM.get()) && containedStack.getItem().isValidRepairItem(containedStack,right)) {
            ItemStack output = containedStack;
            output.setCount(1);
            output.setDamageValue(containedStack.getMaxDamage()-1);
            this.resultSlots.setItem(0,output);
            this.repairItemCountCost = 1;
            ci.cancel();
        }

    }
}
