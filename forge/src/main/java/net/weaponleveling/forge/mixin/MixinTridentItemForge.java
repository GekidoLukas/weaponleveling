package net.weaponleveling.forge.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TridentItem;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.api.LevelingAPI;
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
        if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
            instance.put(WLAttributes.RANGED_DAMAGE, new AttributeModifier(LevelingAPI.BASE_RANGED_DAMAGE_UUID, "Tool modifier", 6.0, AttributeModifier.Operation.ADDITION));
        }
            return original.call(instance);
    }
}
