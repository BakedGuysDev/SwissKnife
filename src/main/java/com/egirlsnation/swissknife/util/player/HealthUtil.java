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

import com.egirlsnation.swissknife.SwissKnife;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class HealthUtil {

    public final void killPlayer(Player player, SwissKnife swissKnife){
        final EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, Float.MAX_VALUE);
        swissKnife.getPluginManager().callEvent(ede);
        ede.getEntity().setLastDamageCause(ede);
        player.setHealth(0);
    }
}
