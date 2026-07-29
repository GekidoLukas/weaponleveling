package net.weaponleveling.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record ContainedItemData(ItemStack stack) {


    public static final Codec<ContainedItemData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.OPTIONAL_CODEC.fieldOf("stack").forGetter(ContainedItemData::stack)
            ).apply(instance, ContainedItemData::new)
    );


    public static final StreamCodec<RegistryFriendlyByteBuf, ContainedItemData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, ContainedItemData::stack,
            ContainedItemData::new
    );
}
