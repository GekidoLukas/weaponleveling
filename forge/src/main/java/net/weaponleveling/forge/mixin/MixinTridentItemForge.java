package net.weaponleveling.forge.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TridentItem;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public class MixinTridentItemForge  {


    @WrapOperation(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;"), remap = false)
    private ImmutableMultimap<Attribute, AttributeModifier> inject(ImmutableMultimap.Builder instance, Operation<ImmutableMultimap<Attribute, AttributeModifier>> original) {
            instance.put(WLAttributes.RANGED_DAMAGE, new AttributeModifier(ModUtils.BASE_RANGED_DAMAGE_UUID, "Tool modifier", 6.0, AttributeModifier.Operation.ADDITION));
            return original.call(instance);
    }
}
