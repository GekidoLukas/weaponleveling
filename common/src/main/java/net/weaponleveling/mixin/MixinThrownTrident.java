package net.weaponleveling.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.weaponleveling.util.LevelingLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class MixinThrownTrident  extends AbstractArrow {


    @Shadow
    public abstract ItemStack getWeaponItem();

    protected MixinThrownTrident(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V",
            at = @At(value = "TAIL"))
    private void injectedLevel(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity entity = entityHitResult.getEntity();
        Entity entity2 = this.getOwner();
        DamageSource source = this.damageSources().trident(this, entity2 == null ? this : entity2);

        if(entity instanceof LivingEntity living) {
            LevelingLogic.updateForHit(living, source, false, getWeaponItem());
            if(!living.isAlive()) {
                LevelingLogic.updateForKill(living, source, getWeaponItem());
            }
        }
    }

    @ModifyArg(
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    public float hurtModify(float f) {

        return (float) getBaseDamage();
    }

}
