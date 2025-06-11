package net.weaponleveling.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface HitXPGainEvent {


    Event<PreGainXP> PRE = EventFactory.createEventResult();
    Event<PreGainXPItem> ITEM_PRE = EventFactory.createEventResult();
    Event<PostGainXPItem> ITEM_POST = EventFactory.createEventResult();


    interface PreGainXP {
        EventResult pre(Player player, LivingEntity victim, DamageSource source, @Nullable ItemStack specificStack);

    }

    interface PreGainXPItem {
        EventResult preItem(ItemStack stack, Player player, Entity victim, Boolean critical);

    }
    interface PostGainXPItem {
        EventResult postItem(ItemStack stack, Player player, Entity victim, Boolean critical, int xp_amount);

    }
}
