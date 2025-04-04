package net.weaponleveling.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.item.BrokenItem;
import net.weaponleveling.item.ModItems;
import net.weaponleveling.util.ModUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Shadow public abstract boolean hurt(int i, RandomSource randomSource, @Nullable ServerPlayer serverPlayer);

    @Shadow @Final private static Logger LOGGER;

    @Shadow public abstract void inventoryTick(Level level, Entity entity, int i, boolean bl);

    @Inject(
            method = "Lnet/minecraft/world/item/ItemStack;getAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;",
            at = @At(value = "RETURN"), cancellable = true)
    private void injectModifier(EquipmentSlot equipmentSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        HashMultimap<Attribute, AttributeModifier> hashmap = HashMultimap.create(cir.getReturnValue());
        ItemStack stack = ((ItemStack) ((Object) this));
        Attribute attackDamage = Attributes.ATTACK_DAMAGE;
        Attribute attackSpeed = Attributes.ATTACK_SPEED;
        Attribute armor = Attributes.ARMOR;
        Attribute armor_toughness = Attributes.ARMOR_TOUGHNESS;
        if (ModUtils.isLevelableItem(stack) && ModUtils.isAcceptedMeleeWeaponStack(stack) && stack.getTag() != null) {
            ModUtils.modifyAttributeModifier(hashmap,attackDamage, ModUtils.getWeaponDamagePerLevel(stack) * stack.getTag().getInt("level"));
            ModUtils.modifyAttributeModifier(hashmap,attackSpeed, 0.0d );
        }
        if (ModUtils.isLevelableItem(stack) && ModUtils.isAcceptedArmor(stack) && stack.getTag() != null) {
            ModUtils.modifyAttributeModifier(hashmap,armor, ModUtils.getArmorPerLevel(stack) * stack.getTag().getInt("level"));
            ModUtils.modifyAttributeModifier(hashmap,armor_toughness, ModUtils.getToughnessPerLevel(stack) * stack.getTag().getInt("level") );
        }
        cir.setReturnValue(hashmap);
    }





    @ModifyReturnValue(
            method = "getTooltipLines",
            at = @At(value = "RETURN"))
    private List<Component> copyToolTip(List<Component> original, Player player, TooltipFlag tooltipFlag) {
        ItemStack stack = ((ItemStack) ((Object) this));

        if(stack.is(ModItems.BROKEN_ITEM.get())) {
            List<Component> lines = BrokenItem.getContainedItem(stack).getTooltipLines(player,tooltipFlag);

            Style TEXT = Style.EMPTY.withColor(WeaponLevelingConfig.textColor);
            Style BROKEN = Style.EMPTY.withColor(WeaponLevelingConfig.brokenColor);

            lines.set(0,Component.empty().append(stack.getHoverName()).withStyle(BrokenItem.getContainedItem(stack).getRarity().color));
            lines.add(1,Component.empty());
            lines.add(1,Component.translatable("weaponleveling.tooltip.broken.description").setStyle(TEXT));
            lines.add(1, Component.translatable("weaponleveling.tooltip.broken").setStyle(BROKEN));

            return lines;
        }

        return original;
    }



    @ModifyReturnValue(
            method = "getHoverName",
            at = @At(value = "RETURN"))
    private Component replaceName(Component original) {
        ItemStack stack = ((ItemStack) ((Object) this));

        if(stack.is(ModItems.BROKEN_ITEM.get())) {
            return Component.empty().append(Component.translatable("weaponleveling.tooltip.broken.prefix")).append(" ").append(BrokenItem.getContainedItem(stack).getHoverName());
        }

        return original;
    }



}
