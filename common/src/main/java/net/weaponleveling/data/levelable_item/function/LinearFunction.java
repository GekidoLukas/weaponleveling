package net.weaponleveling.data.levelable_item.function;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class LinearFunction extends LevelingFunction{





    private int slope = 80;


    @Override
    public long calculateProgress(int level, int startingAmount) {
        return (long) slope * level  + startingAmount;
    }


    @Override
    public void setData(JsonObject object) {
        if(object.has("slope")) {
            slope = Math.abs(object.get("slope").getAsInt());
        }
    }

    @Override
    public void setData(CompoundTag tag) {
        if(tag.contains("slope")) {
            slope = Math.abs(tag.getInt("slope"));
        }
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        slope = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(slope);
    }
}
