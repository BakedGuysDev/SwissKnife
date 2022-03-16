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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class ToggleItemAbilityCommand extends Command {

    public ToggleItemAbilityCommand(){
        super("toggle-item-ability");
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args){
        if(sender instanceof ConsoleCommandSender){
            sendMessage(sender, ChatColor.RED + "You need to be a player to execute this command.");
            return;
        }

        Commands.get().get(SwissKnifeCommand.class).handleCommand(sender, new String[]{"toggle", "draconite"});
    }
}
