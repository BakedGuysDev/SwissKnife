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

package com.egirlsnation.swissknife.listeners.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.api.IllegalItemHandler;
import com.egirlsnation.swissknife.utils.handlers.customItems.DraconiteAbilityHandler;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class onPlayerInteract implements Listener {

    private final SwissKnife plugin;
    public onPlayerInteract(SwissKnife plugin){ this.plugin = plugin; }

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    private final DraconiteAbilityHandler draconiteAbilityHandler = new DraconiteAbilityHandler();

    private final static Map<UUID, Long> crystalMap = new HashMap<>();

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent e){

        if(e.getClickedBlock() != null && illegalItemHandler.isSpawnEgg(e.getItem())){
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot do this m8");
            e.getItem().setAmount(0);
            e.setCancelled(true);
        }



        if(!Action.RIGHT_CLICK_BLOCK.equals(e.getAction())) return;
        if(e.getMaterial().equals(Material.TNT_MINECART) && e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !e.getPlayer().hasPermission("swissknife.bypass.creative")){
            e.setCancelled(true);
            return;
        }

    }
}
