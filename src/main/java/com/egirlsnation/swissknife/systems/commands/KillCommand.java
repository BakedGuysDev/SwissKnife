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

import com.egirlsnation.swissknife.utils.entity.player.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillCommand extends Command {

    public KillCommand(){
        super("kill");
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        // Console with no args
        if(args.length == 0 && !(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You can't kill yourself m8.");
            return;
        }

        // Console with arguments
        if(args.length > 0 && !(sender instanceof Player)){
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null){
                sender.sendMessage(ChatColor.RED + "That player isn't online.");
                return;
            }
            PlayerUtil.killPlayer(player);
            sender.sendMessage(ChatColor.GREEN + "Killed " + player.getDisplayName());
            return;
        }

        // Player with no args
        if(args.length == 0){
            Player player = (Player) sender;

            PlayerUtil.killPlayer(player);
            return;
        }

        // Player with args (with permission only)
        Player cmdSender = (Player) sender;
        if(cmdSender.hasPermission("swissknife.kill.others")){
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null){
                cmdSender.sendMessage(ChatColor.RED + "That player isn't online");
                return;
            }
            PlayerUtil.killPlayer(player);
            cmdSender.sendMessage(ChatColor.GREEN + "Killed " + player.getDisplayName());
            return;
        }
        sender.sendMessage(ChatColor.RED + "You do not have enough permissions.");
    }
}
