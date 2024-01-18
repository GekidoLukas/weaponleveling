package net.weaponleveling.data;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.weaponleveling.WLPlatformGetter;

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

    private final int armorXPRNGModifier;

    //private final String leveltype;


    public LevelableItem(Item item, boolean disabled, boolean isMelee, boolean isProjectile, boolean isArmor, int maxLevel, int levelModifier, int levelStartAmount, int hitXPAmount, int hitXPChance, int critXPAmount, int critXPChance, double weaponDamagePerLevel, double bowlikeModifier, double armorMaxDamageReduction, int armorXPRNGModifier) {
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

    public double getBowlikeModifier() {
        return bowlikeModifier;
    }

    public double getArmorMaxDamageReduction() {
        return armorMaxDamageReduction;
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

        Item item = Registry.ITEM.get(resourceLocation);

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
        int maxLevel = WLPlatformGetter.getMaxLevel();
        if (object.has("maxLevel")) {
            maxLevel = object.get("maxLevel").getAsInt();
        }

        int levelModifier = WLPlatformGetter.getLevelModifier();
        if (object.has("levelModifier")) {
            levelModifier = object.get("levelModifier").getAsInt();
        }

        int levelStartAmount = WLPlatformGetter.getStartingLevelAmount();
        if (object.has("levelStartAmount")) {
            levelStartAmount = object.get("levelStartAmount").getAsInt();
        }

        int hitXPAmount = WLPlatformGetter.getHitXPAmount();
        if (object.has("hitXPAmount")) {
            hitXPAmount = object.get("hitXPAmount").getAsInt();
        }

        int hitXPChance = WLPlatformGetter.getHitXPPercentage();
        if (object.has("hitXPChance")) {
            hitXPChance = object.get("hitXPChance").getAsInt();
        }

        int critXPAmount = WLPlatformGetter.getCritXPAmount();
        if (object.has("critXPAmount")) {
            critXPAmount = object.get("critXPAmount").getAsInt();
        }

        int critXPChance = WLPlatformGetter.getCritXPPercentage();
        if (object.has("critXPChance")) {
            critXPChance = object.get("critXPChance").getAsInt();
        }


        //Damage

        double weaponDamagePerLevel = WLPlatformGetter.getDamagePerLevel();
        if (object.has("weaponDamagePerLevel")) {
            weaponDamagePerLevel = object.get("weaponDamagePerLevel").getAsDouble();
        }

        double bowlikeModifier = WLPlatformGetter.getBowlikeModifier();
        if (object.has("bowlikeModifier")) {
            bowlikeModifier = object.get("bowlikeModifier").getAsDouble();
        }

        double armorMaxDamageReduction = WLPlatformGetter.getMaxDamageReduction();
        if (object.has("armorMaxDamageReduction")) {
            armorMaxDamageReduction = object.get("armorMaxDamageReduction").getAsDouble();
        }

        int armorXPRNGModifier = WLPlatformGetter.getArmorXPRNGModifier();
        if (object.has("armorXPRNGModifier")) {
            armorXPRNGModifier = object.get("armorXPRNGModifier").getAsInt();
        }

        return new LevelableItem(item, disabled, isMeleeWeapon, isProjectileWeapon, isArmor, maxLevel, levelModifier, levelStartAmount, hitXPAmount, hitXPChance, critXPAmount, critXPChance, weaponDamagePerLevel, bowlikeModifier, armorMaxDamageReduction, armorXPRNGModifier);
    }
}
