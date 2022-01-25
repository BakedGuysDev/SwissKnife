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

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerHeads extends Module {
    public PlayerHeads() {
        super(Categories.Player, "player-heads", "Adds playerheads with cool lores");
    }

    @EventHandler
    private void PlayerDeath(PlayerDeathEvent e){
        if(!isEnabled()) return;

        if(e.getEntity().getKiller() == null) return;
        if(!ItemUtil.isAncientOrDraconite(e.getEntity().getKiller().getInventory().getItemInMainHand())) return;
        Player killer = e.getEntity().getKiller();
        boolean canDrop = true;
        if(e.getEntity().hasPermission("swissknife.heads.nodrop")){
            canDrop = false;
        }
        //TODO: Config option for playernames that can't drop heads at all

        //TODO: New config options for head drop chances based on ranks, ect.
    }
}
