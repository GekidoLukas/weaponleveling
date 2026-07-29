package net.weaponleveling.util;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.weaponleveling.api.LevelingAPI;
import net.weaponleveling.attribute.WLAttributes;
import net.weaponleveling.data.levelable_item.LevelableAttribute;
import net.weaponleveling.data.levelable_item.LevelableItem;
import net.weaponleveling.data.ranged_damage.RangedDamageLoader;

public class WLAttributeUtil {



    public static AttributeModifier modifyAttributeModifierValue(Holder<Attribute> attribute, AttributeModifier original, ItemStack stack, Player player, boolean addPlayerBase) {
        LevelableItem levelableItem = LevelingAPI.getLevelableItem(stack);
        int level = LevelingAPI.getLevel(stack);

        double rangedAddedDamage = 0;

        if(attribute.equals(WLAttributes.RANGED_DAMAGE) && player != null) {
            if(addPlayerBase) rangedAddedDamage += player.getAttributeBaseValue(WLAttributes.RANGED_DAMAGE);

            if(RangedDamageLoader.isValid(stack.getItem())){
                rangedAddedDamage += RangedDamageLoader.get(stack.getItem()).getAmount();
            }
        }

        if(levelableItem != null && level > 0) {
            var opt = levelableItem.attributes().stream().filter(levelableAttribute -> levelableAttribute.attribute().equals(attribute)).findFirst();
            if(opt.isPresent()) {
                LevelableAttribute lvlAtt = opt.get();
                return new AttributeModifier(original.id(), original.amount() + rangedAddedDamage + LevelingAPI.calculateModifierAmount(original.amount(),level,lvlAtt), original.operation());

            }
        }
        else if(rangedAddedDamage > 0) {
            return new AttributeModifier(original.id(), original.amount() + rangedAddedDamage, original.operation());

        }
        return original;
    }
}
