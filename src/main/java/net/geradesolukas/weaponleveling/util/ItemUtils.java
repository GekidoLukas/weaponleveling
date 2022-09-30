package net.geradesolukas.weaponleveling.util;

import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

public class ItemUtils {
    public static boolean isBroken(ItemStack stack) {
        return stack.getDamage() >= (stack.getMaxDamage() + 1);
    }

    public static void removeAttributeModifer(Multimap<EntityAttribute, EntityAttributeModifier> multimap, EntityAttribute attribute) {
        if(multimap.get(attribute).stream().findFirst().isPresent()) {
            EntityAttributeModifier modifier = multimap.get(attribute).stream().findFirst().get();
                multimap.remove(attribute,modifier);
        }

    }
}
