package net.weaponleveling.data.levelable_item;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevelableAttribute {


    private final double valuePerLevel;
    private final boolean addIfNonExistent;
    @NotNull
    private final Set<EquipmentSlot>  slotsForNonExistent;
    private final Attribute attribute;
    private final boolean isPercent;




    public LevelableAttribute(double valuePerLevel, Attribute attribute, boolean addIfNonExistent, Set<EquipmentSlot> slotsForNonExistent, boolean isPercent) {
        this.valuePerLevel = valuePerLevel;
        this.attribute = attribute;
        this.addIfNonExistent = addIfNonExistent;
        this.slotsForNonExistent = slotsForNonExistent;
        this.isPercent = isPercent;
    }


    public static LevelableAttribute fromJSON(JsonObject object) {
        double valuePerLevel = object.has("valuePerLevel") ? object.get("valuePerLevel").getAsDouble() : WeaponLevelingConfig.value_per_level;
        boolean addIfNonExistent  = object.has("addIfNonExistent") ? object.get("addIfNonExistent").getAsBoolean() : false;
        boolean isPercent  = object.has("isPercent") ? object.get("isPercent").getAsBoolean() : false;
        Set<EquipmentSlot> slotsForNonExistent = new HashSet<>();

        if(object.has("slotForNonExistent")) {
            try {
                slotsForNonExistent.add(EquipmentSlot.byName(object.get("slotForNonExistent").getAsString().toLowerCase()));
            } catch (Exception e) {
                WeaponLevelingMod.LOGGER.error(object.get("slotForNonExistent").getAsString() + " is not a correct EquipmentSlot");
            }
        } else if(object.has("slotsForNonExistent")) {
            if(object.get("slotsForNonExistent").isJsonArray()) {
                for(var slot : object.get("slotsForNonExistent").getAsJsonArray()) {
                    try {
                        EquipmentSlot equipmentSlot = EquipmentSlot.byName(slot.getAsString().toLowerCase());
                        slotsForNonExistent.add(equipmentSlot);
                    }
                    catch (Exception e) {
                        WeaponLevelingMod.LOGGER.error(slot.getAsString() + " is not a correct EquipmentSlot");
                    }

                }
            }
        }



        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(new ResourceLocation(object.get("attribute").getAsString()));


        return attribute != null ? new LevelableAttribute(valuePerLevel,attribute,addIfNonExistent,slotsForNonExistent,isPercent) : null;
    }
    public static LevelableAttribute fromNBT(CompoundTag tag) {
        double valuePerLevel = tag.contains("valuePerLevel") ? tag.getDouble("valuePerLevel") : WeaponLevelingConfig.value_per_level;
        boolean addIfNonExistent = tag.contains("addIfNonExistent") ? tag.getBoolean("addIfNonExistent") : false;
        boolean isPercent = tag.contains("isPercent") ? tag.getBoolean("isPercent") : false;
        Set<EquipmentSlot> slotsForNonExistent = new HashSet<>();
        if(tag.contains("slotForNonExistent")) {
            try {
                slotsForNonExistent.add(EquipmentSlot.byName(tag.getString("slotForNonExistent").toLowerCase()));
            } catch (Exception e) {
                WeaponLevelingMod.LOGGER.error(tag.getString("slotForNonExistent") + " is not a correct EquipmentSlot");
            }
        } else if(tag.contains("slotsForNonExistent")) {
            if(!tag.getList("slotsForNonExistent", Tag.TAG_STRING).isEmpty()) {
                for(var slot : tag.getList("slotsForNonExistent", Tag.TAG_STRING)) {
                    if(slot instanceof StringTag stringTag) {

                        try {
                            EquipmentSlot equipmentSlot = EquipmentSlot.byName(stringTag.getAsString().toLowerCase());
                            slotsForNonExistent.add(equipmentSlot);
                        }
                        catch (Exception e) {
                            WeaponLevelingMod.LOGGER.error(stringTag + " is not a correct EquipmentSlot");
                        }
                    }

                }
            }
        }

        Attribute attribute = null;
        ResourceLocation id = new ResourceLocation(tag.getString("attribute"));
        attribute =  BuiltInRegistries.ATTRIBUTE.get(id);


        return attribute != null ? new LevelableAttribute(valuePerLevel,attribute,addIfNonExistent,slotsForNonExistent,isPercent) : null;
    }

    public static boolean hasValidInNBT(ListTag tag) {
        int correctOnes = 0;
        for (var item : tag) {
            if(item instanceof CompoundTag compoundTag) {
                LevelableAttribute levelableAttribute = fromNBT(compoundTag);
                if(levelableAttribute != null) correctOnes++;
            }
        }
        return correctOnes > 0;
    }

    public double getValuePerLevel() {
        return valuePerLevel;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public boolean isPercent() {
        return isPercent;
    }

    /**
     * Adds the attribute if it doesn't exist yet
     */
    public boolean addIfNonExistent() {
        return addIfNonExistent;
    }

    public @NotNull Set<EquipmentSlot> getSlotForNonExistent() {
        return slotsForNonExistent;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(this.valuePerLevel);
        buf.writeBoolean(this.addIfNonExistent);
        buf.writeBoolean(this.isPercent);
        buf.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(this.attribute));
        buf.writeInt(slotsForNonExistent.size());
        for(EquipmentSlot slot : slotsForNonExistent) {
            buf.writeEnum(slot);
        }
    }

    public static LevelableAttribute read(FriendlyByteBuf buf) {
        double valuePerLevel = buf.readDouble();
        boolean addIfNonExistent = buf.readBoolean();
        boolean isPercent = buf.readBoolean();
        Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(buf.readResourceLocation());
        Set<EquipmentSlot> slots = new HashSet<>();
        int count = buf.readInt();
        for(int i = 0; i< count; i++) {
            try {
                slots.add(buf.readEnum(EquipmentSlot.class));
            } catch (Exception e) {
                WeaponLevelingMod.LOGGER.error(e);
            }
        }
        return new LevelableAttribute(valuePerLevel, attribute,addIfNonExistent,slots,isPercent);
    }
}
