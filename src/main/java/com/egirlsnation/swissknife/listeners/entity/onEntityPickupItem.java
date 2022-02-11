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

package com.egirlsnation.swissknife.listeners.entity;

import com.egirlsnation.swissknife.api.IllegalItemHandler;
import com.egirlsnation.swissknife.utils.handlers.customItems.AnniversaryItemHanlder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class onEntityPickupItem implements Listener {

    private final IllegalItemHandler illegalItemHandler = new IllegalItemHandler();
    private final AnniversaryItemHanlder anniversaryItemHanlder = new AnniversaryItemHanlder();

    @EventHandler
    private void onEntityPickupItemEvent(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        if(illegalItemHandler.handleIllegals(e.getItem(), player)){
            e.setCancelled(true);
        }
    }
}
