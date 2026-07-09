package net.weaponleveling.data.ranged_damage;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.data.mob_xp.MobXP;

public class RangedDamageEntry {


    private final double amount;

    public RangedDamageEntry(double amount ) {
        this.amount = amount;
    }

    public static RangedDamageEntry fromJson(JsonObject object) {

        double amount = 1;
        if (object.has("amount")) {
            amount = Math.max(-1000,Math.min(object.get("amount").getAsDouble(),10000));
        }

        return new RangedDamageEntry(amount);
    }

    public double getAmount() {
        return amount;
    }
}
