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

package com.egirlsnation.swissknife.systems.modules.database;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.settings.BoolSetting;
import com.egirlsnation.swissknife.settings.IntSetting;
import com.egirlsnation.swissknife.settings.Setting;
import com.egirlsnation.swissknife.settings.SettingGroup;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.utils.player.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class PlayerStats extends Module {
    public PlayerStats(){
        super(Categories.Database, "player-stats", "Collects player stats into a database");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> periodicalUpdate = sgGeneral.add(new BoolSetting.Builder()
            .name("update-periodically")
            .description("If the plugin should update player stats every x minutes on top of updating when player leaves")
            .defaultValue(true)
            .build()
    );

    private final Setting<Integer> updateInterval = sgGeneral.add(new IntSetting.Builder()
            .name("update-interval")
            .description("The update interval in minutes (5 to 120)")
            .defaultValue(10)
            .range(5, 120)
            .build()
    );

    private BukkitTask periodicalTask = null;

    @Override
    public void onEnable(){
        if(!periodicalUpdate.get()) return;
        if(!SwissKnife.INSTANCE.SQL.isConnected()){
            warn("Disabling... This module depends on the MySQL database, which is not connected.");
            toggle();
            return;
        }
        periodicalTask = Bukkit.getScheduler().runTaskTimer(SwissKnife.INSTANCE, () -> {
            if(!Bukkit.getOnlinePlayers().isEmpty()){
                List<PlayerInfo> playerInfoList = new ArrayList<>();
                for(Player player : Bukkit.getOnlinePlayers()){
                    playerInfoList.add(new PlayerInfo(player));
                }
                Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
                    for(PlayerInfo info : playerInfoList){
                        SwissKnife.INSTANCE.sqlQuery.updateValues(info);
                    }
                });
            }
        }, 6000, 20L * 60 * updateInterval.get());
    }

    @Override
    public void onDisable(){
        if(periodicalTask != null){
            periodicalTask.cancel();
        }
    }

    @EventHandler
    private void playerJoin(PlayerJoinEvent e){
        if(!isEnabled()) return;
        if(e.getPlayer().hasPlayedBefore()) return;
        if(SwissKnife.INSTANCE.SQL.isConnected()){
            SwissKnife.INSTANCE.sqlQuery.createPlayer(e.getPlayer());
        }
    }

    @EventHandler
    private void playerLeave(PlayerQuitEvent e){
        if(!isEnabled()) return;
        if(SwissKnife.INSTANCE.SQL.isConnected()){
            SwissKnife.INSTANCE.sqlQuery.updateValues(e.getPlayer());
        }
    }
}
