package net.weaponleveling.forge;

import com.google.gson.JsonElement;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.weaponleveling.WLConfigReader;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.WeaponLevelingModClient;
import net.weaponleveling.data.LevelableItemsLoader;
import net.weaponleveling.forge.compat.tetra.TetraCompat;

import java.util.Map;

@Mod.EventBusSubscriber
@Mod(WeaponLevelingMod.MODID)
public class WeaponLevelingModForge {
    public WeaponLevelingModForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(WeaponLevelingMod.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        WeaponLevelingMod.init();

        if(Platform.getEnv() == Dist.CLIENT) {
            WeaponLevelingModClient.init();
        }
    }
    private void setup(final FMLCommonSetupEvent event) {
        TetraCompat.init();
    }


    @SubscribeEvent
    public static void addReloadListeners(TagsUpdatedEvent event) {
        Map<ResourceLocation, JsonElement> jsonMap = LevelableItemsLoader.MAP;
        LevelableItemsLoader.applyNew(jsonMap);
    }

    @SubscribeEvent
    public static void syncConfig(OnDatapackSyncEvent event) {
        if(event.getPlayer() != null) {
            WLConfigReader.sync(event.getPlayer());
        }
    }
}
