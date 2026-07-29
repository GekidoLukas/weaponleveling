package net.weaponleveling.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.weaponleveling.item.component.ContainedItemData;
import net.weaponleveling.item.component.WLDataComponents;

public class BrokenItem extends Item {




    public BrokenItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return 0;
    }


    public static void setContainedItem(ItemStack container , ItemStack containedItem) {
        if(!container.has(WLDataComponents.CONTAINED_ITEM.get())) {
            container.set(WLDataComponents.CONTAINED_ITEM.get(),new ContainedItemData(containedItem));
        }
    }

    public static ItemStack getContainedItem(ItemStack container) {

        if(container.has(WLDataComponents.CONTAINED_ITEM.get())) {
            return container.get(WLDataComponents.CONTAINED_ITEM.get()).stack();
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack of(ItemStack stack) {
        ItemStack brokenItem = ModItems.BROKEN_ITEM.get().getDefaultInstance();
        BrokenItem.setContainedItem(brokenItem,stack);

        return brokenItem;


    }


}
