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

package com.egirlsnation.swissknife.util.cooldownManager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private static final Map<UUID,CommandInfo> cooldowns = new HashMap<>();

    public static final int DEFAULT_COOLDOWN = 20;

    public void setCooldown(UUID identifier, long time, CommandType cmd){
        if(time < 1){
            cooldowns.remove(identifier);
        }else{
            cooldowns.put(identifier, new CommandInfo(time, cmd));
        }
    }

    public void setCooldown(UUID identifier, CommandType cmd){
        cooldowns.put(identifier, new CommandInfo(System.currentTimeMillis(), cmd));
    }

    public CommandInfo getCommandInfo(UUID identifier, CommandType cmd){
        return cooldowns.getOrDefault(identifier, new CommandInfo(0L, cmd));
    }

    public void removePlayer(Player player){
        cooldowns.remove(player.getUniqueId());
    }

    public boolean isOnCooldown(Player player, CommandType type){

        long timeLeft = System.currentTimeMillis() - getCommandInfo(player.getUniqueId(), type).getCooldown();

        if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.DEFAULT_COOLDOWN){
            return false;
        }
        return true;
    }


}
