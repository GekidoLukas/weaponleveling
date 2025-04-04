package net.weaponleveling.fabric.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.item.BrokenItem;
import net.weaponleveling.item.ModItems;
import net.weaponleveling.util.DataGetter;
import net.weaponleveling.util.ModUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilMenuFabric extends ItemCombinerMenu {


    @Shadow public int repairItemCountCost;

    public MixinAnvilMenuFabric(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(
            method = "createResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void unBreakItem(CallbackInfo ci, ItemStack itemStack, int i, int j, int k, ItemStack left, ItemStack right, Map map) {
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
