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
import com.egirlsnation.swissknife.utils.entity.player.SwissPlayer;
import com.egirlsnation.swissknife.utils.server.ItemUtil;
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

public class ArmorStackLimiter extends Module {
    public ArmorStackLimiter() {
        super(Categories.Illegals, "armor-stack-limiter", "Limits how big can illegally stacked armor stacks be");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> maxArmorStack = sgGeneral.add(new IntSetting.Builder()
            .name("max-stack")
            .description("Max stack of armor stacks")
            .defaultValue(3)
            .range(1,64)
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
            .description("If the plugin should alert player when trimming armor stacks")
            .defaultValue(false)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "Illegally stacked item found. This incident will be reported")
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when trimming armor stacks")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;
        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }
        if(scanAndTrimArmorStacks(e.getInventory()) && (e.getPlayer() instanceof Player)){
            if(alertPlayers.get() && SwissPlayer.getSwissPlayer((Player) e.getPlayer()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                sendMessage((Player) e.getPlayer(), ChatColor.translateAlternateColorCodes('§', message.get()));
            }
            if(log.get()){
                if(e.getInventory().getLocation() != null){
                    info("Trimmed armor stack in inventory opened by " + e.getPlayer().getName() + " at: " + LocationUtil.getLocationString(e.getInventory().getLocation()));
                }else{
                    info("Trimmed armor stack in inventory opened by " + e.getPlayer().getName());
                }

            }
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e){
        if(!isEnabled()) return;
        if(e.getClickedInventory() == null) return;
        if(e.getWhoClicked().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }
        if(scanAndTrimArmorStacks(e.getClickedInventory()) && e.getWhoClicked() instanceof Player){
            e.setCancelled(true);
            if(alertPlayers.get() && (e.getWhoClicked() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getWhoClicked()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                sendMessage((Player) e.getWhoClicked(), ChatColor.translateAlternateColorCodes('§', message.get()));
            }
            if(log.get()){
                if(e.getClickedInventory().getLocation() != null){
                    info("Trimmed armor stack in inventory clicked by " + e.getWhoClicked().getName() + " at: " + LocationUtil.getLocationString(e.getClickedInventory().getLocation()));
                }else{
                    info("Trimmed armor stack in inventory clicked by " + e.getWhoClicked().getName());
                }
            }
        }

    }

    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof HumanEntity)) return;

        if(e.getEntity().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if(ItemUtil.isArmorPiece(e.getItem().getItemStack()) && e.getItem().getItemStack().getAmount() > maxArmorStack.get()){
            ItemStack is = e.getItem().getItemStack();
            is.setAmount(maxArmorStack.get());
            e.getItem().setItemStack(is);

            if(e.getEntity() instanceof Player){
                if(alertPlayers.get() && (e.getEntity() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getEntity()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                    sendMessage((Player) e.getEntity(), ChatColor.translateAlternateColorCodes('§', message.get()));
                }
                if(log.get()){
                    info("Trimmed armor stack to be picked up by " + e.getEntity().getName() + " at: " + LocationUtil.getLocationString(e.getEntity().getLocation()));
                }
            }
        }
    }

    public boolean scanAndTrimArmorStacks(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(ItemUtil.isArmorPiece(item) && item.getAmount() > maxArmorStack.get()){
                item.setAmount(maxArmorStack.get());
                found = true;
            }
        }
        return found;
    }
}
