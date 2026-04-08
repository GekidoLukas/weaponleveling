package net.weaponleveling.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.AbstractArrowAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class MixinAbstractArrow implements AbstractArrowAccessor {
    @Unique
    private ItemStack sourceWeapon = ItemStack.EMPTY;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectWrite(CompoundTag nbt, CallbackInfo ci) {
        if (!sourceWeapon.isEmpty()) {
            nbt.put("SourceWeapon", sourceWeapon.save(new CompoundTag()));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNbt(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("SourceWeapon")) {
            sourceWeapon = ItemStack.of(tag.getCompound("SourceWeapon"));
        }
    }

    @Override
    public ItemStack getSourceWeapon() {
        return sourceWeapon;
    }

    @Override
    public void setSourceWeapon(ItemStack stack) {
        this.sourceWeapon = stack.copy();
    }
}
