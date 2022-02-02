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

package com.egirlsnation.swissknife.utils.player;

import org.bukkit.Material;
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
    private final long distanceair;
    private final long distanceland;
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
        this.distanceland = player.getStatistic(Statistic.WALK_ONE_CM) + player.getStatistic(Statistic.SPRINT_ONE_CM);
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
        this.distanceland = player.getStatistic(Statistic.WALK_ONE_CM) + player.getStatistic(Statistic.SPRINT_ONE_CM);
        this.timesincedeath = player.getStatistic(Statistic.TIME_SINCE_DEATH);
        this.obsidianMined = player.getStatistic(Statistic.MINE_BLOCK, Material.OBSIDIAN);
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

    public long getDistanceair(){
        return distanceair;
    }

    public long getDistanceland(){
        return distanceland;
    }

    public long getFirstplayed(){
        return firstplayed;
    }

    public int getObsidianMined(){
        return obsidianMined;
    }
}
