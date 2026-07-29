package net.weaponleveling.data.ranged_damage;

import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

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


    public static final StreamCodec<RegistryFriendlyByteBuf, RangedDamageEntry> STREAM_CODEC = StreamCodec.of(
            // Encoder
            (buf,entry) -> {
                buf.writeDouble(entry.amount);
            },
            //Decoder
            buf -> {
                double amount = buf.readDouble();
                return new RangedDamageEntry(amount);
            }
    );
}
