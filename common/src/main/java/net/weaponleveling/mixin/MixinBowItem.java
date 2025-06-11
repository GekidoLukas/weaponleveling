package net.weaponleveling.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.weaponleveling.EarlyConfig;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.api.LevelingAPI;
import net.weaponleveling.attribute.IRangedWeapon;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public abstract class MixinBowItem
        extends ProjectileWeaponItem
        implements Vanishable, IRangedWeapon {

    @Unique
    private Multimap<Attribute, AttributeModifier> defaultModifiers;

    public MixinBowItem(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "<init>",
            at = @At(value = "TAIL"))
    private void addAttribute(Item.Properties properties, CallbackInfo ci) {
//        MidnightConfig.init(WeaponLevelingMod.MODID, WeaponLevelingConfig.class);
        if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(WLAttributes.RANGED_DAMAGE, new AttributeModifier(LevelingAPI.BASE_RANGED_DAMAGE_UUID, "Tool modifier", 4.0, AttributeModifier.Operation.ADDITION));
                defaultModifiers = builder.build();
        }



    }




    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if(EarlyConfig.USE_WL_RANGED_ATTRIBUTE) {
            if (equipmentSlot == EquipmentSlot.MAINHAND || equipmentSlot == EquipmentSlot.OFFHAND) {
                return this.defaultModifiers;
            }
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }



}
