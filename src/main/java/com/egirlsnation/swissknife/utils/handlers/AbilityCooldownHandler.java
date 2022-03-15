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

package com.egirlsnation.swissknife.utils.handlers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AbilityCooldownHandler {

    private final Map<UUID, Long> swordCooldowns = new HashMap<>();
    private final Map<UUID, Long> axeCooldowns = new HashMap<>();
    private final Map<UUID, Long> crystalCooldowns = new HashMap<>();
    private final Map<UUID, Long> pickaxeCooldowns = new HashMap<>();

    public final int DEFAULT_AXE_COOLDOWN = 10;
    public final int DEFAULT_SWORD_COOLDOWN = 15;
    public final int DEFAULT_CRYSTAL_COOLDOWN = 300;
    public final int DEFAULT_PICKAXE_COOLDOWN = 30;

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

    public boolean isOnAxeCooldown(Player player){
        long timeLeft = System.currentTimeMillis() - getAxeCooldown(player.getUniqueId());
        return TimeUnit.MILLISECONDS.toSeconds(timeLeft) < DEFAULT_AXE_COOLDOWN;
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

    public boolean isOnSwordCooldown(Player player){
        long timeLeft = System.currentTimeMillis() - getSwordCooldown(player.getUniqueId());
        return TimeUnit.MILLISECONDS.toSeconds(timeLeft) < DEFAULT_SWORD_COOLDOWN;
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

    public boolean isOnCrystalCooldown(Player player){
        long timeLeft = System.currentTimeMillis() - getCrystalCooldown(player.getUniqueId());
        return TimeUnit.MILLISECONDS.toSeconds(timeLeft) < DEFAULT_CRYSTAL_COOLDOWN;
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

    public long getPickaxeCooldown(UUID identifier){
        return crystalCooldowns.getOrDefault(identifier, 0L);
    }

    public void setPickaxeCooldown(UUID identifier, long time){
        if(time < 1){
            pickaxeCooldowns.remove(identifier);
        }else{
            crystalCooldowns.put(identifier, time);
        }
    }

    public boolean isOnPickaxeCooldown(Player player){
        long timeLeft = System.currentTimeMillis() - getPickaxeCooldown(player.getUniqueId());
        return TimeUnit.MILLISECONDS.toSeconds(timeLeft) < DEFAULT_PICKAXE_COOLDOWN;
    }

    public long getPickaxeRemainingTime(Player player){
        UUID playerUUID = player.getUniqueId();
        if(!crystalCooldowns.containsKey(playerUUID)) return 0;

        long timeDifference = System.currentTimeMillis() - crystalCooldowns.get(playerUUID);
        if(TimeUnit.MILLISECONDS.toSeconds(timeDifference) >= DEFAULT_PICKAXE_COOLDOWN) return 0;

        return DEFAULT_PICKAXE_COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timeDifference);
    }

    public String getCooldownMessage(long remainingTime){
        return remainingTime + " seconds before you can use the ability again.";
    }

    public void removeAllCooldowns(Player player){
        swordCooldowns.remove(player.getUniqueId());
        axeCooldowns.remove(player.getUniqueId());
        crystalCooldowns.remove(player.getUniqueId());
        pickaxeCooldowns.remove(player.getUniqueId());
    }
}
