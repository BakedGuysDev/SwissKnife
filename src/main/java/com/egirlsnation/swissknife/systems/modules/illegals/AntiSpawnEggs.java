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

package com.egirlsnation.swissknife.systems.modules.illegals;

import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.IllegalItemsUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiSpawnEggs extends Module {
    public AntiSpawnEggs() {
        super("anti-spawn-eggs", "Prevents players from using spawn eggs");
    }

    @EventHandler
    private void onBlockDispenseEvent(BlockDispenseEvent e){
        if(IllegalItemsUtil.isSpawnEgg(e.getItem())){
            e.setItem(IllegalItemsUtil.getReplacementItem());
        }
    }

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent e) {

        if (e.getClickedBlock() != null && IllegalItemsUtil.isSpawnEgg(e.getItem())) {
            IllegalItemsUtil.notifyPlayerAboutIllegal(e.getPlayer());
            e.getItem().setAmount(0);
            e.setCancelled(true);
        }
    }
}
