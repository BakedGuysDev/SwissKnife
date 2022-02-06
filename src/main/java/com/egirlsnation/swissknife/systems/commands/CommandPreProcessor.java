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

package com.egirlsnation.swissknife.systems.commands;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.systems.handlers.commandCooldown.CommandType;
import com.egirlsnation.swissknife.systems.handlers.commandCooldown.CooldownHandler;
import com.egirlsnation.swissknife.utils.Config;
import com.egirlsnation.swissknife.utils.LocationUtil;
import com.egirlsnation.swissknife.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.concurrent.TimeUnit;

public class CommandPreProcessor  implements Listener {

    private final SwissKnife plugin;
    public CommandPreProcessor(SwissKnife plugin){ this.plugin = plugin; }

    private final CooldownHandler cooldownHandler = new CooldownHandler();
    private final LocationUtil radiusManager = new LocationUtil();
    private final StringUtil stringUtils = new StringUtil();

    @EventHandler
    public void CommandPreProcessorEvent(PlayerCommandPreprocessEvent e){

        if(e.getMessage().toLowerCase().startsWith("/afk")){
            CommandType type = CommandType.AFK;
            Player player = e.getPlayer();

            // Getting time left for this player for this command
            long timeLeft = System.currentTimeMillis() - cooldownHandler.getCommandInfo(player.getUniqueId(), type).getCooldown();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                return;
            }else if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownHandler.DEFAULT_COOLDOWN){
                cooldownHandler.setCooldown(player.getUniqueId(), System.currentTimeMillis(), type);
                return;
            }
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You gotta wait before doing that m8");
            return;
        }

        if(e.getMessage().toLowerCase().startsWith("/me")){
            CommandType type = CommandType.ME;
            Player player = e.getPlayer();

            // Getting time left for this player for this command
            long timeLeft = System.currentTimeMillis() - cooldownHandler.getCommandInfo(player.getUniqueId(), type).getCooldown();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                return;
            }else if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownHandler.DEFAULT_COOLDOWN){
                cooldownHandler.setCooldown(player.getUniqueId(), System.currentTimeMillis(), type);
                return;
            }
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You gotta wait before doing that m8");
            return;
        }

        if(e.getMessage().toLowerCase().startsWith("/ping")){
            CommandType type = CommandType.PING;
            Player player = e.getPlayer();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                return;
            }

            if(!cooldownHandler.isOnCooldown(player, type)){
                cooldownHandler.setCooldown(player.getUniqueId(), type);
                return;
            }

            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You gotta wait before doing that m8");
            return;
        }

        if(e.getMessage().toLowerCase().startsWith("/refreshrank")){
            CommandType type = CommandType.REFRESHRANK;
            Player player = e.getPlayer();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                return;
            }

            if(!cooldownHandler.isOnCooldown(player, type)){
                cooldownHandler.setCooldown(player.getUniqueId(), type);
                return;
            }

            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You gotta wait before doing that m8");
            return;
        }


        if(e.getMessage().toLowerCase().startsWith("/pay")){
            Player player = e.getPlayer();

            if(player.hasPermission("egirls.rank.newfag")){
                return;
            }

            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You need to be NewFag to execute this command");
            return;
        }

        if(Config.instance.disableCommandsAtSpawn && radiusManager.isInRadius(e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getZ(), Config.instance.disableCommandsRadius)){
            for(String command : Config.instance.radiusLimitedCmds){
                if(e.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "You need to be further from spawn in order to do this command.");
                }
            }
        }

        if(Config.instance.coordsCommandsEnabled){
            for(String command : Config.instance.coordsCommands){
                if(e.getMessage().toLowerCase().startsWith(command)){
                    e.setMessage(e.getMessage().replaceAll(Config.instance.coordsPlaceholder, stringUtils.getCoordsPlaceholderFormatted(e.getPlayer())));
                    break;
                }
            }

        }

        if(!plugin.SQL.isConnected()) return;
        if(!Config.instance.enableShitlist) return;

        if(plugin.sqlQuery.isShitlisted(e.getPlayer())){
            for(String command : Config.instance.blacklistedCommands){
                if(e.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "You are shitlisted and can't do this command.");
                    break;
                }
            }
        }
    }
}
