package net.weaponleveling.forge;

import com.google.gson.JsonElement;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.weaponleveling.WLConfigReader;
import net.weaponleveling.WeaponLevelingMod;
import net.weaponleveling.WeaponLevelingModClient;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.data.levelable_item.LevelableItemsLoader;
import net.weaponleveling.data.mob_xp.MobXPLoader;
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
        if(Platform.getEnvironment() == Env.SERVER) {
            Map<ResourceLocation, JsonElement> itemMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(itemMap);
            Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
            MobXPLoader.applyNew(mobMap);
        } else {
            localServerLoad();

        }
    }

    @SubscribeEvent
    public static void syncConfig(OnDatapackSyncEvent event) {
        if(event.getPlayer() != null) {
            if(Platform.getEnvironment() == Env.SERVER) {
                if(event.getPlayer().server.isDedicatedServer()) {
                    WLConfigReader.sync(event.getPlayer());
                    LevelableItemsLoader.sync(event.getPlayer());
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void localServerLoad() {
        if(Minecraft.getInstance().isLocalServer()) {
            Map<ResourceLocation, JsonElement> itemMap = LevelableItemsLoader.MAP;
            LevelableItemsLoader.applyNew(itemMap);
            Map<ResourceLocation, JsonElement> mobMap = MobXPLoader.MAP;
            MobXPLoader.applyNew(mobMap);
        }
    }
}
