package net.weaponleveling.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.weaponleveling.util.AbstractArrowAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class MixinAbstractArrow
        extends Projectile implements AbstractArrowAccessor  {
    @Unique
    private ItemStack sourceWeapon = ItemStack.EMPTY;

    public MixinAbstractArrow(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectWrite(CompoundTag nbt, CallbackInfo ci) {
        if (!sourceWeapon.isEmpty()) {
            nbt.put("SourceWeapon", sourceWeapon.save(this.registryAccess(),new CompoundTag()));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readNbt(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("SourceWeapon")) {
            sourceWeapon = ItemStack.parseOptional(this.registryAccess(),tag.getCompound("SourceWeapon"));
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
