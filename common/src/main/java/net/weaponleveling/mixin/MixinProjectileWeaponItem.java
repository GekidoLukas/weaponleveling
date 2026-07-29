package net.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.api.LevelingAPI;
import net.weaponleveling.attribute.IRangedWeapon;
import net.weaponleveling.attribute.WLAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileWeaponItem.class)
public abstract class MixinProjectileWeaponItem implements IRangedWeapon {



    @WrapOperation(
            method = "shoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )

    // Making sure the weapon is saved. Might not be needed but causes no issues for now
    private boolean onArrowFired(ServerLevel instance, Entity entity, Operation<Boolean> original,ServerLevel serverLevel,LivingEntity owner, InteractionHand interactionHand, ItemStack bowStack) {
        if (entity instanceof AbstractArrow arrow) {
            LevelingAPI.referenceItemStackOnArrowEntity(arrow,bowStack);
        }
        return original.call(instance, entity);
    }
    @Inject(
            method = "<init>",
            at = @At(value = "HEAD"))
    private static void addAttribute(Item.Properties properties, CallbackInfo ci) {
        if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
            properties.attributes(createAttributes());
        }

    }

    @Unique
    private static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(WLAttributes.RANGED_DAMAGE, new AttributeModifier(LevelingAPI.BASE_RANGED_DAMAGE_ID, 0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                .build();
    }

}
