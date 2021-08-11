/*
 * This file is part of the SwissKnife plugin distibution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2021 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 *
 * You should have received a copy of the MIT
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/MIT>.
 */

package com.egirlsnation.swissknife.util.player;

import org.bukkit.entity.Player;

public class GamemodeUtil {

    public void ensureFlyDisable(Player player){
        if(!player.hasPermission("swissknife.bypass.fly")){
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }
}
