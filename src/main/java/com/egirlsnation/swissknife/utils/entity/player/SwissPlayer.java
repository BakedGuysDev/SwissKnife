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
import com.egirlsnation.swissknife.systems.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SwissPlayer {
    private final UUID uuid;
    private final String playerName;
    private final Map<SwissFeature, Boolean> featureStates = new HashMap<>(1);
    private boolean loadedFeatureData = false;

    private static final Map<UUID, SwissPlayer> swissPlayers = new HashMap<>(1);

    private static final Map<UUID, BukkitTask> removalTasks = new HashMap<>(1);

    private SwissPlayer(Player player){
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();

        for(SwissFeature feature : SwissFeature.values()){
            featureStates.put(feature, true);
        }

        if(MySQL.get().isConnected()){
            Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
                Map<SwissFeature, Boolean> sqlFeatureMap = MySQL.get().getPlayerDataDriver().getFeatureMap(uuid);
                if(sqlFeatureMap != null){
                    featureStates.putAll(sqlFeatureMap);
                }
                loadedFeatureData = true;
            });
        }
    }

    public static SwissPlayer getSwissPlayer(Player player){
        if(swissPlayers.containsKey(player.getUniqueId())){
            return swissPlayers.get(player.getUniqueId());
        }else{
            SwissPlayer swissPlayer = new SwissPlayer(player);
            swissPlayers.put(player.getUniqueId(), swissPlayer);
            return swissPlayer;
        }
    }

    public UUID getUuid(){
        return uuid;
    }

    public String getPlayerName(){
        return playerName;
    }

    public boolean hasLoadedFeatureData(){
        return loadedFeatureData;
    }

    public boolean hasFeatureEnabled(SwissFeature feature){
        return featureStates.get(feature);
    }

    public void setFeature(SwissFeature feature, boolean enabled){
        featureStates.put(feature, enabled);
    }

    public void toggleFeature(SwissFeature feature){
        if(!featureStates.get(feature)){
            featureStates.put(feature, true);
        }else{
            featureStates.put(feature, false);
        }
    }

    public static void scheduleSave(Player player){
        BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(SwissKnife.INSTANCE, () -> {
            SwissPlayer swissPlayer = swissPlayers.remove(player.getUniqueId());
            swissPlayer.save();
        }, 5 * 60 * 20);
        removalTasks.put(player.getUniqueId(), task);
    }

    public static void cancelSave(Player player){
        BukkitTask task = removalTasks.get(player.getUniqueId());
        if(task != null){
            task.cancel();
            removalTasks.remove(player.getUniqueId(), task);
        }
    }

    private void save(){
        if(MySQL.get().isConnected()){
            MySQL.get().getPlayerDataDriver().updatePlayerData(this);
        }
        swissPlayers.remove(uuid);
    }

    public enum SwissFeature{
        PET_TOTEMS,
        DRACONITE_ABILITIES,
        MODULE_ALERTS
    }
}
