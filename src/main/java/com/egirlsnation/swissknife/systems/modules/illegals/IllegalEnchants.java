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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class IllegalEnchants extends Module {
    public IllegalEnchants() {
        super(Categories.Illegals, "illegal-enchants", "Removes items with way too high enchant levels or fixes them");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> fixValues = sgGeneral.add(new BoolSetting.Builder()
            .name("fix-enchants")
            .description("Force all enchants to have vanilla values instead of removing the items")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> maxValue = sgGeneral.add(new IntSetting.Builder()
            .name("max-value")
            .description("Value to compare enchants to when removing over-enchanted items")
            .defaultValue(10)
            .min(0)
            .build()
    );

    private final Setting<Boolean> dispenserCheck = sgGeneral.add(new BoolSetting.Builder()
            .name("dispenser-check")
            .description("If the plugin should check items being dispensed from dispensers")
            .defaultValue(true)
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
            .description("If the plugin should alert players when it fixes/removes over-enchanted items")
            .defaultValue(false)
            .build()
    );

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
            .name("message")
            .description("The message to send (supports color codes)")
            .defaultValue(ChatColor.RED + "Over-enchanted item found. This incident will be reported")
            .build()
    );

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("logging")
            .description("If the plugin should log when it fixes/removes over-enchanted items")
            .defaultValue(false)
            .build()
    );


    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;

        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if(fixValues.get()){
            if(scanAndFixEnchants(e.getInventory())){
                if(alertPlayers.get() && (e.getPlayer() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getPlayer()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                    alertPlayer(e.getPlayer());
                }
                if(log.get()){
                    if(e.getInventory().getLocation() == null){
                        info("Fixed over-enchanted item in inventory opened by " + e.getPlayer().getName());
                    }else{
                        info("Fixed over-enchanted item in inventory at: " + LocationUtil.getLocationString(e.getInventory().getLocation()) + " opened by " + e.getPlayer().getName());
                    }

                }
            }
        }else{
            if(scanAndRemoveFromInv(e.getInventory())){
                if(alertPlayers.get() && (e.getPlayer() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getPlayer()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                    alertPlayer(e.getPlayer());
                }
                if(log.get()){
                    if(e.getInventory().getLocation() == null){
                        info("Removed over-enchanted item in inventory opened by " + e.getPlayer().getName());
                    }else{
                        info("Removed over-enchanted item in inventory at: " + LocationUtil.getLocationString(e.getInventory().getLocation()) + " opened by " + e.getPlayer().getName());
                    }
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
        if(fixValues.get()){
            if(scanAndFixEnchants(e.getClickedInventory())){
                e.setCancelled(true);
                if(alertPlayers.get() && (e.getWhoClicked() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getWhoClicked()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                    alertPlayer(e.getWhoClicked());
                }
                if(log.get()){
                    if(e.getClickedInventory().getLocation() == null){
                        info("Fixed over-enchanted item in inventory opened by " + e.getWhoClicked().getName());
                    }else{
                        info("Fixed over-enchanted item in inventory at: " + LocationUtil.getLocationString(e.getClickedInventory().getLocation()) + " opened by " + e.getWhoClicked().getName());
                    }

                }
            }
        }else{
            if(scanAndRemoveFromInv(e.getClickedInventory())){
                e.setCancelled(true);
                if(alertPlayers.get() && (e.getWhoClicked() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getWhoClicked()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                    alertPlayer(e.getWhoClicked());
                }
                if(log.get()){
                    if(e.getClickedInventory().getLocation() == null){
                        info("Removed over-enchanted item in inventory opened by " + e.getWhoClicked().getName());
                    }else{
                        info("Removed over-enchanted item in inventory at: " + LocationUtil.getLocationString(e.getClickedInventory().getLocation()) + " opened by " + e.getWhoClicked().getName());
                    }
                }
            }
        }

    }

    private boolean scanAndRemoveFromInv(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(ItemUtil.isOverEnchanted(item, maxValue.get())){
                inv.remove(item);
                found = true;
            }
        }
        return found;
    }

    private boolean scanAndFixEnchants(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(ItemUtil.hasEnchants(item)){
                Map<Enchantment, Integer> enchantMap = new HashMap<>();
                for(Map.Entry<Enchantment, Integer> entry : item.getItemMeta().getEnchants().entrySet()){
                    if(entry.getValue() > entry.getKey().getMaxLevel()){
                        item.removeEnchantment(entry.getKey());
                        enchantMap.put(entry.getKey(), entry.getKey().getMaxLevel());
                        found = true;
                    }
                }

                if(found){
                    item.addUnsafeEnchantments(enchantMap);
                }
            }
        }
        return found;
    }

    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent e){
        if(!isEnabled()) return;
        if(!(e.getEntity() instanceof HumanEntity)) return;

        if(e.getEntity().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if(fixValues.get()){
            boolean found = false;
            if(ItemUtil.hasEnchants(e.getItem().getItemStack())){
                ItemMeta meta = e.getItem().getItemStack().getItemMeta();
                Map<Enchantment, Integer> enchantMap = new HashMap<>();
                for(Map.Entry<Enchantment, Integer> entry : e.getItem().getItemStack().getItemMeta().getEnchants().entrySet()){
                    if(entry.getValue() > entry.getKey().getMaxLevel()){
                        //entry.setValue(entry.getKey().getMaxLevel()); //UnsupportedOperationException: null
                        meta.removeEnchant(entry.getKey());
                        enchantMap.put(entry.getKey(), entry.getKey().getMaxLevel());
                        found = true;
                    }
                }
                if(found){
                    e.getItem().getItemStack().addUnsafeEnchantments(enchantMap);
                    if(alertPlayers.get() && (e.getEntity() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getEntity()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                        alertPlayer(e.getEntity());
                    }
                    if(log.get()){
                        info("Fixed over-enchanted item being picked up by: " + e.getEntity().getName() + " at: " + LocationUtil.getLocationString(e.getEntity().getLocation()));
                    }
                }
            }
        }else{
            if(ItemUtil.isOverEnchanted(e.getItem().getItemStack(), maxValue.get())){
                e.getItem().remove();
                e.setCancelled(true);

                if(alertPlayers.get() && (e.getEntity() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getEntity()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                    alertPlayer(e.getEntity());
                }
                if(log.get()){
                    info("Removed over-enchanted item being picked up by: " + e.getEntity().getName() + " at: " + LocationUtil.getLocationString(e.getEntity().getLocation()));
                }
            }
        }
    }

    private void alertPlayer(Entity entity){
        if(entity instanceof Player){
            sendMessage((Player) entity, ChatColor.translateAlternateColorCodes('§', message.get()));
        }
    }


    @EventHandler
    private void onBlockDispenseEvent(BlockDispenseEvent e){
        if(!isEnabled()) return;
        if(!dispenserCheck.get()) return;
        if(ItemUtil.isOverEnchanted(e.getItem(), maxValue.get())) {
            e.setItem(ItemUtil.getReplacementItem());
        }
    }


}
