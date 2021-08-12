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

package com.egirlsnation.swissknife.util.player;

import com.egirlsnation.swissknife.hooks.votingPlugin.UserUtils;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class RankUtil {

    private final UserUtils userUtils = new UserUtils();

    public void promoteIfEligible(@NotNull Player player){

        if(!ranksEnabled){
            return;
        }

        if(!player.hasPlayedBefore()) return;

        //Name "PLAY_ONE_MINUTE" is missleading. It's actually in ticks.
        int pt = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if(pt >= getTicksFromHours(midfagHours) && !player.hasPermission("egirls.rank.vet")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add veteran";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.YELLOW + "MidFag" + ChatColor.GREEN + "!");
        }

        if(pt >= getTicksFromHours(oldfagHours) && !player.hasPermission("egirls.rank.oldfag")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add oldfag";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.RED + "OldFag" + ChatColor.GREEN + "!");
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 100, 0);
        }

        if(userUtils.getUserManager() == null) return;
        if(pt >= getTicksFromHours(elderfagHours) && userUtils.getVotes(player) >= 300 && !player.hasPermission("egirls.rank.legend")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add legend";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.AQUA + "ElderFag" + ChatColor.GREEN + "!");
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 100, 0);
            }
        }
    }

    public int getTicksFromHours(int hours){
        int ticks = hours * 20;
        ticks = ticks * 60;
        ticks = ticks * 60;
        return ticks;
    }

    public List<String> getOnlinePlayerRankList(){
        List<String> playerRankList = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            playerRankList.add(p.getDisplayName());
        }
        return playerRankList;
    }

    public List<String> getOnlinePlayerNamesUnderPlaytime(int playtimeHours){
        List<String> playernames = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getStatistic(Statistic.PLAY_ONE_MINUTE) < getTicksFromHours(playtimeHours)){
                playernames.add(p.getName());
            }
        }
        return playernames;
    }
}
