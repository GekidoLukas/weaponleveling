package net.weaponleveling.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.util.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Inject(
            method = "Lnet/minecraft/world/item/ItemStack;getAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;",
            at = @At(value = "RETURN"), cancellable = true)
    private void inejctModifier(EquipmentSlot equipmentSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        //Multimap<Attribute, AttributeModifier> multimap = cir.getReturnValue();
        HashMultimap<Attribute, AttributeModifier> hashmap = HashMultimap.create(cir.getReturnValue());
        ItemStack stack = ((ItemStack) ((Object) this));
        Attribute attackDamage = Attributes.ATTACK_DAMAGE;
        Attribute attackSpeed = Attributes.ATTACK_SPEED;
        if (ItemUtils.isLevelableItem(stack) && ItemUtils.isAcceptedMeleeWeaponStack(stack) && stack.getTag() != null) {
            ItemUtils.modifyAttributeModifier(hashmap,attackDamage, ItemUtils.getWeaponDamagePerLevel(stack) * stack.getTag().getInt("level"));
            ItemUtils.modifyAttributeModifier(hashmap,attackSpeed, 0.0d );
        }
        cir.setReturnValue(hashmap);
    }
}
