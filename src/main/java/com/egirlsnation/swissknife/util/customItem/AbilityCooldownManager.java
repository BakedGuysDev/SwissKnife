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

package com.egirlsnation.swissknife.util.customItem;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AbilityCooldownManager {

    private static final Map<UUID, Long> swordCooldowns = new HashMap<>();
    private static final Map<UUID, Long> axeCooldowns = new HashMap<>();
    private static final Map<UUID, Long> crystalCooldowns = new HashMap<>();

    public static final int DEFAULT_AXE_COOLDOWN = 10;
    public static final int DEFAULT_SWORD_COOLDOWN = 15;
    public static final int DEFAULT_CRYSTAL_COOLDOWN = 300;

    public void setAxeCooldown(UUID identifier, long time){
        if(time < 1){
            axeCooldowns.remove(identifier);
        }else{
            axeCooldowns.put(identifier, time);
        }
    }

    public long getAxeCooldown(UUID identifier){
        return axeCooldowns.getOrDefault(identifier, 0L);
    }

    public void removeAxeCooldown(Player player){
        axeCooldowns.remove(player.getUniqueId());
    }

    public long getAxeRemainingTime(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!axeCooldowns.containsKey(playerUUID)) return 0;

        long timeDifference = System.currentTimeMillis() - axeCooldowns.get(playerUUID);
        if(TimeUnit.MILLISECONDS.toSeconds(timeDifference) >= DEFAULT_AXE_COOLDOWN) return 0;

        return DEFAULT_AXE_COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timeDifference);
    }

    public void setSwordCooldown(UUID identifier, long time){
        if(time < 1){
            swordCooldowns.remove(identifier);
        }else{
            swordCooldowns.put(identifier, time);
        }
    }

    public long getSwordCooldown(UUID identifier){
        return swordCooldowns.getOrDefault(identifier, 0L);
    }

    public void removeSwordCooldown(Player player){
        swordCooldowns.remove(player.getUniqueId());
    }

    public long getSwordRemainingTime(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!swordCooldowns.containsKey(playerUUID)) return 0;

        long timeDifference = System.currentTimeMillis() - swordCooldowns.get(playerUUID);
        if(TimeUnit.MILLISECONDS.toSeconds(timeDifference) >= DEFAULT_SWORD_COOLDOWN) return 0;

        return DEFAULT_SWORD_COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timeDifference);
    }


    public void setCrystalCooldown(UUID identifier, long time){
        if(time < 1){
            crystalCooldowns.remove(identifier);
        }else{
            crystalCooldowns.put(identifier, time);
        }
    }

    public long getCrystalCooldown(UUID identifier){
        return crystalCooldowns.getOrDefault(identifier, 0L);
    }

    public void removeCrystalCooldown(Player player){
        crystalCooldowns.remove(player.getUniqueId());
    }

    public long getCrystalRemainingTime(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!crystalCooldowns.containsKey(playerUUID)) return 0;

        long timeDifference = System.currentTimeMillis() - crystalCooldowns.get(playerUUID);
        if(TimeUnit.MILLISECONDS.toSeconds(timeDifference) >= DEFAULT_CRYSTAL_COOLDOWN) return 0;

        return DEFAULT_CRYSTAL_COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timeDifference);
    }

    public String getCooldownMessage(long remainingTime){
        return remainingTime + " seconds before you can use the ability again.";
    }
}
