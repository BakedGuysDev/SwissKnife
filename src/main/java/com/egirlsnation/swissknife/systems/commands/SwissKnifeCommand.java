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
import com.egirlsnation.swissknife.utils.entity.player.SwissPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class SwissKnifeCommand extends Command {

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
                break;
            }
            case "toggle":{
                if(!(sender instanceof Player)){
                    sendMessage(sender, ChatColor.RED + "You cannot do this command.");
                }
                handleToggle(sender, args);
                break;
            }
            default:{
                sendMessage(sender, ChatColor.RED + "Incorrect arguments. Valid arguments are: modules, info, reload");
                break;
            }
        }
    }

    private void displayInfo(CommandSender sender){
        sender.sendMessage(ChatColor.GOLD + "-------------- [" + ChatColor.LIGHT_PURPLE + "SwissKnife" + ChatColor.GOLD + "] --------------");
        sender.sendMessage(ChatColor.GOLD + "Authors:\n" + ChatColor.LIGHT_PURPLE + "Lerbiq, codingPotato and Killmlana");
        sender.sendMessage(ChatColor.GOLD + "Source code:\n" + ChatColor.LIGHT_PURPLE + "https://github.com/EgirlsNationDev/SwissKnife");
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
                    if(modules.indexOf(module) != (modules.size() - 1)){
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
                sendMessage(sender, ChatColor.YELLOW + "This task is resource intensive, can't be cancelled and will generally take some time, depending on how many players joined your server in the past and your hardware.\n" +
                        ChatColor.YELLOW + "Even tho it runs asynchronously I recommend you don't do this without the server being in some form of a maintenance mode!\n" +
                        ChatColor.YELLOW + "If you're sure you want to run this task do the command again.");
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

        OfflinePlayer[] players = Bukkit.getOfflinePlayers();
        int playerCount = players.length;
        List<PlayerInfo> infos = new ArrayList<>();
        fillDbTask = Bukkit.getScheduler().runTaskAsynchronously(SwissKnife.INSTANCE, () -> {
            long started = System.currentTimeMillis();
            sendMessage(sender, ChatColor.GREEN + "Starting task for getting PlayerInfo of " + playerCount + " players");
            int processedPlayersCount = 0;
            long lastUpdate = started;
            for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()){
                infos.add(new PlayerInfo(offlinePlayer));
                processedPlayersCount++;
                if((System.currentTimeMillis() - lastUpdate) >= 1000){
                    sendMessage(sender, ChatColor.GREEN + "Processed " + processedPlayersCount + "/" + playerCount + " players");
                    lastUpdate = System.currentTimeMillis();
                }
            }
            long firstTaskMs = System.currentTimeMillis() - started;
            sendMessage(sender, ChatColor.GREEN + "Task for getting PlayerInfo of " + playerCount + " players finished in " + TimeUnit.MILLISECONDS.toSeconds(firstTaskMs) + " seconds");
            sendMessage(sender, ChatColor.GREEN + "Starting fillDb task for " + playerCount + " players");
            started = System.currentTimeMillis();
            processedPlayersCount = 0;
            for(PlayerInfo info : infos){
                MySQL.get().getPlayerStatsDriver().updateValues(info);
                processedPlayersCount++;
                if((System.currentTimeMillis() - lastUpdate) >= 1000){
                    sendMessage(sender, ChatColor.GREEN + "Processed " + processedPlayersCount + "/" + playerCount + " players");
                    lastUpdate = System.currentTimeMillis();
                }
            }
            long secondTaskMs = System.currentTimeMillis() - started;
            sendMessage(sender, ChatColor.GREEN + "Fill database task for " + playerCount + " playerCount finished in " + TimeUnit.MILLISECONDS.toSeconds(secondTaskMs) + " seconds");
            sendMessage(sender, ChatColor.GREEN + "Total execution time was: " + TimeUnit.MILLISECONDS.toSeconds(firstTaskMs + secondTaskMs) + " seconds");
            fillDbTask = null;
        });
    }

    private void handleToggle(CommandSender sender, String[] args){
        if(args.length == 1) sender.sendMessage(ChatColor.RED + "No arguments. Possible arguments are: <alerts | petTotems | draconite>");

        Player player = (Player) sender;

        switch(args[1].toLowerCase()){
            case "alerts":{
                if(SwissPlayer.getSwissPlayer(player).hasFeatureEnabled(SwissPlayer.SwissFeature.MODULE_ALERTS)){
                    sendMessage(sender, ChatColor.GOLD + "Module alerts were " + ChatColor.RED + "disabled.");
                }else{
                    sendMessage(sender, ChatColor.GOLD + "Module alerts were " + ChatColor.GREEN + "enabled.");
                }
                SwissPlayer.getSwissPlayer(player).toggleFeature(SwissPlayer.SwissFeature.MODULE_ALERTS);
                break;
            }
            case "pet-totems":{
                if(SwissPlayer.getSwissPlayer(player).hasFeatureEnabled(SwissPlayer.SwissFeature.PET_TOTEMS)){
                    sendMessage(sender, ChatColor.GOLD + "Pet totems were " + ChatColor.RED + "disabled.");
                }else{
                    sendMessage(sender, ChatColor.GOLD + "Pet totems were " + ChatColor.GREEN + "enabled.");
                }
                SwissPlayer.getSwissPlayer(player).toggleFeature(SwissPlayer.SwissFeature.PET_TOTEMS);
            }
            case "draconite":{
                if(SwissPlayer.getSwissPlayer(player).hasFeatureEnabled(SwissPlayer.SwissFeature.DRACONITE_ABILITIES)){
                    sendMessage(sender, ChatColor.GOLD + "Draconite abilities were " + ChatColor.RED + "disabled.");
                }else{
                    sendMessage(sender, ChatColor.GOLD + "Draconite abilities were " + ChatColor.GREEN + "enabled.");
                }
                SwissPlayer.getSwissPlayer(player).toggleFeature(SwissPlayer.SwissFeature.DRACONITE_ABILITIES);
            }
            default:{
                sendMessage(sender, ChatColor.RED + "Incorrect arguments. Possible arguments are: <alerts | petTotems | draconite>");
                break;
            }
        }
    }
}
