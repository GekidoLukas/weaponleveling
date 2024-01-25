package net.weaponleveling.forge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

@Pseudo
@Mixin(targets = "com.mrcrayfish.guns.item.GunItem")
public abstract class MixinGunItem extends Item{

    public MixinGunItem(Properties pProperties) {
        super(pProperties);
    }


    @WrapOperation(
            method = "Lcom/mrcrayfish/guns/item/GunItem;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private boolean wrapTooltip(List<Component> instance, Object object, Operation<Boolean> original, ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        if(ModUtils.isAcceptedProjectileWeapon(stack)) {
            MutableComponent component = (MutableComponent) object;
            Style DAMAGE = Style.EMPTY.withColor(12517240);
            int level = stack.getOrCreateTag().getInt("level");
            DecimalFormat doubleDecimalFormat = new DecimalFormat("#.##");
            double extradamage = level * ModUtils.getWeaponDamagePerLevel(stack);
            Component mycomponent = new TextComponent(" +" + doubleDecimalFormat.format(extradamage)).withStyle(DAMAGE);
            return original.call(instance, component.append(mycomponent));
        }
        return original.call(instance,object);
    }


}
