package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Consumer;

@Mixin(Inventory.class)
public abstract class MixinInventory {


    @WrapOperation(
            method = "hurtArmor",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    private <T extends LivingEntity> void changeDamageAmount(ItemStack instance, int amount, LivingEntity livingEntity, Consumer<T> pOnBroken, Operation<Void> original) {
        if (!WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, amount, livingEntity, pOnBroken);
        }
        if(!ItemUtils.isBroken(instance)) {
            original.call(instance, amount, livingEntity, pOnBroken);
        }
    }
}
