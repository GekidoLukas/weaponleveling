package net.weaponleveling.data.levelable_item.type;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public class WornType extends LevelingType{

    public static final StreamCodec<ByteBuf, EquipmentSlot> EQUIPMENT_SLOT_STREAM_CODEC =
            ByteBufCodecs.fromCodecTrusted(EquipmentSlot.CODEC);

    public static final MapCodec<WornType> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EquipmentSlot.CODEC.listOf().optionalFieldOf("slots", List.of()).forGetter(WornType::getSlots)
            ).apply(instance, WornType::new)
    );

    public static final StreamCodec<ByteBuf, WornType> STREAM_CODEC = StreamCodec.composite(
            EQUIPMENT_SLOT_STREAM_CODEC.apply(ByteBufCodecs.list()), WornType::getSlots,
            WornType::new
    );



    private List<EquipmentSlot> slots;

    public WornType() {
        this(new ArrayList<>());
    }

    public WornType(List<EquipmentSlot> slots) {
        this.slots = slots;
    }


    @Override
    public MapCodec<? extends LevelingType> getCodec() {
        return CODEC;
    }

    @Override
    public StreamCodec<ByteBuf, ? extends LevelingType> getStreamCodec() {
        return STREAM_CODEC;
    }

    public List<EquipmentSlot> getSlots() {
        return slots;
    }
}
