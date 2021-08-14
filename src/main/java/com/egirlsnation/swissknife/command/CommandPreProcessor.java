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

package com.egirlsnation.swissknife.command;

import com.egirlsnation.swissknife.SwissKnife;
import com.egirlsnation.swissknife.util.SpawnRadiusManager;
import com.egirlsnation.swissknife.util.cooldownManager.CommandType;
import com.egirlsnation.swissknife.util.cooldownManager.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.concurrent.TimeUnit;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class CommandPreProcessor  implements Listener {

    private final SwissKnife plugin;
    public CommandPreProcessor(SwissKnife plugin){ this.plugin = plugin; }

    private final CooldownManager cooldownManager = new CooldownManager();
    private final SpawnRadiusManager radiusManager = new SpawnRadiusManager();

    @EventHandler
    public void CommandPreProcessorEvent(PlayerCommandPreprocessEvent e){

        if(e.getMessage().toLowerCase().startsWith("/afk")){
            CommandType type = CommandType.AFK;
            Player player = e.getPlayer();

            // Getting time left for this player for this command
            long timeLeft = System.currentTimeMillis() - cooldownManager.getCommandInfo(player.getUniqueId(), type).getCooldown();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                return;
            }else if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.DEFAULT_COOLDOWN){
                cooldownManager.setCooldown(player.getUniqueId(), System.currentTimeMillis(), type);
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
            long timeLeft = System.currentTimeMillis() - cooldownManager.getCommandInfo(player.getUniqueId(), type).getCooldown();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                return;
            }else if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.DEFAULT_COOLDOWN){
                cooldownManager.setCooldown(player.getUniqueId(), System.currentTimeMillis(), type);
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

            if(!cooldownManager.isOnCooldown(player, type)){
                cooldownManager.setCooldown(player.getUniqueId(), type);
                return;
            }

            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You gotta wait before doing that m8");
            return;
        }

        if(disableCommandsAtSpawn && radiusManager.isInRadius(e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getZ(), disableCommandsRadius)){
            for(String command : radiusLimitedCmds){
                if(e.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "You need to be further from spawn in order to do this command.");
                }
            }
        }

        if(!plugin.SQL.isConnected()) return;
        if(!enableShitlist) return;

        if(plugin.sqlQuery.isShitlisted(e.getPlayer())){
            for(String command : blacklistedCommands){
                if(e.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "You are shitlisted and can't do this command.");
                    break;
                }
            }
        }
    }
}
