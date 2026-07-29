package net.weaponleveling.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.api.LevelingAPI;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.data.levelable_item.LevelableItem;
import net.weaponleveling.data.ranged_damage.RangedDamageLoader;
import net.weaponleveling.util.LevelingLogic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {


    @Shadow
    @Final
    private AttributeMap attributes;

    @Shadow
    public boolean swinging;

    @Inject(
            method = "collectEquipmentChanges",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void removeAttribute(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir, Map map, EquipmentSlot[] var2, int var3, int var4, EquipmentSlot equipmentSlot, ItemStack itemStack, ItemStack itemStack2, AttributeMap attributeMap) {
        LevelableItem levelableItem = LevelingAPI.getLevelableItem(itemStack);



        if(levelableItem != null) {
            for(var lvlAtt : levelableItem.attributes()) {
                AttributeInstance attributeInstance = attributeMap.getInstance((Holder<Attribute>)lvlAtt.attribute());
                if (attributeInstance != null) {
                    attributeInstance.removeModifier(WeaponLevelingMod.id("modify_" + equipmentSlot.getSerializedName()+ "_" + lvlAtt.attribute().getRegisteredName().replace(":","")));
                }
            }
        }
    }


    @Inject(
            method = "collectEquipmentChanges",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void addAttribute(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir, Map map, Iterator var2, Map.Entry entry, EquipmentSlot equipmentSlot2, ItemStack itemStack3) {
        LevelableItem levelableItem = LevelingAPI.getLevelableItem(itemStack3);
        int level = LevelingAPI.getLevel(itemStack3);


        if(levelableItem != null) {
            for(var lvlAtt : levelableItem.attributes()) {
                AttributeInstance attributeInstance = this.attributes.getInstance((Holder<Attribute>)lvlAtt.attribute());

                AtomicReference<AttributeModifier> modifier = new AtomicReference<>();
                itemStack3.forEachModifier(equipmentSlot2,(holder, attributeModifier) ->{
                    if(lvlAtt.attribute().equals(holder) && modifier.get() == null) {
                        modifier.set(attributeModifier);
                    }
                });

                if (attributeInstance != null && modifier.get() != null) {
                    double rangedAddedDamage = 0;

                    if(attributeInstance.getAttribute().equals(WLAttributes.RANGED_DAMAGE) && RangedDamageLoader.isValid(itemStack3.getItem())){
                        rangedAddedDamage = RangedDamageLoader.get(itemStack3.getItem()).getAmount();
                    }
                    ResourceLocation createdID = WeaponLevelingMod.id("modify_" + equipmentSlot2.getSerializedName()+ "_" + lvlAtt.attribute().getRegisteredName().replace(":",""));
                    attributeInstance.removeModifier(createdID);
                    attributeInstance.addTransientModifier(new AttributeModifier(createdID,rangedAddedDamage + LevelingAPI.calculateModifierAmount(modifier.get().amount(),level,lvlAtt), AttributeModifier.Operation.ADD_VALUE));
                }
            }
        }
    }


    @Inject(
            method = "createLivingAttributes",
            at = @At(value = "RETURN"))
    private static void addAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE ) {
            cir.getReturnValue().add(WLAttributes.RANGED_DAMAGE);
        }
    }
//    @WrapOperation(
//            method = "<init>",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/DefaultAttributes;getSupplier(Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;"))
//    private static AttributeSupplier addAttributes(EntityType<? extends LivingEntity> supplier, Operation<AttributeSupplier> original) {
//        if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
////            ResourceKey<Attribute> attributeKey = ResourceKey.create(
////                    Registries.ATTRIBUTE,
////                    WeaponLevelingMod.id("ranged_damage")
////            );
////            BuiltInRegistries.ATTRIBUTE.getHolder(attributeKey).ifPresent(holder -> {
////                supplier
////            });
//            if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
//                cir.getReturnValue().add(WLAttributes.RANGED_DAMAGE);
//            }
//        }
//        return original.call(supplier);
//    }

    @Inject(
            method = "die",
            at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedDie(DamageSource source, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        LevelingLogic.updateForKill(victim, source, null);
    }



    @Inject(
            method = "actuallyHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void injectedHurt(DamageSource source, float damageamount, CallbackInfo ci) {
        LivingEntity victim = ((LivingEntity) ((Object) this));
        LevelingLogic.updateForHit(victim, source, false, null);
    }





}
