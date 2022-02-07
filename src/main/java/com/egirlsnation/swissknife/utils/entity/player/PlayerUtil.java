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

package com.egirlsnation.swissknife.utils.entity.player;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.modules.misc.KickMessageMasking;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerUtil {

    public static void killPlayer(Player player){
        final EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, Float.MAX_VALUE);
        ede.getEntity().setLastDamageCause(ede);
        SwissKnife.INSTANCE.getPluginManager().callEvent(ede);
        if(ede.isCancelled()){
            return;
        }
        player.setHealth(0);
    }

    public static void kickPlayer(Player player, String kickMessage){
        if(Modules.get().isActive(KickMessageMasking.class)){
            player.kickPlayer("Swissknife " + kickMessage);
        }else{
            player.kickPlayer(kickMessage);
        }
    }

    public static void kickPlayer(Player player){
        kickPlayer(player, ChatColor.GOLD + "Lost connection to the server ¯\\_(ツ)_/¯");
    }

}
