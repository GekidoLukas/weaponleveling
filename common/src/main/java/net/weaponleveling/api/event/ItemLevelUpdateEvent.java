package net.weaponleveling.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ItemLevelUpdateEvent {


    Event<PreGainXP> PRE = EventFactory.createEventResult();
    Event<LevelUp> LEVEL_UP = EventFactory.createEventResult();
    Event<SendNotification> SEND_NOTIFICATION = EventFactory.createEventResult();


    interface PreGainXP {
        EventResult pre(LivingEntity attacker, ItemStack stack, int amount);

    }
    interface LevelUp {
        EventResult levelUp(LivingEntity attacker, ItemStack stack, int currentLevel, long currentProgress, long maxProgress);

    }

    interface SendNotification {
        EventResult send(LivingEntity attacker, ItemStack stack, int currentLevel, long currentProgress, long maxProgress);

    }

}
