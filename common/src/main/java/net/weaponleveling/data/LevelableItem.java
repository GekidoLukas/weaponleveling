package net.weaponleveling.data;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.weaponleveling.WeaponLevelingConfig;

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
    private final double armorArmorPerLevel;
    private final double armorToughnessPerLevel;
    private final double bowlikeModifier;
    private final int armorXPRNGModifier;

    //private final String leveltype;


    public LevelableItem(Item item, boolean disabled, boolean isMelee, boolean isProjectile, boolean isArmor, int maxLevel, int levelModifier, int levelStartAmount, int hitXPAmount, int hitXPChance, int critXPAmount, int critXPChance, double weaponDamagePerLevel, double armorArmorPerLevel,double armorToughnessPerLevel, double bowlikeModifier,  int armorXPRNGModifier) {
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
        this.armorArmorPerLevel = armorArmorPerLevel;
        this.armorToughnessPerLevel = armorToughnessPerLevel;
        this.bowlikeModifier = bowlikeModifier;
        this.armorXPRNGModifier = armorXPRNGModifier;
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
    public double getArmorPerLevel() {
        return armorArmorPerLevel;
    }
    public double getToughnessPerLevel() {
        return armorToughnessPerLevel;
    }

    public double getBowlikeModifier() {
        return bowlikeModifier;
    }


    public int getArmorXPRNGModifier() {
        return armorXPRNGModifier;
    }

    public JsonObject serialize() {

        final JsonObject object = new JsonObject();
        if (this.isMelee) object.addProperty("isMeleeWeapon", true);
        if (this.isProjectile) object.addProperty("isProjectileWeapon", true);
        if (this.isArmor) object.addProperty("isArmor", true);

        return object;
    }

    public static LevelableItem fromJson(JsonObject object, ResourceLocation resourceLocation) {
        Item item = BuiltInRegistries.ITEM.get(resourceLocation);

        boolean disabled = false;
        if (object.has("disabled")) {
            disabled = object.get("disabled").getAsBoolean();
        }

        //Type
        boolean isMeleeWeapon = false;
        if (object.has("isMeleeWeapon")) {
            isMeleeWeapon = object.get("isMeleeWeapon").getAsBoolean();
        }

        boolean isProjectileWeapon = false;
        if (object.has("isProjectileWeapon")) {
            isProjectileWeapon = object.get("isProjectileWeapon").getAsBoolean();
        }

        boolean isArmor = false;
        if (object.has("isArmor")) {
            isArmor = object.get("isArmor").getAsBoolean();
        }

        //Level
        int maxLevel = WeaponLevelingConfig.max_item_level;
        if (object.has("maxLevel")) {
            maxLevel = object.get("maxLevel").getAsInt();
        }

        int levelModifier = WeaponLevelingConfig.level_modifier;
        if (object.has("levelModifier")) {
            levelModifier = object.get("levelModifier").getAsInt();
        }

        int levelStartAmount = WeaponLevelingConfig.starting_xp_amount;
        if (object.has("levelStartAmount")) {
            levelStartAmount = object.get("levelStartAmount").getAsInt();
        }

        int hitXPAmount = WeaponLevelingConfig.hit_xp_amount;
        if (object.has("hitXPAmount")) {
            hitXPAmount = object.get("hitXPAmount").getAsInt();
        }

        int hitXPChance = WeaponLevelingConfig.hit_percentage;
        if (object.has("hitXPChance")) {
            hitXPChance = object.get("hitXPChance").getAsInt();
        }

        int critXPAmount = WeaponLevelingConfig.crit_xp_amount;
        if (object.has("critXPAmount")) {
            critXPAmount = object.get("critXPAmount").getAsInt();
        }

        int critXPChance = WeaponLevelingConfig.crit_percentage;
        if (object.has("critXPChance")) {
            critXPChance = object.get("critXPChance").getAsInt();
        }


        //Damage

        double weaponDamagePerLevel = WeaponLevelingConfig.damage_per_level;
        if (object.has("weaponDamagePerLevel")) {
            weaponDamagePerLevel = object.get("weaponDamagePerLevel").getAsDouble();
        }

        double bowlikeModifier = WeaponLevelingConfig.bow_like_damage_modifier;
        if (object.has("bowlikeModifier")) {
            bowlikeModifier = object.get("bowlikeModifier").getAsDouble();
        }

        double armorArmorPerLevel = WeaponLevelingConfig.armor_per_level;
        if (object.has("armorPerLevel")) {
            armorArmorPerLevel = object.get("armorArmorPerLevel").getAsDouble();
        }

        double armorToughnessPerLevel = WeaponLevelingConfig.toughness_per_level;
        if (object.has("toughnessPerLevel")) {
            armorToughnessPerLevel = object.get("armorToughnessPerLevel").getAsDouble();
        }

        int armorXPRNGModifier = WeaponLevelingConfig.armor_rng_modifier;
        if (object.has("armorXPRNGModifier")) {
            armorXPRNGModifier = object.get("armorXPRNGModifier").getAsInt();
        }

        return new LevelableItem(item, disabled, isMeleeWeapon, isProjectileWeapon, isArmor, maxLevel, levelModifier, levelStartAmount, hitXPAmount, hitXPChance, critXPAmount, critXPChance, weaponDamagePerLevel, armorArmorPerLevel, armorToughnessPerLevel, bowlikeModifier, armorXPRNGModifier);
    }
}
