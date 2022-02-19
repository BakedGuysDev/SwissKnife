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

package com.egirlsnation.swissknife.systems.modules.illegals;

import com.egirlsnation.swissknife.settings.*;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class IllegalLores extends Module {
    public IllegalLores() {
        super(Categories.Illegals, "illegal-lores", "Removes items with certain lores");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> illegalLores = sgGeneral.add(new StringListSetting.Builder()
            .name("illegal-lores")
            .description("List of lores found on items that should get removed")
            .defaultValue(Arrays.asList("§9§lBig Dick Energy X", "§cCurse of Simping"))
            .build()
    );

    private final Setting<Boolean> bypass = sgGeneral.add(new BoolSetting.Builder()
            .name("bypass")
            .description("If the check can be bypassed by permissions")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> alertPlayers = sgGeneral.add(new BoolSetting.Builder()
            .name("alert-players")
            .description("If the plugin should alert players when it detects item with illegal lore")
            .defaultValue(false)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "Item with illegal lore found. This incident will be reported")
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when it removes item with illegal lore")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;

        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if(scanAndRemoveFromInv(e.getInventory()) && e.getPlayer() instanceof Player){
            e.setCancelled(true);
            if(alertPlayers.get()){
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('§', message.get()));
            }
            if(log.get()){
                if(e.getInventory().getLocation() == null){
                    info("Found item with illegal lore in inventory opened by " + e.getPlayer().getName());
                }else{
                    info("Found item with illegal lore in inventory at: " + LocationUtil.getLocationString(e.getInventory().getLocation()) + " opened by " + e.getPlayer().getName());
                }
            }
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e){
        if(!isEnabled()) return;

        if(e.getWhoClicked().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if(e.getClickedInventory() == null) return;
        if(scanAndRemoveFromInv(e.getClickedInventory()) && e.getWhoClicked() instanceof Player){
            e.setCancelled(true);
            if(alertPlayers.get()){
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('§', message.get()));
            }
            if(log.get()){
                if(e.getClickedInventory().getLocation() == null){
                    info("Found item with illegal lore clicked by " + e.getWhoClicked().getName() + " in inventory");
                }else{
                    info("Found item with illegal lore clicked by " + e.getWhoClicked().getName() + " in inventory at: " + LocationUtil.getLocationString(e.getClickedInventory().getLocation()));
                }
            }
        }

    }

    private boolean scanAndRemoveFromInv(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(hasIllegalLore(item)){
                inv.remove(item);
                found = true;
            }
        }
        return found;
    }

    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent e){
        if(!isEnabled()) return;

        if(e.getEntity().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if(!(e.getEntity() instanceof HumanEntity)) return;

        if(hasIllegalLore(e.getItem().getItemStack())){
            e.getItem().remove();
            e.setCancelled(true);

            if(alertPlayers.get()){
                e.getEntity().sendMessage(ChatColor.translateAlternateColorCodes('§', message.get()));
            }
            if(log.get()){
                info("Removed item with illegal lore being picked up by " + e.getEntity().getName() + " at: " + LocationUtil.getLocationString(e.getEntity().getLocation()));
            }
        }
    }

    private boolean hasIllegalLore(@Nullable ItemStack item){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();

        if(!meta.hasLore()) return false;
        if(meta.getLore() == null) return false;

        return meta.getLore().stream().anyMatch(element -> illegalLores.get().contains(element));
    }
}
