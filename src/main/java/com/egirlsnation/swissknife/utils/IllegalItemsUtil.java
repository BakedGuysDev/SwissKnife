/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.utils;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Map;

public class IllegalItemsUtil {

    public static boolean isOverEnchanted(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasEnchants()) return false;

        Map<Enchantment, Integer> enchantMap = meta.getEnchants();
        for(Map.Entry<Enchantment, Integer> enchant: enchantMap.entrySet()){
            if(enchant.getValue() > Config.instance.maxEnchantLevel) return true;
        }
        return false;
    }

    public static void notifyPlayerAboutOEI(Player player){
        player.sendMessage(Config.instance.prefix + ChatColor.RED + "Over-enchanted item found. This incident will be reported");
    }

    public static boolean hasIllegalLore(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();

        if(!meta.hasLore()) return false;
        if(meta.lore() == null) return false;

        return meta.lore().stream().anyMatch(element -> Config.instance.illegalLoreList.contains(element));
    }

    public static void notifyPlayerAboutIllegal(Player player){
        player.sendMessage(Config.instance.prefix + ChatColor.RED + "Illegal item found. This incident will be reported");
    }

    public static void notifyPlayerAboutOSI(Player player){
        player.sendMessage(Config.instance.prefix + ChatColor.RED + "Overstacked item found. The stack has been trimmed");
    }

}