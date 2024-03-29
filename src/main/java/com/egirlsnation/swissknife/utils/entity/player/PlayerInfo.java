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

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerInfo {
    private final UUID uuid;
    private final String name;
    private final boolean shitlisted;
    private final long playtime;
    private final long firstplayed;
    private final int kills;
    private final int deaths;
    private final int mobkills;
    private final int distanceair;
    private final int distancewalked;
    private final int distancesprinted;
    private final int timesincedeath;
    private final int obsidianMined;


    public PlayerInfo(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.shitlisted = false;
        this.playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        this.firstplayed = player.getFirstPlayed();
        this.kills = player.getStatistic(Statistic.PLAYER_KILLS);
        this.deaths = player.getStatistic(Statistic.DEATHS);
        this.mobkills = player.getStatistic(Statistic.MOB_KILLS);
        this.distanceair = player.getStatistic(Statistic.AVIATE_ONE_CM);
        this.distancewalked = player.getStatistic(Statistic.WALK_ONE_CM);
        this.distancesprinted = player.getStatistic(Statistic.SPRINT_ONE_CM);
        this.timesincedeath = player.getStatistic(Statistic.TIME_SINCE_DEATH);
        this.obsidianMined = player.getStatistic(Statistic.MINE_BLOCK, Material.OBSIDIAN);
    }

    public PlayerInfo(Player player, boolean shitlisted) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.shitlisted = shitlisted;
        this.playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        this.firstplayed = player.getFirstPlayed();
        this.kills = player.getStatistic(Statistic.PLAYER_KILLS);
        this.deaths = player.getStatistic(Statistic.DEATHS);
        this.mobkills = player.getStatistic(Statistic.MOB_KILLS);
        this.distanceair = player.getStatistic(Statistic.AVIATE_ONE_CM);
        this.distancewalked = player.getStatistic(Statistic.WALK_ONE_CM);
        this.distancesprinted = player.getStatistic(Statistic.SPRINT_ONE_CM);
        this.timesincedeath = player.getStatistic(Statistic.TIME_SINCE_DEATH);
        this.obsidianMined = player.getStatistic(Statistic.MINE_BLOCK, Material.OBSIDIAN);
    }

    public PlayerInfo(OfflinePlayer offlinePlayer){
        this.uuid = offlinePlayer.getUniqueId();
        this.name = offlinePlayer.getName();
        this.shitlisted = false;
        this.playtime = offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
        this.firstplayed = offlinePlayer.getFirstPlayed();
        this.kills = offlinePlayer.getStatistic(Statistic.PLAYER_KILLS);
        this.deaths = offlinePlayer.getStatistic(Statistic.DEATHS);
        this.mobkills = offlinePlayer.getStatistic(Statistic.MOB_KILLS);
        this.distanceair = offlinePlayer.getStatistic(Statistic.AVIATE_ONE_CM);
        this.distancewalked = offlinePlayer.getStatistic(Statistic.WALK_ONE_CM);
        this.distancesprinted = offlinePlayer.getStatistic(Statistic.SPRINT_ONE_CM);
        this.timesincedeath = offlinePlayer.getStatistic(Statistic.TIME_SINCE_DEATH);
        this.obsidianMined = offlinePlayer.getStatistic(Statistic.MINE_BLOCK, Material.OBSIDIAN);
    }


    public UUID getUuid() {
        return uuid;
    }

    public String getName(){
        return name;
    }

    public boolean isShitlisted(){
        return shitlisted;
    }

    public long getPlaytime(){
        return playtime;
    }

    public int getKills(){
        return kills;
    }

    public int getDeaths(){
        return deaths;
    }

    public int getMobkills(){
        return mobkills;
    }

    public int getTimesincedeath(){
        return timesincedeath;
    }

    public int getDistanceair(){
        return distanceair;
    }

    public int getDistancewalked(){
        return distancewalked;
    }

    public int getDistancesprinted(){
        return distancesprinted;
    }

    public long getFirstplayed(){
        return firstplayed;
    }

    public int getObsidianMined(){
        return obsidianMined;
    }
}
