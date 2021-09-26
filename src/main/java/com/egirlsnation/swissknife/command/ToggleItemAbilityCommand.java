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

import com.egirlsnation.swissknife.util.customItem.CustomItemHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleItemAbilityCommand implements CommandExecutor {

    private final CustomItemHandler customItemHandler = new CustomItemHandler();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender){
            sender.sendMessage(ChatColor.RED + "You need to be a player to execute this command.");
            return true;
        }
        Player player = (Player) sender;

        if(customItemHandler.getDisabledPlayersList().contains(player.getUniqueId())){
            player.sendMessage(ChatColor.GOLD + "Draconite item abilities " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "enabled");
            customItemHandler.getDisabledPlayersList().remove(player.getUniqueId());
        }else{
            player.sendMessage(ChatColor.GOLD + "Draconite item abilities " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "disabled");
            customItemHandler.getDisabledPlayersList().add(player.getUniqueId());
        }

        return true;
    }
}
