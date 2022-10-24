package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.annotation.Target;

@Pseudo
@Mixin(targets = "com.theishiopian.parrying.Entity.SpearEntity")
public abstract class MixinSpearEntity {


    @Shadow(remap = false) public ItemStack spearItem;

    @Inject(
            method = "Lcom/theishiopian/parrying/Entity/SpearEntity;onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
            at = @At(value = "INVOKE",  target = "Lcom/theishiopian/parrying/Entity/SpearEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedXP(EntityHitResult p_213868_1_, CallbackInfo ci, Entity entity, LivingEntity living, float damage, Entity owner, DamageSource src) {
        if(ItemUtils.isAcceptedProjectileWeapon(spearItem) && owner instanceof Player) {
            UpdateLevels.applyXPOnItemStack(spearItem, (Player) owner, entity, false);
        }
    }


    @ModifyArg(
            method = "Lcom/theishiopian/parrying/Entity/SpearEntity;onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),index = 1)
    private float replaceEmpty(float pAmount) {
        double weaponlevelamount = spearItem.getOrCreateTag().getInt("level");
        weaponlevelamount *= ItemUtils.getWeaponDamagePerLevel(spearItem);
        pAmount += weaponlevelamount;

        return pAmount;
    }



}
