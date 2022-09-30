package net.geradesolukas.weaponleveling.mixin;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @WrapOperation(
            method = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;",
            at = @At(value = "INVOKE",  target = "Ljava/util/List;add(Ljava/lang/Object;)Z",ordinal = 15))
    private boolean injectedDamage(List instance, Object object, Operation<Boolean> original) {
        if (ItemUtils.isBroken(((ItemStack) ((Object)this)))) {
            MutableText component = new TranslatableText("weaponleveling.tooltip.broken").setStyle(Style.EMPTY.withColor(Formatting.RED));
            return original.call(instance,component);
        } else return original.call(instance,object);
    }


    @WrapOperation(
            method = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void removeRemoval(ItemStack instance, int amount, Operation<Void> original) {
        if (!WeaponlevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance,amount);
        }
    }

    @WrapOperation(
            method = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"))
    private void changeDamageAmount(ItemStack instance, int amount, Operation<Void> original) {
        if (WeaponlevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, instance.getMaxDamage() + 1);
        } else original.call(instance, amount);

    }

    @ModifyReturnValue(
            method = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;",
            at = @At(value = "RETURN"))
    private Multimap<EntityAttribute, EntityAttributeModifier> changeDamageAmount(Multimap<EntityAttribute, EntityAttributeModifier> original,EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> multimap = original;
        if (((ItemStack) ((Object) this)).getItem() instanceof ArmorItem) {
            if (ItemUtils.isBroken(((ItemStack) ((Object) this)))) {
                EntityAttribute armor = EntityAttributes.GENERIC_ARMOR;
                EntityAttribute armorToughness = EntityAttributes.GENERIC_ARMOR_TOUGHNESS;
                EntityAttribute knockbackResistanceesistance = EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE;
                ItemUtils.removeAttributeModifer(multimap,armor);
                ItemUtils.removeAttributeModifer(multimap,armorToughness);
                ItemUtils.removeAttributeModifer(multimap,knockbackResistanceesistance);
                return multimap;
            }
        }
        return original;
    }


}
