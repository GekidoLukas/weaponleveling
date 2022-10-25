package net.geradesolukas.weaponleveling.data;

import com.google.gson.JsonObject;
import net.geradesolukas.weaponleveling.config.WeaponLevelingConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public class LevelableItem {

    private final Item item;
    private final boolean disabled;
    private final boolean isMelee;
    private final boolean isProjectile;
    private final boolean isArmor;



    private final int maxLevel;
    private final int levelModifier;
    private final int levelStartAmount;

    private final int hitXPAmount;
    private final int hitXPChance;
    private final int critXPAmount;
    private final int critXPChance;
    private final double weaponDamagePerLevel;
    private final double bowlikeModifier;
    private final double armorMaxDamageReduction;

    //private final String leveltype;


    public LevelableItem(Item item, boolean disabled, boolean isMelee, boolean isProjectile, boolean isArmor, int maxLevel, int levelModifier, int levelStartAmount, int hitXPAmount, int hitXPChance, int critXPAmount, int critXPChance, double weaponDamagePerLevel, double bowlikeModifier, double armorMaxDamageReduction) {
        this.item = item;
        this.disabled = disabled;
        this.isMelee = isMelee;
        this.isProjectile = isProjectile;
        this.isArmor = isArmor;
        this.maxLevel = maxLevel;
        this.levelModifier = levelModifier;
        this.levelStartAmount = levelStartAmount;
        this.hitXPAmount = hitXPAmount;
        this.hitXPChance = hitXPChance;
        this.critXPAmount = critXPAmount;
        this.critXPChance = critXPChance;
        this.weaponDamagePerLevel = weaponDamagePerLevel;
        this.bowlikeModifier = bowlikeModifier;
        this.armorMaxDamageReduction = armorMaxDamageReduction;

    }


    public Item getItem() {
        return item;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isMelee() {
        return isMelee;
    }

    public boolean isProjectile() {
        return isProjectile;
    }

    public boolean isArmor() {
        return isArmor;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getLevelModifier() {
        return levelModifier;
    }

    public int getLevelStartAmount() {
        return levelStartAmount;
    }

    public int getHitXPAmount() {
        return hitXPAmount;
    }

    public int getHitXPChance() {
        return hitXPChance;
    }

    public int getCritXPAmount() {
        return critXPAmount;
    }

    public int getCritXPChance() {
        return critXPChance;
    }

    public double getWeaponDamagePerLevel() {
        return weaponDamagePerLevel;
    }

    public double getBowlikeModifier() {
        return bowlikeModifier;
    }

    public double getArmorMaxDamageReduction() {
        return armorMaxDamageReduction;
    }

    public JsonObject serialize() {

        final JsonObject object = new JsonObject();
        if(this.isMelee) object.addProperty("isMeleeWeapon", true);
        if(this.isProjectile) object.addProperty("isProjectileWeapon", true);
        if(this.isArmor) object.addProperty("isArmor", true);

        return object;
    }

    public static LevelableItem fromJson(JsonObject object, ResourceLocation resourceLocation) {
        Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
        boolean disabled = false;
        if(object.has("disabled")) {
            disabled = object.get("disabled").getAsBoolean();
        }

        //Type
        boolean isMeleeWeapon = false;
        if(object.has("isMeleeWeapon")) {
            isMeleeWeapon = object.get("isMeleeWeapon").getAsBoolean();
        }

        boolean isProjectileWeapon = false;
        if(object.has("isProjectileWeapon")) {
            isProjectileWeapon = object.get("isProjectileWeapon").getAsBoolean();
        }

        boolean isArmor = false;
        if(object.has("isArmor")) {
            isArmor = object.get("isArmor").getAsBoolean();
        }

        //Level
        int maxLevel = WeaponLevelingConfig.Server.value_max_level.get();
        if(object.has("maxLevel")) {
            maxLevel = object.get("maxLevel").getAsInt();
        }

        int levelModifier = WeaponLevelingConfig.Server.value_level_modifier.get();
        if(object.has("levelModifier")) {
            levelModifier = object.get("levelModifier").getAsInt();
        }

        int levelStartAmount = WeaponLevelingConfig.Server.value_starting_level_amount.get();
        if(object.has("levelStartAmount")) {
            levelStartAmount = object.get("levelStartAmount").getAsInt();
        }

        int hitXPAmount = WeaponLevelingConfig.Server.value_hit_xp_amount.get();
        if(object.has("hitXPAmount")) {
            hitXPAmount = object.get("hitXPAmount").getAsInt();
        }

        int hitXPChance = WeaponLevelingConfig.Server.value_hit_percentage.get();
        if(object.has("hitXPChance")) {
            hitXPChance = object.get("hitXPChance").getAsInt();
        }

        int critXPAmount = WeaponLevelingConfig.Server.value_crit_xp_amount.get();
        if(object.has("critXPAmount")) {
            critXPAmount = object.get("critXPAmount").getAsInt();
        }

        int critXPChance = WeaponLevelingConfig.Server.value_crit_percentage.get();
        if(object.has("critXPChance")) {
            critXPChance = object.get("critXPChance").getAsInt();
        }


        //Damage

        double weaponDamagePerLevel = WeaponLevelingConfig.Server.value_damage_per_level.get();
        if(object.has("weaponDamagePerLevel")) {
            weaponDamagePerLevel = object.get("weaponDamagePerLevel").getAsDouble();
        }

        double bowlikeModifier = WeaponLevelingConfig.Server.value_bowlike_damage_modifier.get();
        if(object.has("bowlikeModifier")) {
            bowlikeModifier = object.get("bowlikeModifier").getAsDouble();
        }

        double armorMaxDamageReduction = WeaponLevelingConfig.Server.value_max_damage_reduction.get();
        if(object.has("armorMaxDamageReduction")) {
            armorMaxDamageReduction = object.get("armorMaxDamageReduction").getAsDouble();
        }



        //String leveltype = "none";
        //if(object.has("leveltype")) {
        //    leveltype = object.get("leveltype").getAsString();
        //}
        //if(object.has("leveltypes")) {
        //    JsonArray leveltypes = object.get("leveltypes").getAsJsonArray();
        //    leveltypes.forEach(levelentry -> {
        //        JsonObject entry = levelentry.getAsJsonObject();
        //        String type = entry.get("type").getAsString();
        //        if(type == null) return;
        //    });
        //}


        //WeaponLeveling.LOGGER.info("This is the Item: " + item.toString() + " and this is disabled: " + disabled);
        return new LevelableItem(item,disabled, isMeleeWeapon, isProjectileWeapon, isArmor, maxLevel, levelModifier, levelStartAmount, hitXPAmount,hitXPChance, critXPAmount, critXPChance,weaponDamagePerLevel,bowlikeModifier, armorMaxDamageReduction);
    }
}
