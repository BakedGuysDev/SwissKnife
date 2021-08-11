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

package com.egirlsnation.swissknife.command;

import com.egirlsnation.swissknife.SwissKnife;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ShitListCommand implements CommandExecutor {

    private final SwissKnife plugin;
    public ShitListCommand(SwissKnife plugin){ this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!sender.isOp()){
            sender.sendMessage(ChatColor.RED + "Go fuck yourself. You don't have enough permissions");
            return true;
        }

        if(!plugin.SQL.isConnected()){
            sender.sendMessage(ChatColor.RED + "Not connected to the database. This command won't work.");
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(ChatColor.RED + "You need to provide some arguments. (add|remove)");
            return true;
        }

        if(args.length == 1){
            sender.sendMessage(ChatColor.RED + "You need to provide a playername.");
            return true;
        }

        if(args[0].equalsIgnoreCase("add")){
            if(plugin.sqlQuery.exists(args[1])){
                plugin.sqlQuery.addToShitlist(args[1]);
                sender.sendMessage(ChatColor.GREEN + "Successfully added " + args[1] + " to the shitlist.");
            }else{
                sender.sendMessage(ChatColor.RED + args[1] + " couldn't be shitlisted, because he isn't in the database.");
            }
            return true;
        }

        if(args[0].equalsIgnoreCase("remove")){
            if(plugin.sqlQuery.exists(args[1])){
                plugin.sqlQuery.removeFromShitlist(args[1]);
                sender.sendMessage(ChatColor.GREEN + "Successfully removed " + args[1] + " from the shitlist.");
            }else{
                sender.sendMessage(ChatColor.RED + args[1] + " couldn't be removed from the shitlist, because he isn't in the database.");
            }
        }

        return true;
    }
}
