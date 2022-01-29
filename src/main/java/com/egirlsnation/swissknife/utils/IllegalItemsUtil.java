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

import com.egirlsnation.swissknife.systems.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Map;

public class IllegalItemsUtil {

    public static boolean hasEnchants(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasEnchants();
    }

    public static boolean isOverEnchanted(@Nullable ItemStack item, int maxEnchantLevel){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasEnchants()) return false;

        Map<Enchantment, Integer> enchantMap = meta.getEnchants();
        for(Map.Entry<Enchantment, Integer> enchant: enchantMap.entrySet()){
            if(enchant.getValue() > maxEnchantLevel) return true;
        }
        return false;
    }

    public static void notifyPlayerAboutOSI(Player player){
        player.sendMessage(Config.prefix+ ChatColor.RED + "Overstacked item found. The stack has been trimmed");
    }

    public static boolean isSpawnEgg(ItemStack item){
        if(item == null) return false;
        return item.getType().toString().matches("[A-Z]*?_?[A-Z]*_SPAWN_EGG");
    }

    public static ItemStack getReplacementItem(){
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "I fucked ya mom");
        paper.setItemMeta(meta);
        return paper;
    }

}
