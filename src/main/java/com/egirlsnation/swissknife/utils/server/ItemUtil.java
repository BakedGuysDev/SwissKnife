/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.utils.server;

import com.egirlsnation.swissknife.utils.OldConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ItemUtil {

    private static final List<PotionEffectType> nonPotionList = ImmutableList.<PotionEffectType>builder().add(
            PotionEffectType.ABSORPTION,
            PotionEffectType.BAD_OMEN,
            PotionEffectType.BLINDNESS,
            PotionEffectType.CONDUIT_POWER,
            PotionEffectType.CONFUSION,
            PotionEffectType.DOLPHINS_GRACE,
            PotionEffectType.FAST_DIGGING,
            PotionEffectType.GLOWING,
            PotionEffectType.HEALTH_BOOST,
            PotionEffectType.HERO_OF_THE_VILLAGE,
            PotionEffectType.HUNGER,
            PotionEffectType.LEVITATION,
            PotionEffectType.SATURATION,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.UNLUCK,
            PotionEffectType.WITHER
    ).build();

    private static final Map<PotionEffectType, Integer> potionMaxAmplifierMap = ImmutableMap.<PotionEffectType, Integer>builder()
            .put(PotionEffectType.DAMAGE_RESISTANCE, 3)
            .put(PotionEffectType.FIRE_RESISTANCE, 0)
            .put(PotionEffectType.HARM, 1)
            .put(PotionEffectType.HEAL, 1)
            .put(PotionEffectType.INCREASE_DAMAGE, 1)
            .put(PotionEffectType.INVISIBILITY, 0)
            .put(PotionEffectType.JUMP, 1)
            .put(PotionEffectType.LUCK, 0)
            .put(PotionEffectType.NIGHT_VISION, 0)
            .put(PotionEffectType.POISON, 1)
            .put(PotionEffectType.REGENERATION, 1)
            .put(PotionEffectType.SLOW, 5)
            .put(PotionEffectType.SLOW_FALLING, 0)
            .put(PotionEffectType.SPEED, 1)
            .put(PotionEffectType.WATER_BREATHING, 0)
            .put(PotionEffectType.WEAKNESS, 0)
            .build();

    public static boolean isArmorPiece(@Nullable ItemStack item){
        if(item == null) return false;
        final String itemTypeString = item.getType().name();
        return itemTypeString.endsWith("_HELMET")
                || itemTypeString.endsWith("_CHESTPLATE")
                || itemTypeString.endsWith("_LEGGINGS")
                || itemTypeString.endsWith("_BOOTS");
    }

    public static boolean isSpawnEgg(@Nullable ItemStack item){
        if(item == null) return false;
        return item.getType().toString().matches("[A-Z]*?_?[A-Z]*_SPAWN_EGG");
    }

    public static boolean isShulkerBox(@Nullable ItemStack item){
        if(item == null) return false;
        final String itemTypeString = item.getType().name();
        return itemTypeString.endsWith("SHULKER_BOX");
    }

    public boolean hasTooLongName(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasDisplayName()) return false;
        return meta.getDisplayName().length() > OldConfig.instance.maxItemNameLength;
    }

    public ItemMeta trimName(@NotNull ItemStack item){
        if(item.getItemMeta() == null) return null;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(meta.getDisplayName().substring(0, Math.min(meta.getDisplayName().length(), OldConfig.instance.maxItemNameLength)));
        return meta;
    }

    public static boolean isAncientOrDraconite(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        if(!item.getItemMeta().hasLore()) return false;
        if(item.getItemMeta().getLore() == null) return false;
        if(item.getItemMeta().getLore().contains("§6Ancient weapon")){
            return true;
        }
        if(item.getItemMeta().getLore().contains("§cDraconite Weapon")){
            return true;
        }
        return false;
    }

    public static int getMaxPotionAmplifier(PotionEffectType effectType){
        return potionMaxAmplifierMap.get(effectType);
    }

    public static boolean isLegitPotionEffect(PotionEffectType effectType){
        return !nonPotionList.contains(effectType);
    }

}
