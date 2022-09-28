package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @WrapOperation(
            method = "Lnet/minecraft/world/item/ItemStack;getTooltipLines(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(value = "INVOKE",  target = "Ljava/util/List;add(Ljava/lang/Object;)Z",ordinal = 15))
    private boolean injectedDamage(List instance, Object object, Operation<Boolean> original) {
        if (ItemUtils.isBroken(((ItemStack) ((Object)this)))) {
            Component component = new TranslatableComponent("weaponleveling.tooltip.broken").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
            return original.call(instance,component);
        } else return original.call(instance,object);
    }


    @WrapOperation(
            method = "hurtAndBreak",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void removeRemoval(ItemStack instance, int amount, Operation<Void> original) {
        if (!WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance,amount);
        }
    }

    @WrapOperation(
            method = "hurtAndBreak",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"))
    private void changeDamageAmount(ItemStack instance, int amount, Operation<Void> original) {
        if (WeaponLevelingConfig.Server.broken_items_wont_vanish.get()) {
            original.call(instance, instance.getMaxDamage() + 1);
        } else original.call(instance, amount);

    }
}
