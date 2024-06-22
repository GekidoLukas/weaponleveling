package net.weaponleveling.forge.mixin;

import dev.architectury.platform.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.WLPlatformGetter;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.util.DataGetter;
import net.weaponleveling.util.ModUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class MixinItemStackForge {


    @Shadow
    public boolean hurt(int i, RandomSource randomSource, @Nullable ServerPlayer serverPlayer) {
        return false;
    }

    @Inject(
            method = "hurtAndBreak",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 1), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private <T extends LivingEntity> void preventBreak(int i, T livingEntity, Consumer<T> consumer, CallbackInfo ci) {
        ItemStack stack = ((ItemStack) ((Object) this));
        if(livingEntity instanceof ServerPlayer player) {
            if(this.hurt(i, livingEntity.getRandom(), player)) {
                if(DataGetter.getBrokenItemsWontVanish() && ModUtils.shouldBeUnbreakable(stack)) {
                    if(Platform.isForge()) {
                        //TODO Play sound for break
                    }
                    CompoundTag tag = stack.getTag();
                    tag.putBoolean("isBroken", true);
                    stack.setTag(tag);
                    ci.cancel();
                }
            }
        }
    }

}
