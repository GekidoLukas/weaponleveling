package net.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.api.LevelingAPI;
import net.weaponleveling.attribute.IRangedWeapon;
import net.weaponleveling.attribute.WLAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public abstract class MixinTridentItem extends Item implements IRangedWeapon {





    public MixinTridentItem(Properties properties) {
        super(properties);
    }


    @WrapOperation(
            method = "createAttributes",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;builder()Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;"))
    private static ItemAttributeModifiers.Builder addAttribute(Operation<ItemAttributeModifiers.Builder> original) {
        if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
            return original.call().add(WLAttributes.RANGED_DAMAGE, new AttributeModifier(LevelingAPI.BASE_RANGED_DAMAGE_ID, 0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND);
        }
        return original.call();
    }
}
