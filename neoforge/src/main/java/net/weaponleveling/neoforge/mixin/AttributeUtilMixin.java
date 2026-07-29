package net.weaponleveling.neoforge.mixin;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import net.neoforged.neoforge.common.util.AttributeUtil;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.util.WLAttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(AttributeUtil.class)
public abstract class AttributeUtilMixin  {


    @WrapOperation(
            method = "applyModifierTooltips",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/AttributeUtil;getSortedModifiers(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lcom/google/common/collect/Multimap;"))
    private static Multimap<Holder<Attribute>, AttributeModifier> changeTooltip(ItemStack stack, EquipmentSlotGroup slot, Operation<Multimap<Holder<Attribute>, AttributeModifier>> original, ItemStack stackParameter, Consumer<Component> tooltip, AttributeTooltipContext ctx) {
        Player player = ctx.player();
        Multimap<Holder<Attribute>, AttributeModifier>  map = original.call(stack,slot);
        Multimap<Holder<Attribute>, AttributeModifier> modifiers =  LinkedListMultimap.create();

        for (var entry : map.entries()) {
            Holder<Attribute> attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue();
            modifiers.put(attribute, WLAttributeUtil.modifyAttributeModifierValue(attribute,modifier,stack,player,false));
        }

        return modifiers;
    }


    @WrapOperation(
            method = "applyTextFor",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/resources/ResourceLocation;equals(Ljava/lang/Object;)Z"))
    private static boolean changeAttributeColor(ResourceLocation instance, Object resourcelocation, Operation<Boolean> original, @Local(name = "attr") Holder<Attribute> holder) {
        if( WLAttributes.isGreenAttribute(holder)) {
            return true;
        }
        return original.call(instance,resourcelocation);
    }
}
