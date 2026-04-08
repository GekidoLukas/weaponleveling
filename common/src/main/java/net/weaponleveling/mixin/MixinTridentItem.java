package net.weaponleveling.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.Vanishable;
import net.weaponleveling.attribute.IRangedWeapon;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TridentItem.class)
public abstract class MixinTridentItem extends Item implements Vanishable, IRangedWeapon {





    public MixinTridentItem(Properties properties) {
        super(properties);
    }

//    @Inject(
//            method = "<init>",
//            at = @At(value = "INVOKE",
//                    target = "Lcom/google/common/collect/ImmutableMultimap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;", ordinal = 1
//            ), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void addAttribute(Properties properties, CallbackInfo ci, ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
//        builder.put(WLAttributes.RANGED_DAMAGE, new AttributeModifier(ModUtils.BASE_RANGED_DAMAGE_UUID, "Tool modifier", 6.0, AttributeModifier.Operation.ADDITION));
//
//    }

}
