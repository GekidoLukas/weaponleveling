package net.weaponleveling.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.weaponleveling.data.levelable_item.LevelableItem;

import java.util.Map;

public interface AfterLevelableLoadedEvent {

    Event<PostLoadEvent> POST = EventFactory.createLoop();

    /**
     * To use with reload listeners apply to a map first with the regular event and then use this to apply it to your final data. Example is the {@link net.weaponleveling.data.levelable_item.LevelableItemsLoader#apply(Map, ResourceManager, ProfilerFiller)} 
     */
    interface PostLoadEvent {
        void post(Map<ResourceLocation, LevelableItem> registryCopy);

    }
}
