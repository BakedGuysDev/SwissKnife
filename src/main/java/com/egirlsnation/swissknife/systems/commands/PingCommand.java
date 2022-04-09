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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.egirlsnation.swissknife.utils.StringUtil.formatPing;

public class PingCommand extends Command {

    public PingCommand(){
        super("ping");
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        if (!(sender instanceof Player)) {
            sendMessage(sender, "You cannot do this command as the console.");
            return;
        }
        if (args.length == 0) {
            Player player = (Player) sender;

            int ping = player.getPing();
            if (ping == 0) {
                sendMessage(sender, ChatColor.RED + "Something went wrong while getting your ping or your ping is 0 ¯\\_(ツ)_/¯");
            } else {
                sendMessage(sender, ChatColor.AQUA + "Your ping is " + formatPing(ping) + ChatColor.AQUA + " ms");
            }
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sendMessage(sender, ChatColor.RED + "Player not online.");
            return;
        }
        int ping = target.getPing();
        if (ping == 0) {
            sendMessage(sender, ChatColor.RED + "Something went wrong while getting ping of " + target.getDisplayName() + " or their ping is 0 ¯\\_(ツ)_/¯");
        } else {
            sendMessage(sender, ChatColor.AQUA + "Ping of " + target.getDisplayName() + " is " + formatPing(ping) + ChatColor.AQUA + " ms");
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, String[] args){
        return Commands.get().playerNamesTabComplete(sender, args, 1);
    }
}
