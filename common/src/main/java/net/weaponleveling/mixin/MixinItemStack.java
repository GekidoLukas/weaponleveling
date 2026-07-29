package net.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.item.BrokenItem;
import net.weaponleveling.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {



    @Shadow
    public abstract boolean is(Item item);

    @Inject(
            method = "addModifierTooltip",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;operation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;",ordinal = 0,shift = At.Shift.BEFORE))
    private void changeAttributeColor(Consumer<Component> consumer, Player player, Holder<Attribute> holder, AttributeModifier attributeModifier, CallbackInfo ci, @Local LocalBooleanRef bl, @Local AttributeModifier modifier) {
        if( WLAttributes.isGreenAttribute(holder)) {
            bl.set(true);
        }
    }




    @ModifyReturnValue(
            method = "getTooltipLines",
            at = @At(value = "RETURN"))
    private List<Component> copyToolTip(List<Component> original, Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag) {
        ItemStack stack = ((ItemStack) ((Object) this));

        if(stack.is(ModItems.BROKEN_ITEM.get())) {
            List<Component> lines = BrokenItem.getContainedItem(stack).getTooltipLines(tooltipContext,player,tooltipFlag);

            Style TEXT = Style.EMPTY.withColor(WeaponLevelingConfig.textColor);
            Style BROKEN = Style.EMPTY.withColor(WeaponLevelingConfig.brokenColor);

            lines.set(0,Component.empty().append(stack.getHoverName()).withStyle(BrokenItem.getContainedItem(stack).getRarity().color()));
            lines.add(1,Component.empty());
            lines.add(1,Component.translatable("weaponleveling.tooltip.broken.description").setStyle(TEXT));
            lines.add(1, Component.translatable("weaponleveling.tooltip.broken").setStyle(BROKEN));

            return lines;
        }

        return original;
    }



    @ModifyReturnValue(
            method = "getHoverName",
            at = @At(value = "RETURN"))
    private Component replaceName(Component original) {
        ItemStack stack = ((ItemStack) ((Object) this));

        if(stack.is(ModItems.BROKEN_ITEM.get())) {
            return Component.empty().append(Component.translatable("weaponleveling.tooltip.broken.prefix",BrokenItem.getContainedItem(stack).getHoverName()).setStyle(BrokenItem.getContainedItem(stack).getHoverName().getStyle()));
        }

        return original;
    }

}
