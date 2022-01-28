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

package com.egirlsnation.swissknife.systems.handlers;

import com.egirlsnation.swissknife.utils.OldConfig;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CombatCheckHandler {

    private static final Map<UUID, Long> combatMap = new HashMap<>();
    private static final Map<UUID, BukkitTask> elytraDisableMap = new HashMap<>();

    public static void addToCombatMap(Player player){
        UUID playerUUID = player.getUniqueId();
        combatMap.put(playerUUID, System.currentTimeMillis());
    }

    public static boolean isInCombat(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!combatMap.containsKey(playerUUID)) return false;

        long timeDifference = System.currentTimeMillis() - combatMap.get(playerUUID);
        if(timeDifference >= OldConfig.instance.combatTimeout){
            combatMap.remove(playerUUID);
            return false;
        }else{
            return true;
        }
    }

    public static long getRemainingTime(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!combatMap.containsKey(playerUUID)) return 0;

        long timeDifference = System.currentTimeMillis() - combatMap.get(playerUUID);
        if(timeDifference >= OldConfig.instance.combatTimeout) return 0;

        return TimeUnit.MILLISECONDS.toSeconds(OldConfig.instance.combatTimeout -timeDifference);
    }

    public static void removePlayer(Player player){
        combatMap.remove(player.getUniqueId());
    }

    public static Map<UUID, BukkitTask> getElytraMap(){
        return elytraDisableMap;
    }
}
