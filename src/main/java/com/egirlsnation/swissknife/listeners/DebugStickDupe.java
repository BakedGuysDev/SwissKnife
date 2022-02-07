/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.listeners;

import com.egirlsnation.swissknife.utils.Config;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DebugStickDupe implements Listener {

    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    @EventHandler
    private void blockBreak(BlockBreakEvent e){
        if(!Config.instance.debugStickDupe) return;
        if(e.getBlock().getState() instanceof ShulkerBox){
            if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DEBUG_STICK)){
                Long cooldown = cooldownMap.get(e.getPlayer().getUniqueId());
                if(cooldown != null){
                    if((cooldown + 9500) > System.currentTimeMillis()){
                        e.setCancelled(true);
                        return;
                    }
                }

                cooldownMap.remove(e.getPlayer().getUniqueId());
                for(ItemStack item : e.getBlock().getDrops()){
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
                }
                e.getBlock().breakNaturally();
                cooldownMap.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        }
    }


    @EventHandler
    private void blockStartMining(BlockDamageEvent e){
        if(!Config.instance.debugStickDupe) return;
        if(e.getItemInHand().getType().equals(Material.DEBUG_STICK)){
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 0));
        }

    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent e){
        if(!Config.instance.debugStickDupe) return;
        cooldownMap.remove(e.getPlayer().getUniqueId());
    }
}
