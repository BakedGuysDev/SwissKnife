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
import com.egirlsnation.swissknife.utils.server.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TotemStackLimiter extends Module {
    public TotemStackLimiter() {
        super(Categories.Illegals, "totem-stack-limiter", "Limits how big illegally stacked totem stacks can be");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> maxTotemStack = sgGeneral.add(new IntSetting.Builder()
            .name("max-stack")
            .description("Max stack of totem stacks")
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
            .description("If the plugin should alert player when trimming totem stacks")
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
            .description("If the plugin should log when trimming totem stacks")
            .defaultValue(false)
            .build()
    );

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e){
        if(!isEnabled()) return;

        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if(scanAndTrimTotemStack(e.getInventory()) && e.getPlayer() instanceof Player){
            if(alertPlayers.get() && (e.getPlayer() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getPlayer()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                alertPlayer(e.getPlayer());
            }

            if(log.get()){
                if(e.getInventory().getLocation() != null){
                    info("Trimmed totem stack in inventory opened by " + e.getPlayer().getName() + " at: " + LocationUtil.getLocationString(e.getInventory().getLocation()));
                }else{
                    info("Trimmed totem stack in inventory opened by " + e.getPlayer().getName());
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
        if(scanAndTrimTotemStack(e.getClickedInventory()) && e.getWhoClicked() instanceof Player){
            e.setCancelled(true);
            if(alertPlayers.get() && (e.getWhoClicked() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getWhoClicked()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                alertPlayer(e.getWhoClicked());
            }

            if(log.get()){
                if(e.getClickedInventory().getLocation() != null){
                    info("Trimmed totem stack in inventory clicked by " + e.getWhoClicked().getName() + " at: " + LocationUtil.getLocationString(e.getClickedInventory().getLocation()));
                }else{
                    info("Trimmed totem stack in inventory clicked by " + e.getWhoClicked().getName());
                }
            }
        }

    }



    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent e){
        if(!isEnabled()) return;

        if(e.getEntity().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if(!(e.getEntity() instanceof HumanEntity)) return;

        if(e.getItem().getItemStack().getType().equals(Material.TOTEM_OF_UNDYING) && e.getItem().getItemStack().getAmount() > maxTotemStack.get()){
            ItemStack is = e.getItem().getItemStack();
            is.setAmount(maxTotemStack.get());
            e.getItem().setItemStack(is);

            if(alertPlayers.get() && (e.getEntity() instanceof Player) && SwissPlayer.getSwissPlayer((Player) e.getEntity()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                alertPlayer(e.getEntity());
            }

            if(log.get()){
                info("Trimmed totem stack to be picked up by " + e.getEntity().getName());
            }
        }
    }

    @EventHandler
    private void SwapHandItems(PlayerSwapHandItemsEvent e) {
        if(!isEnabled()) return;

        if(e.getPlayer().hasPermission("swissknife.bypass.illegals") && bypass.get()){
            return;
        }

        if (e.getOffHandItem() != null) {
            if (e.getOffHandItem().getType().equals(Material.TOTEM_OF_UNDYING)) {
                if (e.getOffHandItem().getAmount() > maxTotemStack.get()) {
                    e.getOffHandItem().setAmount(maxTotemStack.get());

                    if(alertPlayers.get() && SwissPlayer.getSwissPlayer(e.getPlayer()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                        alertPlayer(e.getPlayer());
                    }

                    if(log.get()){
                        info("Found over-stacked totems on " + e.getPlayer().getName());
                    }
                }
            }
        }

        if (e.getMainHandItem() != null) {
            if (e.getMainHandItem().getType().equals(Material.TOTEM_OF_UNDYING)) {
                if (e.getMainHandItem().getAmount() > maxTotemStack.get()) {
                    e.getMainHandItem().setAmount(maxTotemStack.get());

                    if(alertPlayers.get() && SwissPlayer.getSwissPlayer(e.getPlayer()).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                        alertPlayer(e.getPlayer());
                    }

                    if(log.get()){
                        info("Found over-stacked totems on " + e.getPlayer().getName());
                    }
                }
            }
        }
    }

    private void alertPlayer(Entity entity){
        if(entity instanceof Player){
            sendMessage((Player) entity, ChatColor.translateAlternateColorCodes('§', message.get()));
        }
    }

    public boolean scanAndTrimTotemStack(Inventory inv){
        boolean found = false;
        for(ItemStack item : inv.getContents()){
            if(item == null) continue;
            if(item.getType().equals(Material.TOTEM_OF_UNDYING) && item.getAmount() > maxTotemStack.get()){
                item.setAmount(maxTotemStack.get());
                found = true;
            }
        }
        return found;
    }
}
