package net.weaponleveling.data.levelable_item.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class LevelingType {


    protected LevelingType() {

    }



    public abstract void setData(JsonObject object);
    public abstract void setData(CompoundTag tag);

    public abstract void read(FriendlyByteBuf buf);
    public abstract void write(FriendlyByteBuf buf);


}
