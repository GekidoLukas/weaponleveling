package net.weaponleveling.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.weaponleveling.item.BrokenItem;
import net.weaponleveling.util.DataGetter;
import net.weaponleveling.util.ModUtils;
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
            if(stack.getTag() != null && stack.getTag().getBoolean("isBroken")) {
                stack.getTag().remove("isBroken");
                ItemStack brokenItem = BrokenItem.of(stack);
                brokenItem.setCount(1);
                this.armor.set(i,brokenItem);
            }
        }

        for(int i = 0; i < this.offhand.size(); i++) {
            ItemStack stack = this.offhand.get(i);
            if(stack.getTag() != null && stack.getTag().getBoolean("isBroken")) {
                stack.getTag().remove("isBroken");
                ItemStack brokenItem = BrokenItem.of(stack);
                brokenItem.setCount(1);
                this.offhand.set(i,brokenItem);
            }
        }


        for(int i = 0; i < this.compartments.size(); i++) {
            for(int j = 0; j < this.compartments.get(i).size();j++) {
                ItemStack stack = this.compartments.get(i).get(j);
                if(stack.getTag() != null && stack.getTag().getBoolean("isBroken")) {
                    stack.getTag().remove("isBroken");
                    ItemStack brokenItem = BrokenItem.of(stack);
                    brokenItem.setCount(1);
                    this.compartments.get(i).set(j,brokenItem);
                }
            }
        }


    }
}
