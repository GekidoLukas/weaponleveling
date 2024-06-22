package net.weaponleveling.util;

import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.weaponleveling.WeaponLevelingConfig;
import net.weaponleveling.WeaponLevelingMod;

import java.util.function.Supplier;

public class DataGetter {


    public static Integer HIT_XP_AMOUNT = null;
    public static int getHitXpAmount() {
        if(HIT_XP_AMOUNT == null) {
            return WeaponLevelingConfig.hit_xp_amount;
        }
        return HIT_XP_AMOUNT;
    }
    public static Integer HIT_PERCENTAGE = null;
    public static int getHitPercentage() {
        if(HIT_PERCENTAGE == null) {
            return WeaponLevelingConfig.hit_percentage;
        }
        return HIT_PERCENTAGE;
    }

    public static Integer CRIT_XP_AMOUNT = null;
    public static int getCritXpAmount() {
        if(CRIT_XP_AMOUNT == null) {
            return WeaponLevelingConfig.crit_xp_amount;
        }
        return CRIT_XP_AMOUNT;
    }

    public static Integer CRIT_PERCENTAGE = null;
    public static int getCritPercentage() {
        if(CRIT_PERCENTAGE == null) {
            return WeaponLevelingConfig.crit_percentage;
        }
        return CRIT_PERCENTAGE;
    }

    public static Integer MAX_LEVEL = null;
    public static int getMaxLevel() {
        if(MAX_LEVEL == null) {
            return WeaponLevelingConfig.max_item_level;
        }
        return MAX_LEVEL;
    }

    public static Integer LEVEL_MODIFIER = null;
    public static int getLevelModifier() {
        if(LEVEL_MODIFIER == null) {
            return WeaponLevelingConfig.level_modifier;
        }
        return LEVEL_MODIFIER;
    }

    public static Integer STARTING_XP = null;
    public static int getStartingXp() {
        if(STARTING_XP == null) {
            return WeaponLevelingConfig.starting_xp_amount;
        }
        return STARTING_XP;
    }

    public static Double DAMAGE_PER_LEVEL = null;
    public static double getDamagePerLevel() {
        if(DAMAGE_PER_LEVEL == null) {
            return WeaponLevelingConfig.damage_per_level;
        }
        return DAMAGE_PER_LEVEL;
    }

    public static Double ARMOR_PER_LEVEL = null;
    public static double getArmorPerLevel() {
        if(ARMOR_PER_LEVEL == null) {
            return WeaponLevelingConfig.armor_per_level;
        }
        return ARMOR_PER_LEVEL;
    }

    public static Double TOUGHNESS_PER_LEVEL = null;
    public static double getToughnessPerLevel() {
        if(TOUGHNESS_PER_LEVEL == null) {
            return WeaponLevelingConfig.toughness_per_level;
        }
        return TOUGHNESS_PER_LEVEL;
    }

    public static Integer ARMOR_RNG = null;
    public static int getArmorRng() {
        if(ARMOR_RNG == null) {
            return WeaponLevelingConfig.armor_rng_modifier;
        }
        return ARMOR_RNG;
    }




    public static Double BOW_LIKE_MODIFIER = null;
    public static double getBowLikeModifier() {
        if(BOW_LIKE_MODIFIER == null) {
            return WeaponLevelingConfig.bow_like_damage_modifier;
        }
        return BOW_LIKE_MODIFIER;
    }

    public static Boolean BROKEN_ITEMS_WONT_VANISH = null;
    public static boolean getBrokenItemsWontVanish() {
        if(BROKEN_ITEMS_WONT_VANISH == null) {
            return WeaponLevelingConfig.broken_items_wont_vanish;
        }
        return BROKEN_ITEMS_WONT_VANISH;
    }

    public static Boolean DISABLE_UNLISTED = null;
    public static Boolean getDisableUnlisted() {
        if(DISABLE_UNLISTED == null) {
            return WeaponLevelingConfig.disable_unlisted_items;
        }
        return DISABLE_UNLISTED;
    }

    public static Boolean LEVELABLE_AUTO_UNBREAKABLE = null;
    public static Boolean getLevelableAutoUnbreakable() {
        if(LEVELABLE_AUTO_UNBREAKABLE == null) {
            return WeaponLevelingConfig.levelable_items_auto_unbreakable;
        }
        return LEVELABLE_AUTO_UNBREAKABLE;
    }

    public static Integer XP_GENERIC = null;
    public static Integer getXpGeneric() {
        if(XP_GENERIC == null) {
            return WeaponLevelingConfig.xp_for_generic_kill;
        }
        return XP_GENERIC;
    }

    public static Integer XP_ANIMAL = null;

    public static Integer getXpAnimal() {
        if(XP_ANIMAL == null) {
            return WeaponLevelingConfig.xp_for_animal_kill;
        }
        return XP_ANIMAL;
    }
    public static Integer XP_MONSTER = null;

    public static Integer getXpMonster() {
        if(XP_MONSTER == null) {
            return WeaponLevelingConfig.xp_for_monster_kill;
        }
        return XP_MONSTER;
    }

    public static Integer XP_MINIBOSS = null;
    public static Integer getXpMiniboss() {
        if(XP_MINIBOSS == null) {
            return WeaponLevelingConfig.xp_for_mini_boss_kill;
        }
        return XP_MINIBOSS;
    }

    public static Integer XP_BOSS = null;
    public static Integer getXpBoss() {
        if(XP_BOSS == null) {
            return WeaponLevelingConfig.xp_for_boss_kill;
        }
        return XP_BOSS;
    }

    public static final TagKey<Item> blacklist_items = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"blacklist_items"));
    public static final TagKey<Item> melee_items = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"melee_items"));
    public static final TagKey<Item> projectile_items = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"projectile_items"));
    public static final TagKey<Item> armor_items = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"armor_items"));
    public static final TagKey<Item> non_vanish_items_whitelist = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"non_vanish_items_whitelist"));
    public static final TagKey<Item> non_vanish_items_blacklist = TagKey.create(Registries.ITEM,new ResourceLocation(WeaponLevelingMod.MODID,"non_vanish_items_blacklist"));



    public static final TagKey<EntityType<?>> entities_blacklist = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(WeaponLevelingMod.MODID,"entities_blacklist"));
    public static final TagKey<EntityType<?>> entities_animal = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(WeaponLevelingMod.MODID,"entities_animal"));
    public static final TagKey<EntityType<?>> entities_monster = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(WeaponLevelingMod.MODID,"entities_monster"));
    public static final TagKey<EntityType<?>> entities_mini_boss = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(WeaponLevelingMod.MODID,"entities_mini_boss"));
    public static final TagKey<EntityType<?>> entities_boss = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(WeaponLevelingMod.MODID,"entities_boss"));
}
