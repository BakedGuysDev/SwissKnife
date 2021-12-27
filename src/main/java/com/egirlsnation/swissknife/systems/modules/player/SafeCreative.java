/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.systems.modules.player;

import com.egirlsnation.swissknife.systems.modules.Module;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class SafeCreative extends Module {
    public SafeCreative() {
        super("safe-creative", "Additional checks for RestrictedCreative to make it safe-ish");
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e){
        if(!e.getPlayer().isOp() && e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            e.getPlayer().setGameMode(GameMode.SURVIVAL);

        }
    }
}
