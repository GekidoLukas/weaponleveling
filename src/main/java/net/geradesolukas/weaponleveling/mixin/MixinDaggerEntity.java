package net.geradesolukas.weaponleveling.mixin;

import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.geradesolukas.weaponleveling.util.UpdateLevels;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Pseudo
@Mixin(targets = "com.theishiopian.parrying.Entity.DaggerEntity")
public abstract class MixinDaggerEntity extends AbstractArrow {


    @Shadow(remap = false) public ItemStack daggerItem;

    protected MixinDaggerEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(
            method = "Lcom/theishiopian/parrying/Entity/DaggerEntity;onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
            at = @At(value = "INVOKE",  target = "Lcom/theishiopian/parrying/Entity/DaggerEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedXP(EntityHitResult p_213868_1_, CallbackInfo ci, Entity entity, LivingEntity living, float damage, Entity owner, DamageSource src) {
        if(ItemUtils.isAcceptedProjectileWeapon(daggerItem) && owner instanceof Player) {
            UpdateLevels.applyXPOnItemStack(daggerItem, (Player) owner, entity, false);
        }
    }


    @ModifyArg(
            method = "Lcom/theishiopian/parrying/Entity/DaggerEntity;onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),index = 1)
    private float replaceEmpty(float pAmount) {
        double weaponlevelamount = daggerItem.getOrCreateTag().getInt("level");
        weaponlevelamount *= ItemUtils.getWeaponDamagePerLevel(daggerItem);
        pAmount += weaponlevelamount;

        return pAmount;
    }



}
