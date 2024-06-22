package net.weaponleveling.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.weaponleveling.util.DataGetter;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Final
    @Shadow
    public NonNullList<ItemStack> armor = NonNullList.withSize(4, ItemStack.EMPTY);



    @Inject(
            method = "hurtArmor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private <T extends LivingEntity> void preventBreak(DamageSource damageSource, float f, int[] is, CallbackInfo ci, int[] var4, int var5, int var6, int i) {
        ItemStack itemStack = this.armor.get(i);
        if(ModUtils.isBroken(itemStack)) {
            ci.cancel();
        }
    }
}
