package net.weaponleveling.item.component;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.data.levelable_item.LevelableItem;

public class WLDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(WeaponLevelingMod.MODID, Registries.DATA_COMPONENT_TYPE);


    public static final RegistrySupplier<DataComponentType<ItemLevelData>> ITEM_LEVEL_DATA =
            DATA_COMPONENTS.register("item_level_data", () -> DataComponentType.<ItemLevelData>builder()
                    .persistent(ItemLevelData.CODEC)
                    .networkSynchronized(ItemLevelData.STREAM_CODEC)
                    .build()
            );

    public static final RegistrySupplier<DataComponentType<LevelableItem>> ITEM_LEVELABLE_DATA =
            DATA_COMPONENTS.register("item_levelable_data", () -> DataComponentType.<LevelableItem>builder()
                    .persistent(LevelableItem.CODEC)
                    .networkSynchronized(LevelableItem.STREAM_CODEC)
                    .build()
            );
    public static final RegistrySupplier<DataComponentType<Boolean>> DISABLE_LEVELING =
            DATA_COMPONENTS.register("disable_leveling", () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
            );

    public static final RegistrySupplier<DataComponentType<ContainedItemData>> CONTAINED_ITEM =
            DATA_COMPONENTS.register("contained_item", () -> DataComponentType.<ContainedItemData>builder()
                    .persistent(ContainedItemData.CODEC)
                    .networkSynchronized(ContainedItemData.STREAM_CODEC)
                    .build()
            );


    public static void register(){
        DATA_COMPONENTS.register();
    }
}
