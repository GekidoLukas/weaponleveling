package net.weaponleveling.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.world.item.ItemStack;

public interface ItemReplaceBrokenEvent {

    Event<PreReplace> PRE = EventFactory.createEventResult();
    Event<ReplaceWithBroken> REPLACE = EventFactory.createEventResult();

    interface ReplaceWithBroken {

        void replace(ItemStack originalItem, ItemStack brokenItem);


    }

    interface PreReplace {

        EventResult pre(ItemStack originalItem);


    }
}
