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
import com.egirlsnation.swissknife.systems.handlers.commandCooldown.CooldownHandler;
import com.egirlsnation.swissknife.systems.handlers.commandCooldown.CommandType;
import com.egirlsnation.swissknife.utils.player.HealthUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class KillCommand implements CommandExecutor {

    private final SwissKnife swissKnife;

    public KillCommand(SwissKnife swissKnife){ this.swissKnife = swissKnife; }

    private final HealthUtil healthUtil = new HealthUtil();

    private final CommandType killCommand = CommandType.KILL;
    private final CooldownHandler cooldownHandler = new CooldownHandler();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // Console with no args
        if(args.length == 0 && !(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You can't kill yourself m8.");
            return true;
        }

        // Console with arguments
        if(args.length > 0 && !(sender instanceof Player)){
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null){
                sender.sendMessage(ChatColor.RED + "That player isn't online.");
                return true;
            }
            healthUtil.killPlayer(player, swissKnife);
            sender.sendMessage(ChatColor.GREEN + "Killed " + player.getDisplayName());
            return true;
        }

        // Player with no args
        if(args.length == 0){
            Player player = (Player) sender;

            // Getting time left for this player for this command
            long timeLeft = System.currentTimeMillis() - cooldownHandler.getCommandInfo(player.getUniqueId(), killCommand).getCooldown();

            if(player.hasPermission("swissknife.cooldown.bypass")){
                healthUtil.killPlayer(player, swissKnife);
                return true;
            }else if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownHandler.DEFAULT_COOLDOWN){
                cooldownHandler.setCooldown(player.getUniqueId(), System.currentTimeMillis(), killCommand);
                healthUtil.killPlayer(player, swissKnife);
                return true;
            }
            player.sendMessage(ChatColor.RED + "You gotta wait before doing that m8");
            return true;
        }

        // Player with args (with permission only)
        Player cmdSender = (Player) sender;
        if(cmdSender.hasPermission("swissknife.kill.others")){
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                cmdSender.sendMessage(ChatColor.RED + "That player isn't online");
                return true;
            }
            healthUtil.killPlayer(player, swissKnife);
            cmdSender.sendMessage(ChatColor.GREEN + "Killed " + player.getDisplayName());
            return true;
        }
        sender.sendMessage(ChatColor.RED + "You do not have enough permissions.");
        return true;
    }



}
