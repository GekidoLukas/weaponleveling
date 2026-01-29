package net.weaponleveling.data.levelable_item.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.weaponleveling.api.registry.LevelingTypeRegistry;

public abstract class LevelingType {


    protected LevelingType() {

    }



    public abstract void setData(JsonObject object);
    public abstract void setData(CompoundTag tag);

    public abstract void read(FriendlyByteBuf buf);
    public abstract void write(FriendlyByteBuf buf);


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LevelingType other) {
            return LevelingTypeRegistry.getID(other).equals(LevelingTypeRegistry.getID(this));
        } else {
            return false;
        }
    }
}
