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
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.modules.database.Shitlist;
import com.egirlsnation.swissknife.systems.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShitListCommand extends Command {

    public ShitListCommand(){
        super("shitlist");
    }

    @Override
    public void onRegister(){
        if(!SwissKnife.INSTANCE.SQL.isConnected()){
            warn("Disabling... This command depends on the MySQL database, which is not connected.");
            toggle();
        }
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        if(!sender.isOp()){
            sendMessage(sender, ChatColor.RED + "Nice try, but you don't have enough permissions :)");
            return;
        }

        if(args.length == 0){
            sendMessage(sender, ChatColor.RED + "You need to provide some arguments. (add|remove)");
            return;
        }

        if(args.length == 1){
            sendMessage(sender, ChatColor.RED + "You need to provide a playername.");
            return;
        }

        if(args[0].equalsIgnoreCase("add")){
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null){
                Modules.get().get(Shitlist.class).addToShitlist(player);
                sendMessage(sender, ChatColor.GREEN + "Shitlisted player " + args[1]);
            }else{
                Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
                    if(MySQL.get().getSqlQuery().exists(args[1])){
                        MySQL.get().getSqlQuery().addToShitlist(args[1]);
                        Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
                            sendMessage(sender, ChatColor.GREEN + "Successfully added " + args[1] + " to the shitlist.");
                        });
                    }else{
                        Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
                            sendMessage(sender, ChatColor.RED + args[1] + " couldn't be shitlisted, because he isn't in the database and isn't online either.");
                        });
                    }
                });
            }
            return;
        }

        if(args[0].equalsIgnoreCase("remove")){
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null){
                boolean wasOnShitlist = Modules.get().get(Shitlist.class).removeFromShitlist(player);
                if(wasOnShitlist){
                    sendMessage(sender, ChatColor.GREEN + "Unshitlisted player " + args[1]);
                }else{
                    sendMessage(sender, ChatColor.RED + "Unable to unshitlist player " + args[1] + " since he wasn't on the shitlist in the first place");
                }
            }else{
                Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
                    if(MySQL.get().getSqlQuery().exists(args[1])){
                        MySQL.get().getSqlQuery().removeFromShitlist(args[1]);
                        Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
                            sendMessage(sender, ChatColor.GREEN + "Successfully removed " + args[1] + " from the shitlist.");
                        });
                    }else{
                        Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
                            sendMessage(sender, ChatColor.RED + args[1] + " couldn't be unshitlisted, because he isn't in the database and isn't online either.");
                        });
                    }
                });
            }
        }
    }


}
