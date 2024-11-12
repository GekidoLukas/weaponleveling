package net.weaponleveling.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.weaponleveling.WeaponLevelingMod;
import org.jetbrains.annotations.NotNull;

public class BrokenItem extends Item {

    public static final String TAG_ITEM = "Item";



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
        CompoundTag tag = container.getOrCreateTag();
        if(!tag.contains(TAG_ITEM)) {
            CompoundTag compoundTag = new CompoundTag();
            containedItem.save(compoundTag);
            tag.put(TAG_ITEM,compoundTag);
        }
    }

    public static ItemStack getContainedItem(ItemStack container) {
        CompoundTag tag = container.getOrCreateTag();

        if(tag.contains(TAG_ITEM)) {
            return ItemStack.of(tag.getCompound(TAG_ITEM));
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack of(ItemStack stack) {
        ItemStack brokenItem = ModItems.BROKEN_ITEM.get().getDefaultInstance();
        BrokenItem.setContainedItem(brokenItem,stack);

        return brokenItem;


    }




}
