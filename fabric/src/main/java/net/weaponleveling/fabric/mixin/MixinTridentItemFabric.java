package net.weaponleveling.fabric.mixin;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TridentItem;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TridentItem.class)
public class MixinTridentItemFabric {


    @Inject(
            method = "<init>",
            at = @At(value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMultimap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;", ordinal = 1
            ), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void addAttribute(Item.Properties properties, CallbackInfo ci, ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
        builder.put(WLAttributes.RANGED_DAMAGE, new AttributeModifier(ModUtils.BASE_RANGED_DAMAGE_UUID, "Tool modifier", 6.0, AttributeModifier.Operation.ADDITION));
    }
}
