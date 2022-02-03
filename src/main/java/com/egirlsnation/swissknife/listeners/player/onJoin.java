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
import com.egirlsnation.swissknife.utils.entity.player.GamemodeUtil;
import com.egirlsnation.swissknife.utils.entity.player.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class onJoin implements Listener {

    private final SwissKnife plugin;
    public onJoin(SwissKnife plugin){ this.plugin = plugin; }

    private final GamemodeUtil gamemodeUtil = new GamemodeUtil();
    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    private final RankUtil rankUtil = new RankUtil();

    @EventHandler(priority = EventPriority.HIGH)
    private void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                gamemodeUtil.ensureFlyDisable(player);
            }
        }, 20);

        if(!player.hasPlayedBefore()) return;

        rankUtil.promoteIfEligible(player);

        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                for(ItemStack item : player.getInventory().getContents()){
                    illegalItemHandler.handleIllegals(item, player);
                }
            }
        });



    }
}
