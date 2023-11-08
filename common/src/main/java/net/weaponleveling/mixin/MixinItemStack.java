package net.weaponleveling.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.weaponleveling.WLConfigGetter;
import net.weaponleveling.util.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.function.Consumer;

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

    @Inject(
            method = "useOn",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void preventInteract(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        if(ItemUtils.isBroken(useOnContext.getItemInHand())) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(
            method = "hurtAndBreak",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private <T extends LivingEntity> void preventBreak(int i, T livingEntity, Consumer<T> consumer, CallbackInfo ci) {
        ItemStack stack = ((ItemStack) ((Object) this));
        if(WLConfigGetter.getBrokenItemsDontVanish() && ItemUtils.isLevelableItem(stack)) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("isBroken", true);
            stack.setTag(tag);
            ci.cancel();
        }
    }
}
