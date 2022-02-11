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

import com.egirlsnation.swissknife.utils.handlers.customItems.DraconiteAbilityHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleItemAbilityCommand implements CommandExecutor {

    private final DraconiteAbilityHandler draconiteAbilityHandler = new DraconiteAbilityHandler();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender){
            sender.sendMessage(ChatColor.RED + "You need to be a player to execute this command.");
            return true;
        }
        Player player = (Player) sender;

        if(draconiteAbilityHandler.getDisabledPlayersList().contains(player.getUniqueId())){
            player.sendMessage(ChatColor.GOLD + "Draconite item abilities " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "enabled");
            draconiteAbilityHandler.getDisabledPlayersList().remove(player.getUniqueId());
        }else{
            player.sendMessage(ChatColor.GOLD + "Draconite item abilities " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "disabled");
            draconiteAbilityHandler.getDisabledPlayersList().add(player.getUniqueId());
        }

        return true;
    }
}
