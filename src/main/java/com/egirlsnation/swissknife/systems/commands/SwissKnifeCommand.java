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
import com.egirlsnation.swissknife.systems.Systems;
import com.egirlsnation.swissknife.systems.modules.Categories;
import com.egirlsnation.swissknife.systems.modules.Category;
import com.egirlsnation.swissknife.systems.modules.Module;
import com.egirlsnation.swissknife.systems.modules.Modules;
import com.egirlsnation.swissknife.systems.sql.MySQL;
import com.egirlsnation.swissknife.utils.entity.player.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwissKnifeCommand extends Command{

    public SwissKnifeCommand(){
        super("swissknife");
    }

    //TODO: Make it less janky

    private boolean fillDbDidCommand = false;
    private BukkitTask fillDbTask = null;

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        if(args.length == 0){
            displayInfo(sender);
            return;
        }
        switch(args[0].toLowerCase()){
            case "reload":{
                if(!sender.hasPermission("swissknife.command.reload")){
                    sendMessage(sender, ChatColor.RED + "You don't have enough permission to do this command");
                    return;
                }
                Systems.reload();
                sendMessage(sender, ChatColor.GREEN + "Reloaded configurations");
                break;
            }
            case "info":{
                displayInfo(sender);
                break;
            }
            case "modules":{
                if(!sender.hasPermission("swissknife.command.modules")){
                    sendMessage(sender, ChatColor.RED + "You don't have enough permission to do this command");
                    return;
                }
                handleModules(sender, args);
                break;
            }
            case "filldb":{
                if(!(sender instanceof ConsoleCommandSender)){
                    sendMessage(sender, ChatColor.RED + "This command is console only!");
                }
                handleFillDb(sender);
            }
            default:{
                sendMessage(sender, ChatColor.RED + "Incorrect arguments. Valid arguments are: modules, info, reload");
                break;
            }
        }
    }

    private void displayInfo(CommandSender sender){
        sender.sendMessage( ChatColor.GOLD + "-------------- [" + ChatColor.LIGHT_PURPLE + "SwissKnife" + ChatColor.GOLD + "] --------------");
        sender.sendMessage( ChatColor.GOLD + "Authors:\n" + ChatColor.LIGHT_PURPLE + "Lerbiq, codingPotato and Killmlana");
        sender.sendMessage( ChatColor.GOLD + "Source code:\n" + ChatColor.LIGHT_PURPLE + "https://github.com/EgirlsNationDev/SwissKnife");
    }

    private void handleModules(CommandSender sender, String[] args){
        if(args[1].equalsIgnoreCase("list")){
            Map<Category, List<Module>> moduleMap = new HashMap<>(6);
            moduleMap.put(Categories.Database, Modules.get().getGroup(Categories.Database));
            moduleMap.put(Categories.Entity, Modules.get().getGroup(Categories.Entity));
            moduleMap.put(Categories.Illegals, Modules.get().getGroup(Categories.Illegals));
            moduleMap.put(Categories.Player, Modules.get().getGroup(Categories.Player));
            moduleMap.put(Categories.World, Modules.get().getGroup(Categories.World));
            moduleMap.put(Categories.Misc, Modules.get().getGroup(Categories.Misc));
            moduleMap.put(Categories.EgirlsNation, Modules.get().getGroup(Categories.EgirlsNation));

            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.GOLD).append("---------- [").append(ChatColor.LIGHT_PURPLE).append(" SwissKnife | Modules").append(ChatColor.GOLD).append("] ----------\n");
            for(Category category : moduleMap.keySet()){

                sb.append(ChatColor.GOLD).append(category.name).append("(").append(moduleMap.get(category).size()).append("): ");
                List<Module> modules = moduleMap.get(category);
                for(Module module : modules){
                    if(module.isEnabled()){
                        sb.append(ChatColor.GREEN).append(module.name);
                    }else{
                        sb.append(ChatColor.RED).append(module.name);
                    }
                    if(modules.indexOf(module) != (modules.size() -1)){
                        sb.append(ChatColor.WHITE).append(", ");
                    }else{
                        sb.append("\n").append(" \n");
                    }
                }
            }
            sender.sendMessage(sb.toString());

        }
    }

    private void handleFillDb(CommandSender sender){
        if(!fillDbDidCommand){
            if(fillDbTask == null){
                sendMessage(sender, ChatColor.YELLOW + "This task is resource intensive, can't be cancelled and will generally take a long time, depending on how many players joined your server in the past.\n" +
                        "You shouldn't be doing this without the server being in some form of a maintenance mode!\n" +
                        "If you're sure you want to run this task do the command again.");
                fillDbDidCommand = true;
            }else{
                sendMessage(sender, ChatColor.RED + "You already have a task for filling the database running!");
            }
            return;
        }
        if(!MySQL.get().isConnected()){
            sendMessage(sender, ChatColor.YELLOW + "MySQL database isn't connected. Can't run the task.");
            return;
        }
        fillDbTask = Bukkit.getScheduler().runTask(SwissKnife.INSTANCE, () -> {
            OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
            int processedPlayersCount = 0;
            long lastUpdate = System.currentTimeMillis();
            sendMessage(sender, ChatColor.GREEN + "Starting fill database task for " + offlinePlayers.length + " players");
            for(OfflinePlayer offlinePlayer : offlinePlayers){
                PlayerInfo info = new PlayerInfo(offlinePlayer);
                MySQL.get().getPlayerStatsDriver().updateValues(info);
                processedPlayersCount++;
                if((System.currentTimeMillis() - lastUpdate) >= 1000){
                    sendMessage(sender, ChatColor.GREEN + "Processed " + processedPlayersCount + "/" + offlinePlayers.length + " players");
                }
            }
        });
    }
}
