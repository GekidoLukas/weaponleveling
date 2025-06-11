package net.weaponleveling.data.levelable_item.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.weaponleveling.WeaponLevelingMod;

import java.util.ArrayList;
import java.util.List;

public class WornType extends LevelingType{


    private List<EquipmentSlot> slots = new ArrayList<>();


    @Override
    public void setData(JsonObject object) {
        if(object.has("slots") && object.get("slots").isJsonArray()) {
            for(var slot : object.get("slots").getAsJsonArray()) {
                try {
                    EquipmentSlot equipmentSlot = EquipmentSlot.byName(slot.getAsString().toLowerCase());
                    slots.add(equipmentSlot);
                }
                catch (Exception e) {
                    WeaponLevelingMod.LOGGER.error(slot.getAsString() + " is not a correct EquipmentSlot");
                }

            }
        }

    }

    @Override
    public void read(FriendlyByteBuf buf) {
        List<EquipmentSlot> slotList = new ArrayList<>();
        int count = buf.readInt();
        for(int i = 0; i< count; i++) {
            try {
                slotList.add(EquipmentSlot.byName(buf.readUtf()));
            } catch (Exception e) {
                WeaponLevelingMod.LOGGER.error(e);
            }
        }
        this.slots = slotList;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(slots.size());
        for(var slot : slots) {
            buf.writeUtf(slot.getName());
        }
    }
}
