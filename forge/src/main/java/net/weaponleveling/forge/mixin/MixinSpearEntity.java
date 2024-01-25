package net.weaponleveling.forge.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.weaponleveling.util.ModUtils;
import net.weaponleveling.util.UpdateLevels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Pseudo
@Mixin(targets = "com.theishiopian.parrying.Entity.SpearEntity")
public abstract class MixinSpearEntity extends AbstractArrow {


    @Shadow(remap = false) public ItemStack spearItem;

    protected MixinSpearEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(
            method = "Lcom/theishiopian/parrying/Entity/SpearEntity;onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
            at = @At(value = "INVOKE",  target = "Lcom/theishiopian/parrying/Entity/SpearEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedXP(EntityHitResult p_213868_1_, CallbackInfo ci, Entity entity, LivingEntity living, float damage, Entity owner, DamageSource src) {
        if(ModUtils.isAcceptedProjectileWeapon(spearItem) && owner instanceof Player) {
            UpdateLevels.applyXPOnItemStack(spearItem, (Player) owner, entity, false);
        }
    }


    @ModifyArg(
            method = "Lcom/theishiopian/parrying/Entity/SpearEntity;onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
            at = @At(value = "INVOKE",  target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),index = 1)
    private float replaceEmpty(float pAmount) {
        double weaponlevelamount = spearItem.getOrCreateTag().getInt("level");
        weaponlevelamount *= ModUtils.getWeaponDamagePerLevel(spearItem);
        pAmount += weaponlevelamount;

        return pAmount;
    }



}
