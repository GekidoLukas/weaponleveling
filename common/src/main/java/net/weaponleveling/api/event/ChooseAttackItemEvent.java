package net.weaponleveling.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class ChooseAttackItemEvent {

    public static Event<Consumer<ChooseAttackItemEvent>> EVENT = EventFactory.createEventResult();
    public final LivingEntity killer;

    public ItemStack itemStack;

    public ChooseAttackItemEvent(LivingEntity killer,ItemStack itemStack) {
        this.killer = killer;
        this.itemStack = itemStack;
    }
}
