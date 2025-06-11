package net.weaponleveling.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public interface KillXPGainEvent {

    Event<Consumer<PreKillXPGainEvent>> PRE_GAIN = EventFactory.createCompoundEventResult();

    class PreKillXPGainEvent {

        public final LivingEntity victim;
        public int xpAmount;

        public PreKillXPGainEvent(LivingEntity victim, int xpAmount) {
            this.victim = victim;
            this.xpAmount = xpAmount;
        }

//        CompoundEventResult<PostKillEvent> post(LivingEntity victim, int xpAmount);

    }
}
