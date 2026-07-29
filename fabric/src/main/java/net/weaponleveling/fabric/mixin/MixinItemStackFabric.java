package net.weaponleveling.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.weaponleveling.util.DataGetter;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.WLAttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;


@Mixin(ItemStack.class)
public abstract class MixinItemStackFabric {


    @Shadow
    public abstract int getMaxDamage();

    /**
     * Sets the "isBroken" Tag, so we can replace it immediately after
     */
    @Inject(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"), cancellable = true)
    private <T extends LivingEntity> void preventBreak(int i, ServerLevel serverLevel, ServerPlayer serverPlayer, Consumer<Item> consumer, CallbackInfo ci, @Local(ordinal = 1) int j) {
        ItemStack stack = ((ItemStack) ((Object) this));
        if(serverPlayer != null) {
            if(j >= this.getMaxDamage()) {
                if(DataGetter.getBrokenItemsWontVanish() && ModUtils.shouldBeUnbreakable(stack)) {
                    CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                    CompoundTag tag = customData != null ? customData.copyTag() : new CompoundTag();
                    tag.putBoolean("weaponleveling:isBroken", true);
                    stack.set(DataComponents.CUSTOM_DATA,CustomData.of(tag));
                    stack.setDamageValue(0);

                    ci.cancel();

                }
            }
        }
    }

    @WrapOperation(
            method = "method_57370", //method_57370
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addModifierTooltip(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V"))
    private void changeTooltip(ItemStack instance, Consumer<Component> consumer, Player player, Holder<Attribute> attribute, AttributeModifier modifier, Operation<Void> original) {
        ItemStack stack = ((ItemStack) ((Object) this));
        original.call(instance,consumer,player,attribute,WLAttributeUtil.modifyAttributeModifierValue(attribute,modifier,stack,player,true));
    }
}
