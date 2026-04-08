package net.weaponleveling.data.levelable_item.function;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class LevelingFunction {

    protected LevelingFunction() {

    }

    public abstract void setData(JsonObject object);
    public abstract void setData(CompoundTag tag);

    public abstract void read(FriendlyByteBuf buf);
    public abstract void write(FriendlyByteBuf buf);

    public abstract long calculateProgress(int level, int startingAmount);
}
