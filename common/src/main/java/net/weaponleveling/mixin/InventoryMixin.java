package net.weaponleveling.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.weaponleveling.api.event.ItemReplaceBrokenEvent;
import net.weaponleveling.item.BrokenItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Final
    @Shadow
    public NonNullList<ItemStack> armor = NonNullList.withSize(4, ItemStack.EMPTY);


    @Shadow @Final public NonNullList<ItemStack> offhand;

    @Shadow @Final private List<NonNullList<ItemStack>> compartments;

    @Inject(
            method = "tick",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void swapBroken(CallbackInfo ci) {

        for(int i = 0; i < this.armor.size(); i++) {
            ItemStack stack = this.armor.get(i);
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag = customData != null ? customData.copyTag() : new CompoundTag();
            if(tag.getBoolean("weaponleveling:isBroken")) {
                tag.remove("weaponleveling:isBroken");
                stack.set(DataComponents.CUSTOM_DATA,CustomData.of(tag));
                ItemStack brokenItem = BrokenItem.of(stack);
                brokenItem.setCount(1);

                ItemReplaceBrokenEvent.REPLACE.invoker().replace(stack,brokenItem);

                this.armor.set(i,brokenItem);
            }
        }

        for(int i = 0; i < this.offhand.size(); i++) {
            ItemStack stack = this.offhand.get(i);
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag = customData != null ? customData.copyTag() : new CompoundTag();
            if(tag.getBoolean("weaponleveling:isBroken")) {
                tag.remove("weaponleveling:isBroken");
                stack.set(DataComponents.CUSTOM_DATA,CustomData.of(tag));
                ItemStack brokenItem = BrokenItem.of(stack);
                brokenItem.setCount(1);

                ItemReplaceBrokenEvent.REPLACE.invoker().replace(stack,brokenItem);


                this.offhand.set(i,brokenItem);
            }
        }


        for(int i = 0; i < this.compartments.size(); i++) {
            for(int j = 0; j < this.compartments.get(i).size();j++) {
                ItemStack stack = this.compartments.get(i).get(j);
                CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                CompoundTag tag = customData != null ? customData.copyTag() : new CompoundTag();
                if(tag.getBoolean("weaponleveling:isBroken")) {
                    tag.remove("weaponleveling:isBroken");
                    stack.set(DataComponents.CUSTOM_DATA,CustomData.of(tag));
                    ItemStack brokenItem = BrokenItem.of(stack);
                    brokenItem.setCount(1);

                    ItemReplaceBrokenEvent.REPLACE.invoker().replace(stack,brokenItem);


                    this.compartments.get(i).set(j,brokenItem);
                }
            }
        }


    }
}
