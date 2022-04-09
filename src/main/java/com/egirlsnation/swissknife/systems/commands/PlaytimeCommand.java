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

package com.egirlsnation.swissknife.systems.commands;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaytimeCommand extends Command {


    public PlaytimeCommand(){
        super("playtime");
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        if(!(sender instanceof Player) && args.length == 0){
            sendMessage(sender, ChatColor.RED + "You cannot execute this command on yourself as a console!");
            return;
        }

        if(args.length == 0){
            Player player = (Player) sender;
            long ptTicks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            sendMessage(sender, ChatColor.AQUA + "Your playtime is " + formatPlayTime(ptTicks));
            return;
        }
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if(target != null){
            long ptTicks = target.getStatistic(Statistic.PLAY_ONE_MINUTE);
            sendMessage(sender, ChatColor.AQUA + "Playtime of " + targetName + " is " + formatPlayTime(ptTicks));
            return;
        }

        if(!MySQL.get().isConnected()){
            sendMessage(sender, ChatColor.RED + "Couldn't get playtime of " + targetName + ".\n" + ChatColor.RED + "This can mean that the player isn't in the database or isn't online.");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
            if(!MySQL.get().getPlayerStatsDriver().exists(targetName)){
                Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
                    sendMessage(sender, ChatColor.RED + "Couldn't get playtime of " + targetName + ".\n" + ChatColor.RED + "This can mean that the player isn't in the database or isn't online.");
                });
                return;
            }
            String playtime = formatPlayTime(MySQL.get().getPlayerStatsDriver().getPlaytime(targetName));
            Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
                sendMessage(sender, ChatColor.RED + "Playtime of " + targetName + " is " + playtime);
            });
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, String[] args){
        return Commands.get().playerNamesTabComplete(sender, args, 1);
    }

    private String formatPlayTime(long ticks){
        long pt = ticks / 20;

        long day = (int) TimeUnit.SECONDS.toDays(pt);
        long hours = TimeUnit.SECONDS.toHours(pt) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(pt) - (TimeUnit.SECONDS.toHours(pt)* 60);
        long second = TimeUnit.SECONDS.toSeconds(pt) - (TimeUnit.SECONDS.toMinutes(pt) *60);

        return day + " days, " + hours + " hours " + minute + " minutes " + second + " seconds";
    }
}
