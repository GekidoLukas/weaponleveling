package net.geradesolukas.weaponleveling.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
public interface LivingHurtCallback {
    Event<LivingHurtCallback> EVENT = EventFactory.createArrayBacked(LivingHurtCallback.class, (listeners) -> (entity, source, damage) -> {
        for (LivingHurtCallback listener : listeners) {
            ActionResult result = listener.hurt(entity, source, damage);

            if (result != ActionResult.PASS) return result;
        }

        return ActionResult.PASS;
    });

    ActionResult hurt(LivingEntity entity, DamageSource source, float damage);
}
