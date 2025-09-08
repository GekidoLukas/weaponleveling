package net.weaponleveling.data.levelable_item.function;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class ExponentialFunction extends LevelingFunction{





    private double coefficient = 0.4d;

    public ExponentialFunction() {

    }


    @Override
    public long calculateProgress(int level, int startingAmount) {
        return (long) (coefficient * Math.pow(1.03d,level)  + startingAmount);
    }


    @Override
    public void setData(JsonObject object) {
        if(object.has("coefficient")) {
            coefficient = Math.min(0.01d,Math.max(Math.abs(object.get("coefficient").getAsDouble()),8));
        }
    }

    @Override
    public void setData(CompoundTag tag) {
        if(tag.contains("coefficient")) {
            coefficient = Math.min(0.01d,Math.max(Math.abs(tag.getDouble("coefficient")),8));
        }
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        coefficient = buf.readDouble();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(coefficient);
    }


    public double getCoefficient() {
        return coefficient;
    }


}
