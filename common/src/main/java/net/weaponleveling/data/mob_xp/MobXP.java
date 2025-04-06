package net.weaponleveling.data.mob_xp;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class MobXP {

    private final EntityType entityType;
    private final int amount;

    public MobXP(EntityType entityType, int amount) {
        this.entityType = entityType;
        this.amount = amount;
    }

    public static MobXP fromJson(JsonObject object, ResourceLocation resourceLocation) {
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation);

        int amount = 1;
        if (object.has("amount")) {
            amount = object.get("amount").getAsInt();
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
