package net.weaponleveling.neoforge.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.weaponleveling.util.DataGetter;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStackNeoForge {


    @Shadow
    public abstract int getMaxDamage();

    /**
     * Sets the "isBroken" Tag, so we can replace it immediately after
     */
    @Inject(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private <T extends LivingEntity> void preventBreak(int i, ServerLevel serverLevel, LivingEntity livingEntity, Consumer<Item> consumer, CallbackInfo ci, int j) {
        ItemStack stack = ((ItemStack) ((Object) this));
        if(livingEntity instanceof ServerPlayer serverPlayer) {
            if(j >= this.getMaxDamage()) {
                if(DataGetter.getBrokenItemsWontVanish() && ModUtils.shouldBeUnbreakable(stack)) {
                    CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                    CompoundTag tag = customData != null ? customData.copyTag() : new CompoundTag();
                    tag.putBoolean("weaponleveling:isBroken", true);
                    stack.set(DataComponents.CUSTOM_DATA,CustomData.of(tag));
                    stack.setDamageValue(0);
                    serverLevel.playSound(null,
                            serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), stack.getBreakingSound(), serverPlayer.getSoundSource(), 0.8F, 0.8F + serverLevel.random.nextFloat() * 0.4F
                    );
                    ci.cancel();

                }
            }
        }
    }




}
