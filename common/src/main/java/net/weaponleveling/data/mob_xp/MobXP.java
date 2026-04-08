package net.weaponleveling.data.mob_xp;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.weaponleveling.WeaponLevelingConfig;

public class MobXP {

    private final EntityType entityType;
    private final int amount;

    public MobXP(EntityType entityType, int amount) {
        this.entityType = entityType;
        this.amount = amount;
    }

    public static MobXP fromJson(JsonObject object, ResourceLocation resourceLocation) {
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation);

        int amount = WeaponLevelingConfig.kill_xp;
        if (object.has("amount")) {
            amount = Math.max(0,Math.min(object.get("amount").getAsInt(),10000000));
        }

        return new MobXP(entityType,amount);
    }


    public EntityType getEntityType() {
        return entityType;
    }

    public int getAmount() {
        return amount;
    }
}
