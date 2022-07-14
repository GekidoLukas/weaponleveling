package net.geradesolukas.weaponleveling.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow public abstract CompoundTag getOrCreateTag();
    @ModifyVariable(
            method = "Lnet/minecraft/world/item/ItemStack;getTooltipLines(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F")),
            at = @At(value = "STORE", ordinal = 0)
    )
    protected double injectedDamage(double bonus) {
        double weaponlevelamount = this.getOrCreateTag().getInt("level");

            weaponlevelamount /= 10;
        bonus += weaponlevelamount;
        return bonus;
    }
}
